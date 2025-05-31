package com.example.sparfuchsapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparfuchsapp.data.dataClasses.Store
import com.example.sparfuchsapp.data.dataClasses.StoreRepository
import com.example.sparfuchsapp.data.remote.dto.ProductPriceHistoryDTO
import com.example.sparfuchsapp.data.repository.ProductRepository
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import com.example.sparfuchsapp.ui.components.StorePriceHistoryChart
import java.time.format.DateTimeFormatter

@Composable
fun ProductSearchScreen(
    initialProductName: String = "No product selected",
    initialPriceBilla: Double? = null,
    initialPriceSpar: Double? = null,
    padding: PaddingValues = PaddingValues()
) {
    val repository = remember { ProductRepository() }
    var barcode by remember { mutableStateOf("5449000000996") } //Default Value For presentation
    var productName by remember { mutableStateOf("No product selected") }
    var priceBilla by remember { mutableStateOf<Double?>(null) }
    var priceSpar by remember { mutableStateOf<Double?>(null) }
    var priceHistories by remember { mutableStateOf<Map<Store, List<ProductPriceHistoryDTO>>>(emptyMap()) }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        ) {
            // Barcode input
            OutlinedTextField(
                value = barcode,
                onValueChange = { barcode = it },
                label = { Text("Barcode") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Button triggers fetching both stores
            Button(
                onClick = {
                    coroutineScope.launch {
                        val storesToCheck = listOf(1L, 2L) // Billa, Spar
                        val results = repository.getProductFromStores(barcode, storesToCheck)

                        // Extract Billa and Spar prices
                        productName = results.values.firstOrNull()?.name ?: "No product found"
                        priceBilla = results[StoreRepository.getStore(1L)]?.price
                        priceSpar = results[StoreRepository.getStore(2L)]?.price

                        val product = results.values.firstOrNull()
                        if (product != null) {
                            priceHistories = repository.getPriceHistoriesFromStores(
                                product = product,
                                storeIds = storesToCheck
                            )
                        }
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Search Product")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Product: $productName",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))


            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Billa: ${priceBilla?.let { "€%.2f".format(it) } ?: "N/A"}")

                    val symbol = when {
                        priceBilla == null || priceSpar == null -> "?"
                        priceBilla!! < priceSpar!! -> "<"
                        priceBilla!! > priceSpar!! -> ">"
                        else -> "="
                    }

                    Text(symbol, fontSize = 20.sp)

                    Text("Spar: ${priceSpar?.let { "€%.2f".format(it) } ?: "N/A"}")
                }
            }
            HorizontalDivider(Modifier.padding(vertical = 16.dp))
            Text("Price History", style = MaterialTheme.typography.titleMedium)

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                priceHistories.forEach { (store, historyList) ->

                    val sortedHistory = historyList.sortedByDescending { it.endTime }

                    item {
                        StorePriceHistoryChart(storeName = store.name, history = sortedHistory)
                    }

//                    // Now list full history descending
//                    items(sortedHistory) { history ->
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 16.dp, vertical = 4.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Text("€%.2f".format(history.price))
//                            Text(history.endTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm")))
//                        }
//                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductSearchScreenPreview() {
    ProductSearchScreen(
        initialProductName = "Sample Product",
        initialPriceBilla = 2.49,
        initialPriceSpar = 2.39
    )
}
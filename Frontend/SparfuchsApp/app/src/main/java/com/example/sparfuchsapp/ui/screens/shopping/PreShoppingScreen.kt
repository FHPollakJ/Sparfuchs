package com.example.sparfuchsapp.ui.screens.shopping

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sparfuchsapp.data.dataClasses.Store
import com.example.sparfuchsapp.ui.screens.shopping.ShoppingViewModel

@Composable
fun PreShoppingScreen(
    padding: PaddingValues,
    viewModel: ShoppingViewModel,
    onStartPurchase: () -> Unit
) {
    val stores = listOf<Store>(Store(1, "Spar"), Store(2, "Billa"))
    var selectedStore by remember { mutableStateOf(stores.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Start Shopping")
        Spacer(modifier = Modifier.height(16.dp))

        //Dropdown
        var expanded by remember {mutableStateOf(false)}
        Box {
            Button(
                onClick = { expanded = true},
            ) {
                Text("Store: ${selectedStore.name}")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                stores.forEach { store ->
                    DropdownMenuItem(
                        text = { Text(store.name) },
                        onClick = {
                            selectedStore = store
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.startPurchase(selectedStore.id)
                onStartPurchase()
            }
        ){
            Text("Start Shopping")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreShoppingScreenPreview() {
    PreShoppingScreen(padding = PaddingValues(0.dp), viewModel = ShoppingViewModel(), onStartPurchase = {})
}
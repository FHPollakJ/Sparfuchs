package com.example.sparfuchsapp.ui.screens.shopping

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sparfuchsapp.data.remote.dto.PurchaseProductDTO
import com.example.sparfuchsapp.ui.components.ProductCard
import com.example.sparfuchsapp.ui.icons.CustomIcons

@ExperimentalMaterial3ExpressiveApi
@Composable
fun ShoppingScreen(padding: PaddingValues, viewModel: ShoppingViewModel) {
    val purchaseState by viewModel.purchase.collectAsState()
    val productList = purchaseState?.products ?: emptyList()

    var fabMenuExpanded by rememberSaveable { mutableStateOf(false)}
    BackHandler(fabMenuExpanded) { fabMenuExpanded = false }

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
            Text("Have fun shopping at ${purchaseState?.storeId ?: "Unknown Store"}")
            if(productList.isEmpty())
                Text("Your shopping list is empty")
            else {
                Text("Your Shopping List:")
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(
                        items = productList
                    ) { productResponse ->
                        val product = PurchaseProductDTO(
                            purchaseId = purchaseState?.purchaseId ?: return@items,
                            productName = productResponse.productName,
                            quantity = productResponse.quantity,
                            discount = productResponse.discount,
                            price = productResponse.price,
                            barcode = ""
                        )
                        ProductCard(
                            product = product,
                            onAmountChange = { newQty ->
                                viewModel.addProductToPurchase(product.copy(quantity = newQty))
                            },
                            onRemove = {
                                viewModel.removeProductFromPurchase(product)
                            }
                        )
                    }
                }
            }
        }
        FloatingActionButtonMenu(
            expanded = fabMenuExpanded,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            button = {
                ToggleFloatingActionButton(
                    checked = fabMenuExpanded,
                    onCheckedChange = { fabMenuExpanded = it }
                ) {
                    Icon(
                        imageVector = if (fabMenuExpanded) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = if (fabMenuExpanded) "Close menu" else "Open menu"
                    )
                }
            }
        ) {
            FloatingActionButtonMenuItem(
                onClick = {
                    fabMenuExpanded = false
                    // Add manually
                },
                icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                text = { Text("Add Product") }
            )
            FloatingActionButtonMenuItem(
                onClick = {
                    fabMenuExpanded = false
                    // Scan
                },
                icon = { Icon(CustomIcons.BarcodeScanner, contentDescription = null) },
                text = { Text("Scan Product") }
            )
            FloatingActionButtonMenuItem(
                onClick = {
                    fabMenuExpanded = false
                    // Finish shopping
                },
                icon = { Icon(CustomIcons.FinishShopping, contentDescription = null) },
                text = { Text("Finish Shopping") }
            )
        }
    }
}


@Composable
fun AddProductButton(
    modifier: Modifier = Modifier,
    onclick: () -> Unit
) {
    // Show a button that adds a new product
    FloatingActionButton(
        onClick = onclick,
        modifier = modifier
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add Product")
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview(showBackground = true, name = "ShoppingScreenPreview", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ShoppingScreenPreview() {
    ShoppingScreen(padding = PaddingValues(0.dp), viewModel = ShoppingViewModel())
}
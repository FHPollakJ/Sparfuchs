package com.example.sparfuchsapp.ui.screens.shopping

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sparfuchsapp.ui.components.ProductCard
import androidx.compose.runtime.getValue
import com.example.sparfuchsapp.data.remote.dto.PurchaseProductDTO

@Composable
fun ShoppingScreen(padding: PaddingValues, viewModel: ShoppingViewModel) {
    val purchaseState by viewModel.purchase.collectAsState()
    val productList = purchaseState?.products ?: emptyList()

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
        AddProductButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(padding)
        ) {
            //TODO: showProductSelector = true;
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

@Preview(showBackground = true, name = "ShoppingScreenPreview", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ShoppingScreenPreview() {
    ShoppingScreen(padding = PaddingValues(0.dp), viewModel = ShoppingViewModel())
}
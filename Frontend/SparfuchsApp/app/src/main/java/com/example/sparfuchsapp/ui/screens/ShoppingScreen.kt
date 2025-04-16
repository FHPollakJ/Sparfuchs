package com.example.sparfuchsapp.ui.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sparfuchsapp.data.dataClasses.Product
import com.example.sparfuchsapp.ui.components.ProductCard
import com.example.sparfuchsapp.ui.screens.viewModels.ShoppingViewModel

@Composable
fun ShoppingScreen(padding: PaddingValues, viewModel: ShoppingViewModel) {
    // A list to hold the products in the cart
    val productList = viewModel.productList

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
            if(productList.isEmpty())
                Text("Your shopping list is empty")
            else {
                Text("Your Shopping List:")
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(productList) { product ->
                        ProductCard(
                            product = product,
                            onAmountChange = { newAmount ->
                                viewModel.updateProduct(product, newAmount)
                            },
                            onRemove = {
                            viewModel.removeProduct(product)
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
            viewModel.addProduct(Product(name = "Coke", price = 20.0, barcode = "1234567890")) }
    }
}


@Composable
fun AddProductButton(
    modifier: Modifier = Modifier,
    onAddProduct: (Product) -> Unit
) {
    // Show a button that adds a new product
    FloatingActionButton(onClick = {
        // Trigger the action to add a product
        onAddProduct(Product(name = "Coke", price = 20.0, barcode = "1234567890"))
        },
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
package com.example.sparfuchsapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sparfuchsapp.data.remote.dto.PurchaseProductDTO


@Composable
fun ProductCard(
    product: PurchaseProductDTO,
    onAmountChange: (Int) -> Unit,
    onRemove: (Any?) -> Unit
) {
    var amountText by remember { mutableStateOf(product.quantity.toString()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Column(Modifier.padding(12.dp)){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
                    .height(46.dp)
            ){
                Text(text = product.productName, style = MaterialTheme.typography.titleLarge)
                Text(text = "€ %.2f".format(product.price), style = MaterialTheme.typography.titleMedium)
            }
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            if (product.quantity > 1) {
                                onAmountChange(product.quantity - 1)
                            } else {
                                onRemove(product)
                            }
                        }
                    ) {
                        if (product.quantity > 1){
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease")
                        } else {
                            Icon(Icons.Default.Delete, contentDescription = "Remove")
                        }

                    }
                    TextField(
                        value = amountText,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) {
                                amountText = newValue
                                newValue.toIntOrNull()?.let { onAmountChange(it) }
                            }
                        },
                        modifier = Modifier
                            .width(75.dp)
                            .height(75.dp),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    )

                    IconButton(onClick = { onAmountChange(product.quantity + 1) }) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase")
                    }
                }
                Text(text = "Total:\n€ %.2f".format(product.total), style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    val exampleProduct = PurchaseProductDTO(
        productName = "Coca Cola",
        price = 1.29,
        barcode = "0",
        quantity = 2,
        purchaseId = 1,
        discount = 25
    )

    ProductCard(
        product = exampleProduct,
        onAmountChange = {},
        onRemove = {}
    )
}
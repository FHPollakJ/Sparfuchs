package com.example.sparfuchsapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sparfuchsapp.data.remote.dto.PurchaseProductDTO
import com.example.sparfuchsapp.data.remote.dto.calculateTotal
import com.example.sparfuchsapp.ui.icons.CustomIcons
import com.example.sparfuchsapp.ui.theme.moneySavedLight


@Composable
fun ProductCard(
    product: PurchaseProductDTO,
    onAmountChange: (Int) -> Unit,
    onRemove: (Any?) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = product.productName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    if (product.discount > 0) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = CustomIcons.Rebate,
                            contentDescription = "Discount Applied",
                            tint = moneySavedLight,
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            text = "-${product.discount}%",
                            fontWeight = FontWeight.Bold,
                            color = moneySavedLight
                        )
                    }
                }
                Text(
                    text = "€ %.2f".format(product.price),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            if (product.quantity > 1) onAmountChange(product.quantity - 1)
                            else onRemove(product)
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (product.quantity > 1) Icons.Default.Remove else Icons.Default.Delete,
                            contentDescription = "Decrease"
                        )
                    }

                    QuantityInput(
                        quantity = product.quantity,
                        onQuantityChange = { newQty -> onAmountChange(newQty) },
                        onRemove = { onRemove(product) }
                    )

                    IconButton(
                        onClick = { onAmountChange(product.quantity + 1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase")
                    }
                }

                val originalTotal = product.price * product.quantity
                val discountedTotal = product.calculateTotal()

                DiscountedPrice(
                    originalPrice = originalTotal,
                    discountedPrice = discountedTotal,
                    hasDiscount = product.discount > 0
                )
            }
        }
    }
}

@Composable
fun QuantityInput(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    var text by remember { mutableStateOf(quantity.toString()) }

    // Keep text state synced with quantity prop
    LaunchedEffect(quantity) {
        if (text != quantity.toString()) {
            text = quantity.toString()
        }
    }

    Box(
        modifier = Modifier
            .width(48.dp)
            .height(36.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(6.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(6.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = text,
            onValueChange = { newValue ->
                // Allow only digits
                if (newValue.all { it.isDigit() }) {
                    val newQuantity = newValue.toIntOrNull()
                    if (newQuantity != null && newQuantity > 0) {
                        text = newValue
                        onQuantityChange(newQuantity)
                    } else if (newValue.isEmpty()) {
                        text = ""
                    } else {
                        onRemove()
                    }
                }
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.titleLarge.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DiscountedPrice(
    originalPrice: Double,
    discountedPrice: Double,
    hasDiscount: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (hasDiscount) {
            Text(
                text = "€ %.2f".format(discountedPrice),
                style = MaterialTheme.typography.titleLarge,
                color = moneySavedLight
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "€ %.2f".format(originalPrice),
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.LineThrough,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            )
        } else {
            Text(
                text = "€ %.2f".format(originalPrice),
                style = MaterialTheme.typography.titleLarge
            )
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
        //, discountType = DiscountType.PERCENTAGE_OFF_ONE_ITEM
    )

    ProductCard(
        product = exampleProduct,
        onAmountChange = {},
        onRemove = {}
    )
}
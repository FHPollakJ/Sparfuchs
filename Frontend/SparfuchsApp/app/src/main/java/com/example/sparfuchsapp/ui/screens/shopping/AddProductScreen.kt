package com.example.sparfuchsapp.ui.screens.shopping

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sparfuchsapp.data.remote.dto.PurchaseProductDTO

@Composable
fun AddProductScreen(
    purchaseId: Long,
    scanBarcode: String? = null,
    onSave: (PurchaseProductDTO) -> Unit,
    onCancel: () -> Unit,
    padding: PaddingValues
) {
    var barcode by remember { mutableStateOf(scanBarcode) }
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var price by remember { mutableStateOf("") }
    var discount by remember { mutableStateOf("0") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text("Add Product", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.padding(8.dp))

            if (scanBarcode != null) {
                OutlinedTextField(
                    value = barcode ?: "",
                    onValueChange = { barcode = it },
                    label = { Text("Barcode") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = price,
                onValueChange = { input ->
                    val regex = Regex("""^\d*\.?\d{0,2}$""")
                    if (input.isEmpty() || input.matches(regex)) {
                        price = input
                    }
                },
                label = { Text("Price Each") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(Modifier.padding(top = 16.dp, start = 8.dp, end = 8.dp)) {
                Button(onClick = onCancel, modifier = Modifier.weight(1f)) {
                    Text("Cancel")
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {
                        val dto = PurchaseProductDTO(
                            purchaseId = purchaseId,
                            barcode = scanBarcode?.takeIf { it.isNotBlank() } ?: "NOBARCODE",
                            productName = name,
                            quantity = quantity.toIntOrNull() ?: 1,
                            discount = discount.toIntOrNull() ?: 0,
                            price = price.toDoubleOrNull() ?: 0.0
                        )
                        onSave(dto)
                    },
                    modifier = Modifier.weight(1f),
                    enabled = name.isNotBlank() && price.toDoubleOrNull() != null
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AddProductScreenPreview() {
    AddProductScreen(
        purchaseId = 1,
        scanBarcode = "191572112347",
        onSave = {},
        onCancel = {},
        padding = PaddingValues(0.dp)
    )
}
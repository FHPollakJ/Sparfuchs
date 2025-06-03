package com.example.sparfuchsapp.ui.screens.shopping

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sparfuchsapp.data.remote.dto.ProductWithPriceDTO
import com.example.sparfuchsapp.data.remote.dto.PurchaseProductDTO
import com.example.sparfuchsapp.utils.CameraPreviewWithScanner
import com.example.sparfuchsapp.utils.Routes
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(
    navController: NavController,
    viewModel: ShoppingViewModel,
    padding: PaddingValues = PaddingValues(),
) {
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var showNewBarcodeDialog by remember { mutableStateOf(false) }
    var scannedBarcode by remember { mutableStateOf("") }
    var isCheckingBarcode by remember { mutableStateOf(false) }
    var showProductFoundDialog by remember { mutableStateOf(false) }
    var foundProduct by remember { mutableStateOf<ProductWithPriceDTO?>(null) }
    val hasScanned = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        }
    }

    if (permissionState.status.isGranted) {
        Box(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(padding)
        ) {
            CameraPreviewWithScanner(
                onBarcodeScanned = { barcode ->
                    if (!isCheckingBarcode) {
                        isCheckingBarcode = true
                        Log.d("Scanner", "Scanned barcode: $barcode")
                        viewModel.getProduct(barcode) { product ->
                            Log.d("Scanner", "Product found? ${product != null}")
                            if (product != null) {
                                foundProduct = product
                                showProductFoundDialog = true
                            } else {
                                scannedBarcode = barcode
                                showNewBarcodeDialog = true
                            }
                            isCheckingBarcode = false
                        }
                    }
                },
                hasScanned = hasScanned
            )

            if (showNewBarcodeDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showProductFoundDialog = false
                        hasScanned.value = false
                                       },
                    title = { Text("New Barcode Detected") },
                    text = { Text("Barcode $scannedBarcode not found in database. Would you like to add it?") },
                    confirmButton = {
                        TextButton(onClick = {
                            showNewBarcodeDialog = false
                            navController.navigate("${Routes.ADD_PRODUCT}/${viewModel.purchase.value?.purchaseId}/${viewModel.purchase.value?.storeId}?scanBarcode=$scannedBarcode")
                        }) {
                            Text("Add Product")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showNewBarcodeDialog = false
                            navController.popBackStack()
                        }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            if (showProductFoundDialog && foundProduct != null) {
                AlertDialog(
                    onDismissRequest = {
                        showProductFoundDialog = false
                        hasScanned.value = false
                                       },
                    title = { Text("Product Found") },
                    text = { Text("Add '${foundProduct!!.name}' to your purchase?") },
                    confirmButton = {
                        TextButton(onClick = {
                            showProductFoundDialog = false
                            viewModel.addProductToPurchase(
                                PurchaseProductDTO(
                                    purchaseId = viewModel.purchase.value?.purchaseId ?: 0L,
                                    barcode = foundProduct!!.barcode,
                                    productName = foundProduct!!.name,
                                    quantity = 1,
                                    discount = 0,
                                    price = foundProduct!!.price
                                )
                            )
                            navController.popBackStack()
                        }) {
                            Text("Add Product")
                        }
                    },
                    dismissButton = {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) { TextButton(onClick = {
                                showProductFoundDialog = false
                                navController.popBackStack()
                            }) {
                                Text("Cancel")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(onClick = {
                                showProductFoundDialog = false
                                hasScanned.value = false
                                viewModel.addProductToPurchase(
                                    PurchaseProductDTO(
                                        purchaseId = viewModel.purchase.value?.purchaseId ?: 0L,
                                        barcode = foundProduct!!.barcode,
                                        productName = foundProduct!!.name,
                                        quantity = 1,
                                        discount = 0,
                                        price = foundProduct!!.price
                                    )
                                )
                            }) {
                                Text("Scan More")
                            }
                        }
                    }
                )
            }

        }
    } else {
        Box(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Companion.Center
        ) {
            Text("Camera permission required to scan barcodes.")
        }
    }
}
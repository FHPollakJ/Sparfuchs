package com.example.sparfuchsapp.ui.screens.shopping

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sparfuchsapp.data.remote.dto.EditPurchaseProductDTO
import com.example.sparfuchsapp.ui.components.ProductCard
import com.example.sparfuchsapp.ui.components.ShoppingSummaryCard
import com.example.sparfuchsapp.ui.icons.CustomIcons
import com.example.sparfuchsapp.utils.Routes
import com.example.sparfuchsapp.utils.toPurchaseProductDTO

@ExperimentalMaterial3ExpressiveApi
@Composable
fun ShoppingScreen(
    padding: PaddingValues,
    viewModel: ShoppingViewModel,
    navController: NavController
) {
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
            val totalSpent = productList.sumOf { (it.price * (100 - it.discount) / 100.0) * it.quantity }
            val totalSaved = productList.sumOf { (it.price * it.discount / 100.0) * it.quantity }

            ShoppingSummaryCard(
                storeID = purchaseState?.storeId ?: 0,
                totalSpent = totalSpent,
                totalSaved = totalSaved,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(8.dp))

            if(productList.isEmpty())
                Text("Scan your first product to add it to your shopping list!")
            else {

                Text("Your Shopping List:")

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(productList, key = {it.id}) { productResponse ->
                        val purchaseId = purchaseState?.purchaseId ?: return@items
                        val product = productResponse.toPurchaseProductDTO(purchaseId)

                        // Context menu
                        var showOptions by remember { mutableStateOf(false) }
                        var showDiscountDialog by remember { mutableStateOf(false) }
                        var showPriceDialog by remember { mutableStateOf(false) }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onLongPress = {
                                            showOptions = true
                                        }
                                    )
                                }
                        ) {
                            ProductCard(
                                product = product,
                                onAmountChange = { newQty ->
                                    viewModel.editProductInPurchase(
                                        EditPurchaseProductDTO(
                                            id = productResponse.id,
                                            purchaseId = purchaseId,
                                            quantity = newQty,
                                            discount = productResponse.discount,
                                            price = productResponse.price
                                        )
                                    )
                                    viewModel.getPurchase(purchaseId)
                                },
                                onRemove = {
                                    viewModel.removeProductFromPurchase(product)
                                }
                            )
                        }

                        if (showOptions) {
                            AlertDialog(
                                onDismissRequest = { showOptions = false },
                                title = { Text("Edit ${product.productName}") },
                                text = { Text("What would you like to do?") },
                                confirmButton = {
                                    TextButton(onClick = {
                                        showOptions = false
                                        showDiscountDialog = true
                                    }) {
                                        Text("Change Discount")
                                    }
                                },
                                dismissButton = {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ){
                                        TextButton(onClick = { showOptions = false }) {
                                            Text("Cancel")
                                        }
                                        TextButton(onClick = {
                                            showOptions = false
                                            showPriceDialog = true
                                        }) {
                                            Text("Change Price")
                                        }
                                    }
                                }
                            )
                        }

                        if (showDiscountDialog) {
                            var discount by remember { mutableStateOf(product.discount.toString()) }

                            AlertDialog(
                                onDismissRequest = { showDiscountDialog = false },
                                title = { Text("Set Discount") },
                                text = {
                                    OutlinedTextField(
                                        value = discount,
                                        onValueChange = { discount = it },
                                        label = { Text("Discount %") },
                                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                        singleLine = true
                                    )
                                },
                                confirmButton = {
                                    TextButton(onClick = {
                                        viewModel.editProductInPurchase(
                                            EditPurchaseProductDTO(
                                                id = product.productId!!,
                                                purchaseId = purchaseId,
                                                quantity = product.quantity,
                                                discount = discount.toIntOrNull() ?: product.discount,
                                                price = product.price
                                            )
                                        )
                                        showDiscountDialog = false
                                    }) { Text("Apply") }
                                }
                            )
                        }

                        if (showPriceDialog) {
                            var price by remember { mutableStateOf(product.price.toString()) }

                            AlertDialog(
                                onDismissRequest = { showPriceDialog = false },
                                title = { Text("Set Price") },
                                text = {
                                    OutlinedTextField(
                                        value = price,
                                        onValueChange = { price = it },
                                        label = { Text("Price") },
                                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                        singleLine = true
                                    )
                                },
                                confirmButton = {
                                    TextButton(onClick = {
                                        viewModel.editProductInPurchase(
                                            EditPurchaseProductDTO(
                                                id = product.productId!!,
                                                purchaseId = purchaseId,
                                                quantity = product.quantity,
                                                discount = product.discount,
                                                price = price.toDoubleOrNull() ?: product.price
                                            )
                                        )
                                        showPriceDialog = false
                                    }) { Text("Apply") }
                                }
                            )
                        }
                    }
                }
            }
        }
        FloatingActionButtonMenu(
            expanded = fabMenuExpanded,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(padding)
            ,
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
            FloatingActionButtonMenuItem(// Add manually
                onClick = {
                    fabMenuExpanded = false
                    navController.navigate("${Routes.ADD_PRODUCT}/${purchaseState?.purchaseId}/${purchaseState?.storeId}")
                },
                icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                text = { Text("Add Barcodeless Product") }
            )
            FloatingActionButtonMenuItem(// Scan
                onClick = {
                    fabMenuExpanded = false
                    navController.navigate(Routes.SCANNER)
                },
                icon = { Icon(CustomIcons.BarcodeScanner, contentDescription = null) },
                text = { Text("Scan Product") }
            )
            FloatingActionButtonMenuItem(// Finish shopping
                onClick = {
                    fabMenuExpanded = false
                    viewModel.finishPurchase(purchaseState?.purchaseId ?: return@FloatingActionButtonMenuItem)
                    viewModel.clearShoppingList()  // Clear the purchase so UI resets
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SHOPPING) { inclusive = true }
                    }
                },
                icon = { Icon(Icons.Default.ShoppingCartCheckout, contentDescription = null) },
                text = { Text("Finish Shopping") }
            )
        }
    }
}
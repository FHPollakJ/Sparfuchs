package com.example.sparfuchsapp.ui.screens.shopping

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sparfuchsapp.data.dataClasses.StoreRepository
import com.example.sparfuchsapp.data.remote.dto.EditPurchaseProductDTO
import com.example.sparfuchsapp.ui.components.ProductCard
import com.example.sparfuchsapp.ui.components.ShoppingSummaryCard
import com.example.sparfuchsapp.ui.icons.CustomIcons
import com.example.sparfuchsapp.ui.theme.moneySavedLight
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
    val store = StoreRepository.getStore(purchaseState!!.storeId)
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

                        ProductCard(
                            product = product,
                            onAmountChange = { newQty ->
                                viewModel.editProductInPurchase(
                                    EditPurchaseProductDTO(
                                        id = productResponse.id,
                                        purchaseId = purchaseId,
                                        quantity = newQty,
                                        discount = productResponse.discount,
                                    )
                                )
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
                icon = { Icon(CustomIcons.FinishShopping, contentDescription = null) },
                text = { Text("Finish Shopping") }
            )
        }
    }
}
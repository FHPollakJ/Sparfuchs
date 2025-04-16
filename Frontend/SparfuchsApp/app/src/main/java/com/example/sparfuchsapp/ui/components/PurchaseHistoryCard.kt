package com.example.sparfuchsapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sparfuchsapp.data.dataClasses.Store
import com.example.sparfuchsapp.data.remote.dto.PurchaseDTO
import com.example.sparfuchsapp.ui.theme.SavingsGreen
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun PurchaseHistoryCard(purchase: PurchaseDTO,store: Store) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(store.name)
                Text("€ ${purchase.totalSpent}")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text("${purchase.lastUpdated.format(DateTimeFormatter.ofPattern("EE, dd.MM.yyyy"))}")
                Text(
                    text = "Savings € ${purchase.totalSaved}",
                    color = SavingsGreen
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PurchaseHistoryCardPreview(){
    val examplePurchase = PurchaseDTO(
        purchaseId = 1,
        storeId = 1,
        lastUpdated = LocalDateTime.parse("2024-05-10T14:30:00"),
        products = emptyList(),
        isCompleted = true,
        totalSpent = 50.0,
        totalSaved = 10.0
    )
    val exampleStore = listOf<Store>(
        Store(id = 1,
        name = "Billa"),
        Store(id = 2,
        name = "Spar")
    )

    val store = exampleStore.find {it.id == examplePurchase.storeId}?: Store(0, "Unknown")

    PurchaseHistoryCard(
        purchase = examplePurchase,
        store = store
    )

}
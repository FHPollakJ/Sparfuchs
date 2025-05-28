package com.example.sparfuchsapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sparfuchsapp.data.dataClasses.StoreRepository
import com.example.sparfuchsapp.data.remote.dto.PurchaseDTO
import com.example.sparfuchsapp.ui.theme.moneySavedLight
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun PurchaseHistory(
    purchaseList: List<PurchaseDTO>
) {
    val purchasesByMonth = purchaseList.
        groupBy { it.lastUpdated.withDayOfMonth(1).toLocalDate() }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        purchasesByMonth
            .toSortedMap(compareByDescending { it }) // Sort by latest month first
            .forEach { (monthStart, purchasesInMonth) ->

                val monthLabel = monthStart.format(DateTimeFormatter.ofPattern("MMMM"))
                val totalSavedByMonth = purchasesInMonth.sumOf { it.totalSaved }

                item {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(
                            text = monthLabel,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Total Savings: € %.2f".format(totalSavedByMonth),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            purchasesInMonth.forEachIndexed { index, purchase ->
                                val store = StoreRepository.getStore(purchase.storeId)

                                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = store.name,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text("€ %.2f".format(purchase.totalSpent))
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                            contentDescription = "Arrow",
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            purchase.lastUpdated.format(
                                                DateTimeFormatter.ofPattern("EE, dd.MM.yyyy")
                                            )
                                        )

                                        Text(
                                            text = "Savings € %.2f".format(purchase.totalSaved),
                                            color = moneySavedLight
                                        )
                                    }
                                }

                                if (index != purchasesInMonth.lastIndex) {
                                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                                }
                            }
                        }
                    }
                }
            }
    }
}

@Preview(showBackground = true)
@Composable
fun PurchaseHistoryPreview() {
    val examplePurchases = listOf(
        PurchaseDTO(
            purchaseId = 1,
            storeId = 1,
            lastUpdated = LocalDateTime.parse("2024-05-10T14:30:00"),
            products = emptyList(),
            isCompleted = true,
            totalSpent = 42.0,
            totalSaved = 5.0
        ),
        PurchaseDTO(
            purchaseId = 2,
            storeId = 2,
            lastUpdated = LocalDateTime.parse("2024-05-18T17:10:00"),
            products = emptyList(),
            isCompleted = true,
            totalSpent = 88.0,
            totalSaved = 12.0
        ),
        PurchaseDTO(
            purchaseId = 3,
            storeId = 1,
            lastUpdated = LocalDateTime.parse("2024-04-03T09:45:00"),
            products = emptyList(),
            isCompleted = true,
            totalSpent = 30.0,
            totalSaved = 3.0
        ),
        PurchaseDTO(
            purchaseId = 4,
            storeId = 2,
            lastUpdated = LocalDateTime.parse("2024-04-21T12:20:00"),
            products = emptyList(),
            isCompleted = false,
            totalSpent = 55.0,
            totalSaved = 6.0
        ),
        PurchaseDTO(
            purchaseId = 5,
            storeId = 1,
            lastUpdated = LocalDateTime.parse("2024-04-28T18:00:00"),
            products = emptyList(),
            isCompleted = true,
            totalSpent = 20.0,
            totalSaved = 2.5
        )
    )

    PurchaseHistory(
        purchaseList = examplePurchases
    )
}
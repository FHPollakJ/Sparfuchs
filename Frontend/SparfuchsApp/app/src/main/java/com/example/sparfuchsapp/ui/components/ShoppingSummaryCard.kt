package com.example.sparfuchsapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sparfuchsapp.data.dataClasses.StoreRepository
import com.example.sparfuchsapp.ui.theme.moneySavedLight


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ShoppingSummaryCard(
    storeID: Long,
    totalSpent: Double,
    totalSaved: Double,
    modifier: Modifier = Modifier
) {
    val storeName = StoreRepository.getStore(storeID).name
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Shopping at $storeName",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Total Spent",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "€ %.2f".format(totalSpent),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Total Saved",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "€ %.2f".format(totalSaved),
                        color = moneySavedLight,
                        style = MaterialTheme.typography.headlineSmallEmphasized.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ShoppingSummaryCardPreview() {
    ShoppingSummaryCard(storeID = 1, totalSpent = 100.0, totalSaved = 50.0)
}
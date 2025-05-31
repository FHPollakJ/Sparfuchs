package com.example.sparfuchsapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sparfuchsapp.data.remote.dto.ProductPriceHistoryDTO
import com.example.sparfuchsapp.ui.theme.moneySavedLight
import com.himanshoe.charty.common.ChartColor
import com.himanshoe.charty.line.LineChart
import com.himanshoe.charty.line.config.LineChartColorConfig
import com.himanshoe.charty.line.config.LineChartConfig
import com.himanshoe.charty.line.model.LineData
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun StorePriceHistoryChart(storeName: String, history: List<ProductPriceHistoryDTO>) {
    val formatter = DateTimeFormatter.ofPattern("MMM")
    val averagedByMonth = history
        .groupBy { YearMonth.from(it.endTime) }
        .map { (yearMonth, items) ->
            val avgPrice = items.map { it.price }.average().toFloat()
            yearMonth to avgPrice
        }
        .sortedBy { it.first }

    val data = averagedByMonth.map { (date, avgPrice) ->
        LineData(xValue = date.format(formatter), yValue = avgPrice)
    }

    if (data.size >= 2) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(storeName, Modifier.padding(bottom = 8.dp))

            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                data = { data },
                colorConfig = LineChartColorConfig(
                    lineColor = ChartColor.Solid(moneySavedLight),
                    axisColor = ChartColor.Solid(Color.Gray),
                    gridLineColor = ChartColor.Solid(Color.LightGray),
                    selectionBarColor = ChartColor.Solid((Color.Transparent)),
                ),
                showFilledArea = true,
                smoothLineCurve = false,
                chartConfig = LineChartConfig(
                    drawPointerCircle = true
                )
            )
        }
    } else {
        Text("$storeName: \n Not enough data for graph")
    }
}

@Composable
fun LineChartPreview() {
    val dummyData = listOf(
        LineData(xValue = 1f, yValue = 2f),
        LineData(xValue = 2f, yValue = 4f),
        LineData(xValue = 3f, yValue = 3f),
        LineData(xValue = 4f, yValue = 5f),
    )

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        data = { dummyData },
        colorConfig = LineChartColorConfig(
            lineColor = ChartColor.Solid(moneySavedLight),
            axisColor = ChartColor.Solid(Color.Gray),
            gridLineColor = ChartColor.Solid(Color.LightGray),
            selectionBarColor = ChartColor.Solid((Color.Gray)),
        ),
        smoothLineCurve = false,
        chartConfig = LineChartConfig(
            drawPointerCircle = true
        )
    )
}

@Preview(showBackground = true)
@Composable
fun LineChartPreviewWrapper() {
    LineChartPreview()
}
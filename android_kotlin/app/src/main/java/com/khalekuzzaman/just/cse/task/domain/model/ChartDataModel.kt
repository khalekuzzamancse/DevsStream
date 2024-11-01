package com.khalekuzzaman.just.cse.task.domain.model

data class ChartDataModel(
    val xAxisData: List<String>,
    val yAxisData: List<Int>
)

data class ChartModel(
    val data: Map<String, ChartDataModel>,
    val timePeriods: List<String>,
    val selectedTimePeriod: String
)

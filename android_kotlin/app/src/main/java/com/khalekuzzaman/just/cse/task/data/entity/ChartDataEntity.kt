package com.khalekuzzaman.just.cse.task.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChartDataEntity(
    @SerialName("xAxisData") val xAxisData: List<String>,
    @SerialName("yAxisData") val yAxisData: List<Int>
)

@Serializable
data class ChartEntity(
    @SerialName("data") val data: Map<String, ChartDataEntity>,
    @SerialName("timePeriods") val timePeriods: List<String>,
    @SerialName("selectedTimePeriod") val selectedTimePeriod: String
)

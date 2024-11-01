package com.khalekuzzaman.just.cse.task.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleEntity(
    @SerialName("1st_schedule") val firstSchedule: Double,
    @SerialName("2nd_schedule") val secondSchedule: Double,
    @SerialName("3rd_schedule") val thirdSchedule: Double,
    @SerialName("4th_schedule") val fourthSchedule: Double,
    @SerialName("5th_schedule") val fifthSchedule: Double
)

@Serializable
data class SpendEntity(
    val data: List<ScheduleEntity>
)

@Serializable
data class SpendReportEntity(
    val period: String,
    val currency: String,
    val spend: SpendEntity
)

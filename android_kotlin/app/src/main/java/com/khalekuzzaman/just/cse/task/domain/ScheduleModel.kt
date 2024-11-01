package com.khalekuzzaman.just.cse.task.domain

data class ScheduleModel(
    val firstSchedule: Double,
    val secondSchedule: Double,
    val thirdSchedule: Double,
    val fourthSchedule: Double,
    val fifthSchedule: Double
)

data class SpendModel(
    val schedules: List<ScheduleModel>
)

data class SpendReportModel(
    val period: String,
    val currency: String,
    val spend: SpendModel
)

package com.khalekuzzaman.just.cse.task.data.entity

import com.khalekuzzaman.just.cse.task.domain.ScheduleModel
import com.khalekuzzaman.just.cse.task.domain.SpendModel
import com.khalekuzzaman.just.cse.task.domain.SpendReportModel

object SpendMapper {
    fun mapEntityToModel(entity: SpendReportEntity): SpendReportModel {
        val schedules = entity.spend.data.map {
            ScheduleModel(
                firstSchedule = it.firstSchedule,
                secondSchedule = it.secondSchedule,
                thirdSchedule = it.thirdSchedule,
                fourthSchedule = it.fourthSchedule,
                fifthSchedule = it.fifthSchedule
            )
        }
        return SpendReportModel(
            period = entity.period,
            currency = entity.currency,
            spend = SpendModel(schedules = schedules)
        )
    }
}
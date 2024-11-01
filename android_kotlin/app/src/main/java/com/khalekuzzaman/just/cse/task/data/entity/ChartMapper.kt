package com.khalekuzzaman.just.cse.task.data.entity

import com.khalekuzzaman.just.cse.task.domain.model.ChartDataModel
import com.khalekuzzaman.just.cse.task.domain.model.ChartModel


object ChartMapper {
    fun mapEntityToModel(entity: ChartEntity): ChartModel {
        val dataModel = entity.data.mapValues { (_, chartDataEntity) ->
            ChartDataModel(
                xAxisData = chartDataEntity.xAxisData,
                yAxisData = chartDataEntity.yAxisData
            )
        }

        return ChartModel(
            data = dataModel,
            timePeriods = entity.timePeriods,
            selectedTimePeriod = entity.selectedTimePeriod
        )
    }
}

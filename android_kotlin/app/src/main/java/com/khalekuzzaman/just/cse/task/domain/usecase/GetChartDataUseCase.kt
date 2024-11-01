package com.khalekuzzaman.just.cse.task.domain.usecase

import com.khalekuzzaman.just.cse.task.domain.model.ChartModel
import com.khalekuzzaman.just.cse.task.domain.repository.SearchRepository

class GetChartDataUseCase(private val repository: SearchRepository) {
    suspend fun execute(): ChartModel {
        return repository.getChartData()
    }
}
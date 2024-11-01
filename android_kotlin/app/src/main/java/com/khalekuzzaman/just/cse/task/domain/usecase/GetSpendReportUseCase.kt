package com.khalekuzzaman.just.cse.task.domain.usecase

import com.khalekuzzaman.just.cse.task.domain.SpendReportModel
import com.khalekuzzaman.just.cse.task.domain.repository.SpendRepository

class GetSpendReportUseCase(private val repository: SpendRepository) {
    suspend fun execute(): SpendReportModel {
        return repository.getSpendReport()
    }
}


package com.khalekuzzaman.just.cse.task.domain.usecase

import com.khalekuzzaman.just.cse.task.domain.model.BreakdownModel
import com.khalekuzzaman.just.cse.task.domain.repository.SpendRepository

class GetBreakdownDataUseCase(private val repository: SpendRepository) {
    suspend fun execute(): List<BreakdownModel> {
        return repository.getBreakdownData()
    }
}
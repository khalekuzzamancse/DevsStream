package com.khalekuzzaman.just.cse.task.domain.repository
import com.khalekuzzaman.just.cse.task.domain.SpendReportModel
import com.khalekuzzaman.just.cse.task.domain.model.BreakdownModel


interface SpendRepository {
    suspend fun getSpendReport(): SpendReportModel
    suspend fun getBreakdownData(): List<BreakdownModel>
}
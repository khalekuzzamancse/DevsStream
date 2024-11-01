package com.khalekuzzaman.just.cse.task.data.repository

import com.khalekuzzaman.just.cse.task.domain.usecase.GetBreakdownDataUseCase
import com.khalekuzzaman.just.cse.task.domain.usecase.GetSpendReportUseCase


// Repository implementation


// Use case class


// Example of creating a repository and use cases
suspend fun main() {
    val repository = SpendRepositoryImpl()
    val spendReportUseCase = GetSpendReportUseCase(repository)
    val breakdownDataUseCase = GetBreakdownDataUseCase(repository)

    // Fetch spend report
    val spendReport = spendReportUseCase.execute()
    println("Spend Report: $spendReport")

    // Fetch breakdown data
    val breakdownData = breakdownDataUseCase.execute()
    println("Breakdown Data: $breakdownData")
}

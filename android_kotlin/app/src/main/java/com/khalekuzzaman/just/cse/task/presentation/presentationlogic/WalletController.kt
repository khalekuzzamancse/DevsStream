package com.khalekuzzaman.just.cse.task.presentation.presentationlogic

import com.khalekuzzaman.just.cse.task.domain.SpendReportModel
import com.khalekuzzaman.just.cse.task.domain.model.BreakdownModel
import kotlinx.coroutines.flow.StateFlow

interface WalletController {
    val spendReport: StateFlow<SpendReportModel?>
    val breakdownData: StateFlow<List<BreakdownModel>>

    fun loadSpendReport()
    fun loadBreakdownData()
}

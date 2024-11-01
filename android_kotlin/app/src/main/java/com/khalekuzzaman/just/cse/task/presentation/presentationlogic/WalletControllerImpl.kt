package com.khalekuzzaman.just.cse.task.presentation.presentationlogic

import com.khalekuzzaman.just.cse.task.domain.SpendReportModel
import com.khalekuzzaman.just.cse.task.domain.model.BreakdownModel
import com.khalekuzzaman.just.cse.task.domain.usecase.GetBreakdownDataUseCase
import com.khalekuzzaman.just.cse.task.domain.usecase.GetSpendReportUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WalletControllerImpl(
    private val getSpendReportUseCase: GetSpendReportUseCase,
    private val getBreakdownDataUseCase: GetBreakdownDataUseCase
) : WalletController {

    private val _spendReport = MutableStateFlow<SpendReportModel?>(null)
    override val spendReport= _spendReport.asStateFlow()

    private val _breakdownData = MutableStateFlow<List<BreakdownModel>>(emptyList())
    override val breakdownData = _breakdownData.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        loadSpendReport()
        loadBreakdownData()
    }

    override fun loadSpendReport() {
        scope.launch {
            val report = getSpendReportUseCase.execute()
            _spendReport.value = report
        }
    }

    override fun loadBreakdownData() {
        scope.launch {
            val data = getBreakdownDataUseCase.execute()
            _breakdownData.value = data
        }
    }
}
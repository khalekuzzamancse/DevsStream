package com.khalekuzzaman.just.cse.task.presentation.presentationlogic

import com.khalekuzzaman.just.cse.task.domain.model.ChartModel
import com.khalekuzzaman.just.cse.task.domain.model.ProductModel
import com.khalekuzzaman.just.cse.task.domain.usecase.GetChartDataUseCase
import com.khalekuzzaman.just.cse.task.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchControllerImpl(
    private val getChartDataUseCase: GetChartDataUseCase,
    private val getProductsUseCase: GetProductsUseCase
) : SearchController {

    private val _chartData = MutableStateFlow<ChartModel?>(null)
    override val chartData: StateFlow<ChartModel?> = _chartData.asStateFlow()

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    override val products: StateFlow<List<ProductModel>> = _products.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        loadChartData()
        loadProducts()
    }

    override fun loadChartData() {
        scope.launch {
            val chartResult = getChartDataUseCase.execute()
            _chartData.value = chartResult
        }
    }

    override fun loadProducts() {
        scope.launch {
            val productResults = getProductsUseCase.execute()
            _products.value = productResults
        }
    }
}

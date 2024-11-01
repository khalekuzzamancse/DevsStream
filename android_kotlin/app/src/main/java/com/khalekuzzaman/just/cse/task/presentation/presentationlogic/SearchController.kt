package com.khalekuzzaman.just.cse.task.presentation.presentationlogic

import com.khalekuzzaman.just.cse.task.domain.model.ChartModel
import com.khalekuzzaman.just.cse.task.domain.model.ProductModel
import kotlinx.coroutines.flow.StateFlow

interface SearchController {
    val chartData: StateFlow<ChartModel?>
    val products: StateFlow<List<ProductModel>>

    fun loadChartData()
    fun loadProducts()
}

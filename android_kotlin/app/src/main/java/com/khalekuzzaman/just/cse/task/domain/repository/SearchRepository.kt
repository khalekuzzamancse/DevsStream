package com.khalekuzzaman.just.cse.task.domain.repository

import com.khalekuzzaman.just.cse.task.domain.model.ChartModel
import com.khalekuzzaman.just.cse.task.domain.model.ProductModel


interface SearchRepository {
    suspend fun getChartData(): ChartModel
    suspend fun getProducts(): List<ProductModel>
}

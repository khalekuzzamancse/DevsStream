package com.khalekuzzaman.just.cse.task.domain.usecase

import com.khalekuzzaman.just.cse.task.domain.model.ProductModel
import com.khalekuzzaman.just.cse.task.domain.repository.SearchRepository

class GetProductsUseCase(private val repository: SearchRepository) {
    suspend fun execute(): List<ProductModel> {
        return repository.getProducts()
    }
}
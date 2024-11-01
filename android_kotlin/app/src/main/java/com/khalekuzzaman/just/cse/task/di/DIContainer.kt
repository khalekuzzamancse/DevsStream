package com.khalekuzzaman.just.cse.task.di

import com.khalekuzzaman.just.cse.task.data.repository.SearchRepositoryImpl
import com.khalekuzzaman.just.cse.task.data.repository.SpendRepositoryImpl
import com.khalekuzzaman.just.cse.task.domain.repository.SearchRepository
import com.khalekuzzaman.just.cse.task.domain.repository.SpendRepository
import com.khalekuzzaman.just.cse.task.domain.usecase.GetBreakdownDataUseCase
import com.khalekuzzaman.just.cse.task.domain.usecase.GetChartDataUseCase
import com.khalekuzzaman.just.cse.task.domain.usecase.GetProductsUseCase
import com.khalekuzzaman.just.cse.task.domain.usecase.GetSpendReportUseCase


object DIContainer {
    private val repository: SpendRepository by lazy {
        SpendRepositoryImpl()
    }
    private val searchRepository: SearchRepository by lazy {
        SearchRepositoryImpl()
    }


    fun provideGetSpendReportUseCase(): GetSpendReportUseCase {
        return GetSpendReportUseCase(repository)
    }

    fun provideGetBreakdownDataUseCase(): GetBreakdownDataUseCase {
        return GetBreakdownDataUseCase(repository)
    }

    fun provideGetChartDataUseCase(): GetChartDataUseCase {
        return GetChartDataUseCase(searchRepository)
    }
    fun provideGetProductsUseCase(): GetProductsUseCase {
        return GetProductsUseCase(searchRepository)
    }
}

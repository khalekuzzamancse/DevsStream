package com.khalekuzzaman.just.cse.task.presentation.presentationlogic

import com.khalekuzzaman.just.cse.task.di.DIContainer

object PresentationFactory {
    fun provideHomeController(): WalletController {
        return WalletControllerImpl(
            getSpendReportUseCase = DIContainer.provideGetSpendReportUseCase(),
            getBreakdownDataUseCase = DIContainer.provideGetBreakdownDataUseCase()
        )
    }
    fun provideSearchController(): SearchController {
        return SearchControllerImpl(
            getChartDataUseCase = DIContainer.provideGetChartDataUseCase(),
            getProductsUseCase = DIContainer.provideGetProductsUseCase()
        )
    }
}
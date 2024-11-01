package com.khalekuzzaman.just.cse.task.data.repository

import com.khalekuzzaman.just.cse.task.data.entity.ScheduleEntity
import com.khalekuzzaman.just.cse.task.data.entity.SpendEntity
import com.khalekuzzaman.just.cse.task.data.entity.SpendMapper
import com.khalekuzzaman.just.cse.task.data.entity.SpendReportEntity
import com.khalekuzzaman.just.cse.task.domain.SpendReportModel
import com.khalekuzzaman.just.cse.task.domain.model.BreakdownModel
import com.khalekuzzaman.just.cse.task.domain.repository.SpendRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SpendRepositoryImpl : SpendRepository {
    private val demoBreakDownData = listOf(
        BreakdownModel(label = "Food & Drinks", percentage = "45%"),
        BreakdownModel(label = "Dresses", percentage = "25%"),
        BreakdownModel(label = "Transport", percentage = "20%"),
        BreakdownModel(label = "Others", percentage = "10%")
    )

    override suspend fun getSpendReport(): SpendReportModel {
        return withContext(Dispatchers.IO) {
            // Simulate fetching data from a data source
            val spendReportEntity = SpendReportEntity(
                period = "this_month",
                currency = "$",
                spend = SpendEntity(
                    data = listOf(
                        ScheduleEntity(
                            firstSchedule = 400.00,
                            secondSchedule = 600.00,
                            thirdSchedule = 1200.00,
                            fourthSchedule = 1800.00,
                            fifthSchedule = 2600.00
                        )
                    )
                )
            )
            SpendMapper.mapEntityToModel(spendReportEntity)
        }
    }

    override suspend fun getBreakdownData(): List<BreakdownModel> {
        return withContext(Dispatchers.IO) {
            demoBreakDownData
        }
    }
}
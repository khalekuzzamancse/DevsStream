package com.khalekuzzaman.just.cse.task.data.repository

import com.khalekuzzaman.just.cse.task.data.entity.SpendMapper
import com.khalekuzzaman.just.cse.task.data.entity.SpendReportEntity
import com.khalekuzzaman.just.cse.task.domain.SpendReportModel
import com.khalekuzzaman.just.cse.task.domain.model.BreakdownModel
import com.khalekuzzaman.just.cse.task.domain.repository.SpendRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class SpendRepositoryImpl : SpendRepository {
    private val demoBreakDownData = listOf(
        BreakdownModel(label = "Food & Drinks", percentage = "45%"),
        BreakdownModel(label = "Dresses", percentage = "25%"),
        BreakdownModel(label = "Transport", percentage = "20%"),
        BreakdownModel(label = "Others", percentage = "10%")
    )

    override suspend fun getSpendReport(): SpendReportModel {
        return withContext(Dispatchers.IO) {
            val spendReportEntity = Json.decodeFromString<SpendReportEntity>(spendReportJsonData)
            SpendMapper.mapEntityToModel(spendReportEntity)
        }
    }

    override suspend fun getBreakdownData(): List<BreakdownModel> {
        return withContext(Dispatchers.IO) {
            demoBreakDownData
        }
    }
}
const val spendReportJsonData = """
{
    "period": "this_month",
    "currency": "$",
    "spend": {
        "data": [
            {
                "1st_schedule": 400.00,
                "2nd_schedule": 600.00,
                "3rd_schedule": 1200.00,
                "4th_schedule": 1800.00,
                "5th_schedule": 2600.00
            }
        ]
    }
}
"""
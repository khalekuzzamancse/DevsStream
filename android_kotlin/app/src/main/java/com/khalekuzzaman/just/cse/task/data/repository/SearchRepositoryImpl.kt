package com.khalekuzzaman.just.cse.task.data.repository
import com.khalekuzzaman.just.cse.task.data.entity.ChartEntity
import com.khalekuzzaman.just.cse.task.data.entity.ChartMapper
import com.khalekuzzaman.just.cse.task.data.entity.ProductEntity
import com.khalekuzzaman.just.cse.task.data.entity.ProductMapper
import com.khalekuzzaman.just.cse.task.domain.model.ChartModel
import com.khalekuzzaman.just.cse.task.domain.model.ProductModel
import com.khalekuzzaman.just.cse.task.domain.repository.SearchRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.json.Json

class SearchRepositoryImpl : SearchRepository {
    override suspend fun getChartData(): ChartModel {
        // Parse the JSON data and map it to the model
        val chartEntity = parseChartJson()
        return ChartMapper.mapEntityToModel(chartEntity)
    }
    private val client = HttpClient()

    override suspend fun getProducts(): List<ProductModel> {
        val response: HttpResponse = client.get("https://fakestoreapi.com/products")
        val responseBody = response.body<String>()
        val productEntities: List<ProductEntity> = Json.decodeFromString(responseBody)
        return ProductMapper.mapEntityListToModelList(productEntities)
    }
}


fun parseChartJson(): ChartEntity {
    return Json.decodeFromString(chartJsonData)
}

const val chartJsonData = """
{
  "data": {
    "1W": {
      "xAxisData": ["MON", "TUE", "WED", "THU", "FRI", "SAT"],
      "yAxisData": [1000, 1200, 1500, 2200, 3500, 5000]
    },
    "1M": {
      "xAxisData": ["Week 1", "Week 2", "Week 3", "Week 4"],
      "yAxisData": [15000, 20000, 25000, 30000]
    },
    "3M": {
      "xAxisData": ["Jan", "Feb", "Mar"],
      "yAxisData": [45000, 50000, 60000]
    },
    "6M": {
      "xAxisData": ["Oct", "Nov", "Dec", "Jan", "Feb", "Mar"],
      "yAxisData": [70000, 75000, 80000, 85000, 90000, 95000]
    },
    "1Y": {
      "xAxisData": ["Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Jan", "Feb", "Mar"],
      "yAxisData": [100000, 105000, 110000, 120000, 130000, 140000, 150000, 155000, 160000, 165000, 170000, 175000]
    },
    "ALL": {
      "xAxisData": ["2020", "2021", "2022", "2023"],
      "yAxisData": [300000, 350000, 400000, 450000]
    }
  },
  "timePeriods": ["1W", "1M", "3M", "6M", "1Y", "ALL"],
  "selectedTimePeriod": "1W"
}
"""

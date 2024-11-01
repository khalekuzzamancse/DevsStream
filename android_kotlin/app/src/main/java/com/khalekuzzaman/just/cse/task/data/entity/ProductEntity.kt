package com.khalekuzzaman.just.cse.task.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductEntity(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("price") val price: Double,
    @SerialName("description") val description: String,
    @SerialName("category") val category: String,
    @SerialName("image") val image: String,
    @SerialName("rating") val rating: RatingEntity
)

@Serializable
data class RatingEntity(
    @SerialName("rate") val rate: Double,
    @SerialName("count") val count: Int
)

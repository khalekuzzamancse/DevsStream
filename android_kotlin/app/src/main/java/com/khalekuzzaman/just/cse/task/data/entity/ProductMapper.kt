package com.khalekuzzaman.just.cse.task.data.entity

import com.khalekuzzaman.just.cse.task.domain.model.ProductModel
import com.khalekuzzaman.just.cse.task.domain.model.RatingModel

object ProductMapper {
    fun mapEntityToModel(entity: ProductEntity): ProductModel {
        return ProductModel(
            id = entity.id,
            title = entity.title,
            price = entity.price,
            description = entity.description,
            category = entity.category,
            image = entity.image,
            rating = RatingModel(
                rate = entity.rating.rate,
                count = entity.rating.count
            )
        )
    }

    fun mapEntityListToModelList(entities: List<ProductEntity>): List<ProductModel> {
        return entities.map { mapEntityToModel(it) }
    }
}

package com.shopflow.app.data.mapper

import com.shopflow.app.FetchCollectionsQuery
import com.shopflow.app.domain.model.Collection
import com.shopflow.app.domain.model.ProductImage

fun FetchCollectionsQuery.Data.toDomainCollections(): List<Collection> {
    return collections.edges.map { edge ->
        val node = edge.node
        Collection(
            id = node.id,
            title = node.title,
            description = node.description,
            image = node.image?.let {
                ProductImage(
                    url = it.url.toString(),
                    altText = it.altText,
                    width = it.width,
                    height = it.height
                )
            },
            handle = node.handle
        )
    }
}

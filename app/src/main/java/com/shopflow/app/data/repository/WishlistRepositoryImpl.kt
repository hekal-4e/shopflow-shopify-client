package com.shopflow.app.data.repository

import com.shopflow.app.data.local.dao.WishlistDao
import com.shopflow.app.data.local.entity.WishlistItemEntity
import com.shopflow.app.domain.model.WishlistItem
import com.shopflow.app.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WishlistRepositoryImpl @Inject constructor(
    private val wishlistDao: WishlistDao
) : WishlistRepository {

    override fun getWishlist(): Flow<List<WishlistItem>> {
        return wishlistDao.getAll().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun addItem(item: WishlistItem) {
        wishlistDao.insert(item.toEntity())
    }

    override suspend fun removeItem(productId: String) {
        wishlistDao.deleteByProductId(productId)
    }

    override fun isWishlisted(productId: String): Flow<Boolean> {
        return wishlistDao.exists(productId)
    }

    override fun getCount(): Flow<Int> {
        return wishlistDao.getCount()
    }
}

private fun WishlistItemEntity.toDomainModel(): WishlistItem = WishlistItem(
    id = id,
    productId = productId,
    variantId = variantId,
    title = title,
    brand = brand,
    imageUrl = imageUrl,
    price = price,
    currency = currency,
    addedAt = addedAt
)

private fun WishlistItem.toEntity(): WishlistItemEntity = WishlistItemEntity(
    id = id,
    productId = productId,
    variantId = variantId,
    title = title,
    brand = brand,
    imageUrl = imageUrl,
    price = price,
    currency = currency,
    addedAt = addedAt
)

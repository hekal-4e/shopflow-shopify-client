package com.shopflow.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shopflow.app.data.local.entity.WishlistItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Query("SELECT * FROM wishlist_items ORDER BY addedAt DESC")
    fun getAll(): Flow<List<WishlistItemEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: WishlistItemEntity)

    @Query("DELETE FROM wishlist_items WHERE productId = :productId")
    suspend fun deleteByProductId(productId: String)

    @Query("SELECT COUNT(*) FROM wishlist_items")
    fun getCount(): Flow<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist_items WHERE productId = :productId)")
    fun exists(productId: String): Flow<Boolean>
}

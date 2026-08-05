package com.example.inventorymanagementapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface InventoryDao {

    // 追加
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: InventoryEntity)

    // 更新
    @Update
    suspend fun update(item: InventoryEntity)

    // 削除
    @Delete
    suspend fun delete(item: InventoryEntity)

    // 対象の取得
    @Query("SELECT * FROM inventory_items WHERE id = :id")
    suspend fun getItem(id: Int): InventoryEntity?

    // 全取得
    @Query("SELECT * FROM inventory_items")
    suspend fun getAllItems(): List<InventoryEntity>

    // 全削除
    @Query("DELETE FROM inventory_items")
    suspend fun deleteAll()
}
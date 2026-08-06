package com.example.inventorymanagementapp.database

// データベースに対して「何ができるか」だけを定義する
// 実際の処理はOfflineInventoryRepositoryが行う
interface InventoryRepository {

    suspend fun getAllItems(): List<InventoryEntity>

    suspend fun getItem(id: Int): InventoryEntity?

    suspend fun insertItem(item: InventoryEntity)

    suspend fun updateItem(item: InventoryEntity)

    suspend fun deleteItem(item: InventoryEntity)

    suspend fun deleteAllItems()
}
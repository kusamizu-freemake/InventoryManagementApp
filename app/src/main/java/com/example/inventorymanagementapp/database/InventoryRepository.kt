package com.example.inventorymanagementapp.database

import kotlinx.coroutines.flow.Flow

// データベースに対して「何ができるか」だけを定義
// 実際の処理はOfflineInventoryRepositoryが行う
interface InventoryRepository {

    // TODO:全件取得（フェーズ3 ⑥で実装予定）
    fun getAllItemsStream(): Flow<List<InventoryEntity>>

    suspend fun insertItem(item: InventoryEntity)

    suspend fun updateItem(item: InventoryEntity)

    suspend fun deleteItem(item: InventoryEntity)

    suspend fun deleteAllItems()
}
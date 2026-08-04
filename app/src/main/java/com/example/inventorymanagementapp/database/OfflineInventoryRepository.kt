package com.example.inventorymanagementapp.database

import kotlinx.coroutines.flow.Flow

// 実際にRoomのDAOを呼び出して処理する
class OfflineInventoryRepository(private val dao: InventoryDao) : InventoryRepository {

    override fun getAllItemsStream(): Flow<List<InventoryEntity>> = dao.getAllItems()

    override suspend fun insertItem(item: InventoryEntity) = dao.insert(item)

    override suspend fun updateItem(item: InventoryEntity) = dao.update(item)

    override suspend fun deleteItem(item: InventoryEntity) = dao.delete(item)

    override suspend fun deleteAllItems() = dao.deleteAll()
}
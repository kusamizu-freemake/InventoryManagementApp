package com.example.inventorymanagementapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventory_items")
data class InventoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val time: String,       // 時刻
    val quantity: Int,      // 数量
    val comment: String,    // コメント
    val isChecked: Boolean, // チェック状態
    val imageUri: String?   // 画像の場所(URI)。まだ選んでいない場合はnull
)
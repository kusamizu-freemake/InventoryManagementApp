package com.example.inventorymanagementapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorymanagementapp.database.InventoryDatabase
import com.example.inventorymanagementapp.database.InventoryEntity
import com.example.inventorymanagementapp.database.InventoryRepository
import com.example.inventorymanagementapp.database.OfflineInventoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 一覧画面(list)用のViewModel
class InventoryListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InventoryRepository

    // 一覧データ
    private val _inventoryList = MutableStateFlow<List<InventoryEntity>>(emptyList())
    val inventoryList: StateFlow<List<InventoryEntity>> = _inventoryList.asStateFlow()

    // 合計数量ダイアログの表示状態
    private val _showTotalDialog = MutableStateFlow(false)
    val showTotalDialog: StateFlow<Boolean> = _showTotalDialog.asStateFlow()

    // 合計数量
    private val _totalQuantity = MutableStateFlow(0)
    val totalQuantity: StateFlow<Int> = _totalQuantity.asStateFlow()

    // すべてのプロパティを初期化し終えた後に実行される
    init {
        val dao = InventoryDatabase.getDatabase(application).inventoryDao()
        repository = OfflineInventoryRepository(dao)
        // ⑥ データ読込
        refresh() // 起動時に1回だけDBから読み込む
    }

    // DBから最新の一覧を取り直して画面用の状態に反映する
    private fun refresh() {
        viewModelScope.launch {
            _inventoryList.value = repository.getAllItems()
        }
    }

    // ⑤ データ追加機能
    // 追加ボタンが押されたときの処理
    fun addItem(item: InventoryEntity) {
        viewModelScope.launch {
            repository.insertItem(item)
            refresh()
        }
    }

    // ⑨ 更新
    // チェックボックスが押されたときの処理
    fun toggleChecked(index: Int) {
        val items = _inventoryList.value
        if (index !in items.indices) return
        val updated = items[index].copy(isChecked = !items[index].isChecked)

        viewModelScope.launch {
            repository.updateItem(updated)
            refresh()
        }
    }

    // ⑦ 削除
    // 削除ボタンが押されたときの処理
    fun deleteItem(index: Int) {
        val items = _inventoryList.value
        if (index !in items.indices) return

        viewModelScope.launch {
            repository.deleteItem(items[index])
            refresh()
        }
    }

    // ⑧ 全削除
    // クリアボタンが押されたときの処理
    fun clearAll() {
        viewModelScope.launch {
            repository.deleteAllItems()
            refresh()
        }
    }

    // 合計数量ボタンが押されたときの処理
    fun calculateTotal() {
        _totalQuantity.value = _inventoryList.value
            .filter { it.isChecked }
            .sumOf { it.quantity }
        _showTotalDialog.value = true
    }

    // ダイアログを閉じる処理
    fun dismissTotalDialog() {
        _showTotalDialog.value = false
    }

    // ⑩ 画像情報保存
    // 詳細画面で画像が選択されたときの処理
    fun updateImageUri(index: Int, newImageUri: String) {
        val items = _inventoryList.value
        if (index !in items.indices) return
        val updated = items[index].copy(imageUri = newImageUri)

        viewModelScope.launch {
            repository.updateItem(updated)
            refresh()
        }
    }
}
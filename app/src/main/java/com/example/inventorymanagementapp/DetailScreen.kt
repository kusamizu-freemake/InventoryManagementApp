package com.example.inventorymanagementapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inventorymanagementapp.ui.theme.InventoryManagementAppTheme

// 詳細画面
@Composable
fun DetailScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // TODO:仮タイトル表示。ここに時刻・数量・コメントが今後並ぶ予定(③のステップで対応)
        Text("詳細画面")
    }
}

// 詳細画面側のプレビュー
@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    InventoryManagementAppTheme {
        DetailScreen()
    }
}
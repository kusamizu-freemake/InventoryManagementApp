package com.example.inventorymanagementapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inventorymanagementapp.ui.theme.InventoryManagementAppTheme

// 詳細画面
// 一覧画面でタップされた行のデータ(時刻・数量・コメント)を受け取って表示するだけの画面
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    time: String, // 渡された時刻
    quantity: Int, // 渡された数量
    comment: String // 渡されたコメント
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("詳細画面")

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        // 時刻の行(ラベルと値を左右に並べる)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(R.string.label_time))
            Text(time)
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        // 数量の行
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(R.string.label_quantity))
            Text(quantity.toString())
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        // コメントの行(内容が長くなる可能性もあるのでラベルを上、内容を下に並べる)
        Text(stringResource(R.string.label_comment))
        Text(comment)
    }
}

// 詳細画面側のプレビュー
@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    InventoryManagementAppTheme {
        DetailScreen(
            time = "12:34:56",
            quantity = 5,
            comment = "サンプルコメント"
        )
    }
}
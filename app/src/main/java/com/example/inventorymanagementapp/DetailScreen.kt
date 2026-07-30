package com.example.inventorymanagementapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    comment: String, // 渡されたコメント
    imageUri: String? = null // 選択された画像の場所(URI)。初期値はnull(画像なし)
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

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        // 画像表示エリア(今回は「画像なし」)
        ImageArea(imageUri = imageUri)
    }
}

// 画像表示エリア
// imageUriがnullの間は「画像なし」の枠だけを表示する
@Composable
fun ImageArea(
    modifier: Modifier = Modifier,
    imageUri: String? // 画像の場所
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.LightGray) // debug用
            .border(1.dp, Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        if (imageUri == null) {
            // 画像が選ばれていない場合は「画像なし」と表示する
            Text(stringResource(R.string.message_no_image))
        } else {
            // TODO: 実際の画像(Image composable)を表示する処理
            Text(stringResource(R.string.message_no_image))
        }
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
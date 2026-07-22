package com.example.inventorymanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.HorizontalDivider
import java.time.LocalTime
import com.example.inventorymanagementapp.ui.theme.InventoryManagementAppTheme
import java.time.format.DateTimeFormatter
import androidx.compose.ui.res.stringResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InventoryManagementAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InventoryEntryArea(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// 画面作成
// 在庫入力エリア（数量表示、変更ボタン、現在時刻表示、コメント入力、追加ボタン）
@Composable
fun InventoryEntryArea (modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Cyan) // debug用
            .padding(16.dp)
    ) {
        // 在庫入力エリア:上部に配置
        InputArea(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2.0f)
                .padding(16.dp)
        )

        // 在庫一覧エリア:中央に配置
        ListArea(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
                .padding(16.dp)
        )

        // フッターエリア:下部に配置
        FooterArea(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
                .padding(16.dp)
        )
    }
}

// 各エリア処理事項
// 在庫入力エリア(上部)
@Composable
fun InputArea(modifier: Modifier) {
    // 数量の状態
    var quantity by remember { mutableStateOf(0) }
    // コメントの状態
    var comment by remember { mutableStateOf("") }

    // 時刻の状態
    var currentTimeText by remember { mutableStateOf(getCurrentTimeText()) }

    Column(
        modifier = modifier
            .background(Color.LightGray) // debug用
            .padding(16.dp)
    ) {
        // 数量の行
        // ラベルと、数量表示,"−","＋"ボタンを横一列に並べる
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically, // 上下中央に揃える
            horizontalArrangement = Arrangement.SpaceBetween // 左右に振り分ける
        ) {
            Text(stringResource(R.string.label_quantity))

            // 数量表示"−","＋"をまとめて右側に置くための内側のRow
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 数量の表示
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(quantity.toString())
                }

                // −ボタン：数量が0より大きい場合のみ減少
                Button(onClick = {
                    if (quantity > 0) {
                        quantity--
                    }
                }) {
                    Text(stringResource(R.string.button_minus))
                }

                // ＋ボタン：数量を増加
                Button(onClick = {
                    quantity++
                }) {
                    Text(stringResource(R.string.button_plus))
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        // 時刻の行
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(R.string.label_time))
            Text(currentTimeText) // 時刻（"hh:mm:ss"形式）
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        // コメントの行
        // ラベルを上、入力欄を下に縦に並べる
        Text(stringResource(R.string.label_comment))

        TextField(
            value = comment,
            onValueChange = { newComment -> comment = newComment },
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        // 追加ボタン
        // 中央に配置するため、Rowで囲んでArrangement.Centerを使う
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {}
            ) {
                Text(stringResource(R.string.button_add))
            }
        }
    }
}

// 在庫一覧エリア(中央)
@Composable
fun ListArea(modifier: Modifier) {
    //
    Box(
        modifier = modifier
            .background(Color.White) // debug用
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(R.string.area_inventory_list))
    }
}

// フッターエリア(下部)
@Composable
fun FooterArea(modifier: Modifier) {
    //
    Box(
        modifier = modifier
            .background(Color.Yellow) // debug用
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(R.string.area_footer))
    }
}


// 現在時刻を "hh:mm:ss" 形式で文字列にして返す関数
private fun getCurrentTimeText(): String {
    val now = LocalTime.now()
    val formater = DateTimeFormatter.ofPattern("HH:mm:ss")
    return now.format(formater)
}

@Preview(showBackground = true)
@Composable
fun InventoryScreenPreview() {
    InventoryManagementAppTheme {
        InventoryEntryArea()
    }
}

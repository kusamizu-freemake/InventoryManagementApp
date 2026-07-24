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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.time.LocalTime
import com.example.inventorymanagementapp.ui.theme.InventoryManagementAppTheme
import java.time.format.DateTimeFormatter
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

// 在庫データ
data class InventoryItem(
    val time: String, // 時刻
    val quantity: Int, // 数量
    val comment: String, // コメント
    val isChecked: Boolean = false // チェック状態
)

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

    val inventoryList = remember { mutableStateListOf<InventoryItem>() }

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
                .padding(16.dp),
            onAddItem = { newItem -> inventoryList.add(newItem) } // 追加ボタンが押されたら、その内容をリストへ追加する
        )

        // 在庫一覧エリア:中央に配置
        ListArea(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
                .padding(16.dp),
            items = inventoryList,
            // チェックボックスが押されたら、toggleChecked()で該当データのチェック状態を反転させる
            onToggleCheck = { index -> toggleChecked(inventoryList, index) }
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
fun InputArea(
    modifier: Modifier,
    onAddItem: (InventoryItem) -> Unit // 追加ボタンが押されたことを親に伝えるための連絡係
) {
    // 数量の状態
    var quantity by remember { mutableStateOf(0) }
    // コメントの状態
    var comment by remember { mutableStateOf("") }

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

        // 時刻表示
        CurrentTimer()

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
            Button(onClick = {
                //
                val newItem = InventoryItem(
                    time = getCurrentTimeText(), // 時刻取得
                    quantity = quantity,
                    comment = comment,
                    isChecked = false
                )
                // 作ったデータを親(InventoryEntryArea)へ渡して、一覧に追加してもらう
                onAddItem(newItem)
            }
            ) {
                Text(stringResource(R.string.button_add))
            }
        }
    }
}

// 時刻表示（毎秒更新）
@Composable
fun CurrentTimer() {
    var currentTimeText by remember { mutableStateOf(getCurrentTimeText()) }

    // 表示用の時刻を1秒ごとに更新
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)  // 1秒待つ
            currentTimeText = getCurrentTimeText()
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(stringResource(R.string.label_time))
        Text(currentTimeText) // 時刻（"hh:mm:ss"形式）
    }
}

// 在庫一覧エリア(中央)
@Composable
fun ListArea(
    modifier: Modifier,
    items: List<InventoryItem>,
    onToggleCheck: (Int) -> Unit
) {
    //
    Box(
        modifier = modifier
            .background(Color.White) // debug用
            .padding(16.dp)
    ) {
        if (items.isEmpty()) {
            // まだ何も追加されていない時の表示
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.message_empty_list))
            }
        } else {
            // LazyColumn: 画面に入りきらない分は自動でスクロールできるリスト
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // itemsIndexed: 中身を1件ずつ、その「何番目か(index)」と一緒に取り出す
                // チェックボックスがどのデータのものかを親に伝えるためにindexが必要
                itemsIndexed(items) { index, item ->
                    InventoryRow(
                        item = item,
                        index = index, // 背景色の切り替えに使う行番号
                        onCheckedChange = { onToggleCheck(index) },
                        onDeleteClick = {
                            // 「この行だけ削除する」処理を後で実装予定
                        }
                    )
                }
            }
        }
    }
}

// 一覧の1行分のデザイン
@Composable
fun InventoryRow(
    item: InventoryItem,
    index: Int, // 背景色の判定に使う
    onCheckedChange: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // 行背景色を決める
    val rowColor = if (item.isChecked) {
        Color.Green // チェックが入っていたら緑(0xFF4CAF50)
    } else if (index % 2 == 0) {
        Color(0xFF82B1FF) // Color.Blueは見えづらいので不採用
    } else {
        Color.White
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(rowColor)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // チェックボックス：押すとisCheckedが反転する
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = { onCheckedChange() }
        )

        Text(item.time)
        Text(item.quantity.toString())
        Text(item.comment)


        // 削除ボタン：配置だけして、処理は後で実装予定
        Button(onClick = onDeleteClick) {
            Text(stringResource(R.string.button_delete))
        }
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

// 一覧の中の、指定した位置(index)のデータだけチェック状態を反転させる関数
private fun toggleChecked(list: SnapshotStateList<InventoryItem>, index: Int) {
    // 対象のデータを取り出す
    val item = list[index]

    // チェック状態を反転させたコピーを作る
    val newItem = item.copy(isChecked = !item.isChecked)

    // 一覧に入れ替える
    list[index] = newItem
}

@Preview(showBackground = true)
@Composable
fun InventoryScreenPreview() {
    InventoryManagementAppTheme {
        InventoryEntryArea()
    }
}
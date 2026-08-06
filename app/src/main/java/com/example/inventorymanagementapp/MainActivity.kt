package com.example.inventorymanagementapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.inventorymanagementapp.ui.theme.InventoryManagementAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InventoryManagementAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 画面遷移をまとめて管理するAppNavHostを呼び出す
                    AppNavHost(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// 画面遷移をまとめて管理する場所
@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    // navController: 今どの画面にいるか、次にどこへ移動するかを管理する案内係
    val navController = rememberNavController()

    val listViewModel: InventoryListViewModel = viewModel()

    val inventoryList by listViewModel.inventoryList.collectAsState()
    val showTotalDialog by listViewModel.showTotalDialog.collectAsState()
    val totalQuantity by listViewModel.totalQuantity.collectAsState()

    // NavHost: 住所(文字列)と画面(Composable)を紐づけて登録する箱
    // startDestination: アプリを開いたときに最初に表示する住所
    NavHost(
        navController = navController,
        startDestination = "list",
        modifier = modifier
    ) {
        // "list"という住所には一覧画面を表示する
        composable("list") {
            InventoryEntryArea(
                inventoryList = inventoryList, // 一覧データを渡す
                showTotalDialog = showTotalDialog,
                totalQuantity = totalQuantity,
                onAddItem = listViewModel::addItem,
                onToggleCheck = listViewModel::toggleChecked,
                onDeleteItem = listViewModel::deleteItem,
                onClickClear = listViewModel::clearAll,
                onClickTotal = listViewModel::calculateTotal,
                onDismissDialog = listViewModel::dismissTotalDialog,
                // タップされた行の「index(何番目か)」と「データ」を受け取り、詳細画面へ渡す
                onItemClick = { index, item ->
                    // コメントを安全に渡せるよう文字列を変換する
                    val encodedComment = Uri.encode(item.comment)
                    // 画像のURIも同じように安全な文字列に変換する
                    val encodedImageUri = Uri.encode(item.imageUri ?: "")

                    // 詳細画面へデータを渡して画面遷移する
                    navController.navigate(
                        "detail/$index/${item.time}/${item.quantity}/$encodedComment/$encodedImageUri"
                    )
                }
            )
        }

        // 詳細画面が受け取るデータ(index・時刻・数量・コメント・画像URI)を定義する
        composable(
            route = "detail/{index}/{time}/{quantity}/{comment}/{imageUri}",
            arguments = listOf(
                navArgument("index") { type = NavType.IntType },
                navArgument("time") { type = NavType.StringType },
                navArgument("quantity") { type = NavType.IntType },
                navArgument("comment") { type = NavType.StringType },
                navArgument("imageUri") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // 一覧画面から渡されたデータを取り出す
            val index = backStackEntry.arguments?.getInt("index") ?: 0
            val time = backStackEntry.arguments?.getString("time") ?: ""
            val quantity = backStackEntry.arguments?.getInt("quantity") ?: 0
            val encodedComment = backStackEntry.arguments?.getString("comment") ?: ""
            val encodedImageUri = backStackEntry.arguments?.getString("imageUri") ?: ""

            // エンコードしたコメントを元の文字列へ戻す
            val comment = Uri.decode(encodedComment)
            // エンコードした画像URIを元の文字列へ戻す
            val decodedImageUri = Uri.decode(encodedImageUri)
            val imageUri = if (decodedImageUri.isEmpty()) null else decodedImageUri

            DetailScreen(
                time = time,
                quantity = quantity,
                comment = comment,
                imageUri = imageUri,
                // 詳細画面で新しい画像が選ばれたときに呼ばれる処理
                onImageSelected = { newImageUri ->
                    listViewModel.updateImageUri(index, newImageUri)
                }
            )
        }
    }
}
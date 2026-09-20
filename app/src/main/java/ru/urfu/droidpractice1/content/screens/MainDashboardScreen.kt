package ru.urfu.droidpractice1.content.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.urfu.droidpractice1.R
import ru.urfu.droidpractice1.content.Screen
import ru.urfu.droidpractice1.content.components.ArticlePreviewCard

/**
 * Главный экран приложения со списком доступных статей.
 * 
 * @param onNavigate Функция обратного вызова для перехода на выбранный экран.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboardScreen(onNavigate: (Screen) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                )
            )
        }
    ) { innerPadding ->
        val context = LocalContext.current
        
        // Обработка системной кнопки Back для выхода из приложения
        var backPressedTime by remember { mutableLongStateOf(0L) }
        BackHandler(enabled = true) {
            if (System.currentTimeMillis() - backPressedTime < 2000) {
                (context as? android.app.Activity)?.finish()
            } else {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(context, "Нажмите еще раз для выхода", Toast.LENGTH_SHORT).show()
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    text = "Выберите статью",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                // Превью первой статьи (Парадокс выбора)
                ArticlePreviewCard(
                    title = stringResource(id = R.string.article_paradox_title),
                    description = stringResource(id = R.string.article_paradox_intro),
                    imageUrl = "https://hi-news.ru/wp-content/uploads/2024/09/shop_hacks_1-750x439.jpg.webp",
                    onClick = { onNavigate(Screen.ArticleCompose) }
                )
            }

            item {
                // Превью второй статьи (XML)
                ArticlePreviewCard(
                    title = "Статья на View XML",
                    description = "Этот раздел будет реализован с использованием классического Android View System (XML). В разработке...",
                    imageUrl = "https://developer.android.com/static/images/social/android-developers.png",
                    onClick = { onNavigate(Screen.ArticleXml) }
                )
            }
        }
    }
}

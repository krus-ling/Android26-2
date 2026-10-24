package ru.urfu.droidpractice1.content.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.urfu.droidpractice1.R
import ru.urfu.droidpractice1.content.Screen
import ru.urfu.droidpractice1.content.components.ArticlePreviewCard

/**
 * Главный экран приложения со списком доступных статей.
 * 
 * @param isXmlArticleRead Флаг прочтения второй статьи.
 * @param onNavigate Функция обратного вызова для перехода на выбранный экран.
 * @param onOpenSecondArticle Функция открытия второй статьи (SecondActivity).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboardScreen(
    isXmlArticleRead: Boolean,
    onNavigate: (Screen) -> Unit,
    onOpenSecondArticle: () -> Unit
) {
    val context = LocalContext.current

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
        
        // Обработка системной кнопки Back для выхода из приложения
        var backPressedTime by remember { mutableLongStateOf(0L) }
        val exitToastMessage = stringResource(R.string.toast_press_back_again_to_exit)
        BackHandler(enabled = true) {
            if (System.currentTimeMillis() - backPressedTime < 2000) {
                (context as? Activity)?.finish()
            } else {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(context, exitToastMessage, Toast.LENGTH_SHORT).show()
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
                    text = stringResource(R.string.dashboard_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                // Превью первой статьи (Compose)
                ArticlePreviewCard(
                    title = stringResource(id = R.string.article_paradox_title),
                    description = stringResource(id = R.string.article_paradox_intro),
                    imageUrl = stringResource(id = R.string.url_article_paradox_image),
                    onClick = { onNavigate(Screen.ArticleCompose) }
                )
            }

            item {
                // Превью второй статьи (XML - SecondActivity)
                Box {
                    ArticlePreviewCard(
                        title = stringResource(id = R.string.article_habits_title),
                        description = stringResource(id = R.string.article_habits_intro),
                        imageUrl = stringResource(id = R.string.url_article_habits_image),
                        onClick = onOpenSecondArticle
                    )
                    
                    // Если статья прочитана, показываем плашку "ПРОЧИТАНО"
                    if (isXmlArticleRead) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp),
                            color = Color.White.copy(alpha = 0.92f),
                            shape = MaterialTheme.shapes.small,
                            shadowElevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = stringResource(R.string.read_badge),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

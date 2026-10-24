package ru.urfu.droidpractice1.content

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ru.urfu.droidpractice1.SecondActivity
import ru.urfu.droidpractice1.content.screens.ArticleComposeScreen
import ru.urfu.droidpractice1.content.screens.MainDashboardScreen
import ru.urfu.droidpractice1.ui.theme.DroidPractice1Theme

enum class Screen {
    Dashboard,
    ArticleCompose
}

@Composable
fun MainActivityScreen() {
    var currentScreen by rememberSaveable { mutableStateOf(Screen.Dashboard) }
    var isXmlArticleRead by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    // Лаунчер для запуска SecondActivity и получения результата отметки "Прочитано"
    val xmlArticleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val isRead = result.data?.getBooleanExtra(SecondActivity.EXTRA_IS_READ, false) ?: false
            isXmlArticleRead = isRead
        }
    }

    val onOpenSecondArticle = {
        val intent = Intent(context, SecondActivity::class.java).apply {
            putExtra(SecondActivity.EXTRA_IS_READ, isXmlArticleRead)
        }
        xmlArticleLauncher.launch(intent)
    }

    DroidPractice1Theme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    if (targetState != Screen.Dashboard) {
                        (slideInHorizontally { width -> width } + fadeIn()) togetherWith
                                (slideOutHorizontally { width -> -width } + fadeOut())
                    } else {
                        (slideInHorizontally { width -> -width } + fadeIn()) togetherWith
                                (slideOutHorizontally { width -> width } + fadeOut())
                    }
                },
                label = "ScreenTransition"
            ) { screen ->
                when (screen) {
                    Screen.Dashboard -> MainDashboardScreen(
                        isXmlArticleRead = isXmlArticleRead,
                        onNavigate = { currentScreen = it },
                        onOpenSecondArticle = onOpenSecondArticle
                    )
                    Screen.ArticleCompose -> ArticleComposeScreen(
                        isSecondArticleRead = isXmlArticleRead,
                        onBack = { currentScreen = Screen.Dashboard },
                        onOpenSecondArticle = onOpenSecondArticle
                    )
                }
            }
        }
    }
}

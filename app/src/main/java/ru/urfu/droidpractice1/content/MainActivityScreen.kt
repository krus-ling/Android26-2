package ru.urfu.droidpractice1.content

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
import ru.urfu.droidpractice1.content.screens.ArticleComposeScreen
import ru.urfu.droidpractice1.content.screens.ArticleXmlPlaceholder
import ru.urfu.droidpractice1.content.screens.MainDashboardScreen
import ru.urfu.droidpractice1.ui.theme.DroidPractice1Theme

/**
 * Перечисление доступных экранов приложения для управления навигацией.
 */
enum class Screen {
    Dashboard, // Главный экран со списком статей
    ArticleCompose, // Экран статьи на Jetpack Compose
    ArticleXml // Экран-заглушка для статьи на View XML
}

@Composable
fun MainActivityScreen() {
    // Состояние текущего активного экрана (сохраняется при смене конфигурации)
    var currentScreen by rememberSaveable { mutableStateOf(Screen.Dashboard) }

    DroidPractice1Theme {
        // Оборачиваем в Box с фоном темы, чтобы при переходах не мелькал белый экран
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    // Плавная анимация: при входе в статью - вправо, при выходе в меню - влево
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
                        onNavigate = { currentScreen = it }
                    )
                    Screen.ArticleCompose -> ArticleComposeScreen(
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                    Screen.ArticleXml -> ArticleXmlPlaceholder(
                        onBack = { currentScreen = Screen.Dashboard }
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MainScreenPreview() {
//    MainActivityScreen()
//}

package ru.urfu.droidpractice1.content

import android.content.Intent
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
    val context = LocalContext.current

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
                        onNavigate = { currentScreen = it }
                    )
                    Screen.ArticleCompose -> ArticleComposeScreen(
                        onBack = { currentScreen = Screen.Dashboard },
                        onOpenSecondArticle = {
                            val intent = Intent(context, SecondActivity::class.java)
                            context.startActivity(intent)
                        }
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

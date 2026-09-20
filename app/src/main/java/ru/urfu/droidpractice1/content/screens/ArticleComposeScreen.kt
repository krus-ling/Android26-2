package ru.urfu.droidpractice1.content.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.urfu.droidpractice1.R
import ru.urfu.droidpractice1.content.ArticleSectionData
import ru.urfu.droidpractice1.content.components.*

/**
 * Экран с детальным описанием статьи, реализованный на Jetpack Compose.
 * 
 * @param onBack Функция обратного вызова для возврата на предыдущий экран.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleComposeScreen(onBack: () -> Unit) {
    val listState = rememberLazyListState()

    // Слушатель системной кнопки "Назад"
    BackHandler(onBack = onBack)

    // Отслеживаем момент, когда заголовок статьи скрылся за экраном
    val showTitle by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 80
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    AnimatedContent(
                        targetState = showTitle,
                        transitionSpec = {
                            if (targetState) {
                                // Скролл вниз: название приложения уезжает вверх, заголовок статьи выезжает снизу
                                (slideInVertically { height -> height } + fadeIn()) togetherWith
                                        (slideOutVertically { height -> -height } + fadeOut())
                            } else {
                                // Возврат наверх: заголовок статьи уезжает вниз, название приложения возвращается сверху
                                (slideInVertically { height -> -height } + fadeIn()) togetherWith
                                        (slideOutVertically { height -> height } + fadeOut())
                            }
                        },
                        label = "TopAppBarTitleAnimation"
                    ) { isScrolled ->
                        Text(
                            text = if (isScrolled) {
                                stringResource(id = R.string.article_paradox_title)
                            } else {
                                stringResource(id = R.string.app_name)
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = if (isScrolled) {
                                MaterialTheme.typography.titleMedium
                            } else {
                                MaterialTheme.typography.titleLarge
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ArticleContent(
            listState = listState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

/**
 * Внутренний контент статьи (список разделов).
 */
@Composable
private fun ArticleContent(
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    val sections = remember {
        listOf(
            ArticleSectionData(
                id = "problem",
                title = R.string.article_paradox_problem_title,
                description = R.string.article_paradox_problem_desc,
                titleItems = R.string.article_paradox_problem_items_title,
                listOfItems = listOf(
                    R.string.article_paradox_problem_item_1,
                    R.string.article_paradox_problem_item_2,
                    R.string.article_paradox_problem_item_3
                )
            ),
            ArticleSectionData(
                id = "solution",
                title = R.string.article_paradox_solution_title,
                description = R.string.article_paradox_solution_desc,
                titleItems = R.string.article_paradox_solution_items_title,
                listOfItems = listOf(
                    R.string.article_paradox_solution_item_1,
                    R.string.article_paradox_solution_item_2,
                )
            ),
            ArticleSectionData(
                id = "steps",
                title = R.string.article_paradox_steps_title,
                description = R.string.article_paradox_steps_desc,
                titleItems = R.string.article_paradox_steps_items_title,
                listOfItems = listOf(
                    R.string.article_paradox_steps_item_1,
                    R.string.article_paradox_steps_item_2,
                    R.string.article_paradox_steps_item_3,
                )
            )
        )
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item(key = "title") {
            Text(
                text = stringResource(id = R.string.article_paradox_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        item(key = "intro") { 
            ArticleIntro() 
        }
        
        item(key = "image") { 
            ArticleImage() 
        }

        items(
            items = sections,
            key = { section -> section.id }
        ) { section ->
            ArticleSectionCard(
                title = section.title,
                description = section.description,
                titleItems = section.titleItems,
                listOfItems = section.listOfItems
            )
        }
        
        item(key = "actions") {
            ArticleActions()
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

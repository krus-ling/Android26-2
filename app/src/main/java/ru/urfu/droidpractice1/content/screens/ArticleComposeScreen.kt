package ru.urfu.droidpractice1.content.screens

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
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
 * @param onOpenSecondArticle Функция переключения на следующую статью.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleComposeScreen(
    onBack: () -> Unit,
    onOpenSecondArticle: (() -> Unit)? = null
) {
    val listState = rememberLazyListState()

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
                            contentDescription = stringResource(R.string.back)
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
            onOpenSecondArticle = onOpenSecondArticle,
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
    onOpenSecondArticle: (() -> Unit)?,
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
            ArticleActions(
                articleId = "article_1",
                articleTitle = stringResource(id = R.string.article_paradox_title)
            )
        }

        if (onOpenSecondArticle != null) {
            item(key = "next_article") {
                val context = LocalContext.current
                val prefs = remember { context.getSharedPreferences("article_prefs_article_2", Context.MODE_PRIVATE) }
                val isRead = prefs.getBoolean("key_is_read", false)

                Box {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer { alpha = if (isRead) 0.65f else 1f }
                            .clickable { onOpenSecondArticle() },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.next_article),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = stringResource(id = R.string.article_habits_title),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = stringResource(R.string.next_article),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }

                    if (isRead) {
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
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

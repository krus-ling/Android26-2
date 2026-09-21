package ru.urfu.droidpractice1.content.components

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import coil3.compose.AsyncImage
import ru.urfu.droidpractice1.R
import ru.urfu.droidpractice1.content.Reaction
import ru.urfu.droidpractice1.content.calculateReaction

/**
 * Карточка превью статьи для главного экрана.
 *
 * @param title Заголовок статьи.
 * @param description Краткое описание статьи.
 * @param imageUrl URL изображения для превью.
 * @param onClick Обработчик нажатия на карточку.
 */
@Composable
fun ArticlePreviewCard(
    title: String,
    description: String,
    imageUrl: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp)
    ) {
        Column {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Интро-текст статьи.
 */
@Composable
fun ArticleIntro() {
    Text(
        text = stringResource(id = R.string.article_paradox_intro),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}

/**
 * Основное изображение статьи с подписью.
 */
@Composable
fun ArticleImage() {
    val isPreview = LocalInspectionMode.current
    Column {
        AsyncImage(
            model = if (isPreview) R.drawable.ic_launcher_background else "https://hi-news.ru/wp-content/uploads/2024/09/shop_hacks_1-750x439.jpg.webp",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.ic_launcher_background),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(16.dp))
        )
        Text(
            text = "Визуализация изобилия выбора",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .padding(top = 8.dp, start = 4.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Карточка раздела статьи (Проблема, Решение и т.д.).
 *
 * @param title Заголовок раздела.
 * @param description Описание раздела.
 * @param titleItems Заголовок списка пунктов.
 * @param listOfItems Список ресурсов строк для пунктов.
 */
@Composable
fun ArticleSectionCard(
    @StringRes title: Int,
    @StringRes description: Int,
    @StringRes titleItems: Int,
    @StringRes listOfItems: List<Int>
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(titleItems),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            listOfItems.forEach { item ->
                Row(
                    modifier = Modifier.padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                    )
                    Text(
                        text = stringResource(item),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
        }
    }
}

/**
 * Панель действий статьи: лайки, дизлайки и кнопка "Поделиться".
 * 
 * @param articleId Уникальный идентификатор статьи для раздельного хранения лайков/дизлайков.
 * @param initialLikes Начальное количество лайков для этой статьи.
 * @param initialDislikes Начальное количество дизлайков для этой статьи.
 */
@Composable
fun ArticleActions(
    articleId: String = "article_1",
    initialLikes: Int = 10,
    initialDislikes: Int = 0
) {
    val context = LocalContext.current
    val prefs = remember(articleId) { context.getSharedPreferences("article_prefs_$articleId", Context.MODE_PRIVATE) }

    var likes by rememberSaveable(articleId) { mutableIntStateOf(prefs.getInt("key_likes", initialLikes)) }
    var dislikes by rememberSaveable(articleId) { mutableIntStateOf(prefs.getInt("key_dislikes", initialDislikes)) }
    
    // Хранение выбора пользователя (Лайк/Дизлайк/Ничего)
    var userSelection by rememberSaveable(articleId) {
        val savedName = prefs.getString("key_selection", Reaction.NONE.name)
        val initialReaction = runCatching { Reaction.valueOf(savedName!!) }.getOrDefault(Reaction.NONE)
        mutableStateOf(initialReaction)
    }

    // Обработка клика по кнопке реакции
    val onReactionClick: (Reaction) -> Unit = { target ->
        val result = calculateReaction(
            currentSelection = userSelection,
            target = target,
            likes = likes,
            dislikes = dislikes
        )
        likes = result.likes
        dislikes = result.dislikes
        userSelection = result.selection

        // Сохранение состояния
        prefs.edit {
            putInt("key_likes", result.likes)
                .putInt("key_dislikes", result.dislikes)
                .putString("key_selection", result.selection.name)
        }
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Секция лайков и дизлайков
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 4.dp)
            ) {
                // Кнопка Лайка
                ReactionButton(
                    icon = Icons.Default.ThumbUp,
                    reaction = Reaction.LIKE,
                    count = likes,
                    currentSelection = userSelection,
                    contentDescription = "Лайк",
                    onClick = { onReactionClick(it) }
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .width(1.dp)
                        .height(16.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )

                // Кнопка Дизлайка
                ReactionButton(
                    icon = Icons.Default.ThumbDown,
                    reaction = Reaction.DISLIKE,
                    count = dislikes,
                    currentSelection = userSelection,
                    contentDescription = "Дизлайк",
                    onClick = { onReactionClick(it) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            // Настройка интента для "Поделиться"
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, stringResource(R.string.textToShare))
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, stringResource(R.string.textToShareTitle))

            // Кнопка Поделиться
            FilledTonalButton(
                onClick = { context.startActivity(shareIntent) },
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.ShareButtonText),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

/**
 * Вспомогательный компонент кнопки реакции (лайк/дизлайк).
 */
@Composable
private fun ReactionButton(
    icon: ImageVector,
    reaction: Reaction,
    count: Int,
    currentSelection: Reaction,
    contentDescription: String,
    onClick: (Reaction) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelected = reaction == currentSelection
    val tintColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        IconButton(onClick = { onClick(reaction) }) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = tintColor,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.labelLarge,
            color = tintColor,
            fontWeight = FontWeight.Bold
        )
    }
}

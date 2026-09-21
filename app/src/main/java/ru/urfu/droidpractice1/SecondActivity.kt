package ru.urfu.droidpractice1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil3.load
import coil3.request.error
import coil3.request.placeholder
import ru.urfu.droidpractice1.content.components.ArticleActions
import ru.urfu.droidpractice1.databinding.ActivitySecondBinding
import ru.urfu.droidpractice1.ui.theme.DroidPractice1Theme

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private val TAG = "SecondActivity"

    private val articleImageUrl = "https://images.unsplash.com/photo-1506784983877-45594efa4cbe"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")
        enableEdgeToEdge()
        
        // Инициализация ViewBinding
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.appBarLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        // 1. Загрузка картинки статьи через Coil
        if (articleImageUrl.isNotBlank()) {
            binding.articleImage.load(articleImageUrl) {
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_background)
            }
        }

        // 2. Настройка тулбара
        binding.toolbar.title = getString(R.string.article_habits_title)
        binding.toolbar.setNavigationOnClickListener {
            Log.d(TAG, "Navigation back clicked")
            onBackPressedDispatcher.onBackPressed()
        }
        binding.toolbar.post {
            for (i in 0 until binding.toolbar.childCount) {
                val child = binding.toolbar.getChildAt(i)
                if (child is android.widget.TextView) {
                    child.isSingleLine = true
                    child.maxLines = 1
                    child.ellipsize = android.text.TextUtils.TruncateAt.END
                }
            }
        }

        // 3. Логика переключателя "Прочитано" с сохранением и восстановлением состояния
        val prefs = getSharedPreferences("article_prefs_article_2", MODE_PRIVATE)
        val initialIsRead = intent.getBooleanExtra("is_read", prefs.getBoolean("key_is_read", false))

        // Устанавливаем начальное состояние тумблера ДО подписки на слушатель,
        // чтобы не вызывать Toast и событие при запуске экрана
        binding.readSwitch.isChecked = initialIsRead
        setResult(RESULT_OK, Intent().apply { putExtra("is_read", initialIsRead) })

        binding.readSwitch.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) getString(R.string.toast_article_marked_read) else getString(R.string.toast_article_marked_unread)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Read switch changed: $isChecked")
            
            // Сохраняем состояние в SharedPreferences
            prefs.edit {
                putBoolean("key_is_read", isChecked)
            }

            // Передаем результат назад в MainActivity (пункт 6 задания)
            val resultIntent = Intent().apply {
                putExtra("is_read", isChecked)
            }
            setResult(RESULT_OK, resultIntent)
        }

        // 4. Встраиваем переиспользуемую Compose-панель действий (Лайки / Дизлайки / Поделиться)
        binding.actionsComposeView.setContent {
            DroidPractice1Theme {
                ArticleActions(
                    articleId = "article_2",
                    initialLikes = 5,
                    initialDislikes = 1,
                    articleTitle = getString(R.string.article_habits_title)
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }
}

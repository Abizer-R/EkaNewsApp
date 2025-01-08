package com.abizer_r.newsapp.ui.newsWebView

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.abizer_r.newsapp.ui.NewsApp
import com.abizer_r.newsapp.ui.navigation.BottomNavBar
import com.abizer_r.newsapp.ui.navigation.NewsNavHostContainer
import com.abizer_r.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewActivity : ComponentActivity() {

    companion object {
        const val EXTRA_URL = "extra_url"
        const val EXTRA_NEWS_ID = "extra_news_id"
        const val EXTRA_SHOULD_SAVE_NEWS_ID = "extra_should_save_news_id"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra(EXTRA_URL) ?: ""
        val newsId = intent.getStringExtra(EXTRA_NEWS_ID)

        setContent {
            NewsAppTheme {
                WebViewScreen(
                    modifier = Modifier
                        .padding(WindowInsets.systemBars.asPaddingValues())
                        .fillMaxSize(),
                    url = url,
                    onBackPressed = { finish() },
                    onSaveClicked = {
                        val resultIntent = Intent().apply {
                            putExtra(EXTRA_SHOULD_SAVE_NEWS_ID, newsId)
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                )
            }
        }
    }
}
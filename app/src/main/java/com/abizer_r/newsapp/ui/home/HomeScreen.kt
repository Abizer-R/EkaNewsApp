package com.abizer_r.newsapp.ui.home

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abizer_r.newsapp.ui.common.error.RetryView
import com.abizer_r.newsapp.ui.common.loading.LoadingView
import com.abizer_r.newsapp.ui.home.model.NewsItem
import com.abizer_r.newsapp.ui.newsWebView.WebViewActivity
import com.abizer_r.newsapp.ui.newsWebView.WebViewActivity.Companion.EXTRA_NEWS_ID
import com.abizer_r.newsapp.ui.newsWebView.WebViewActivity.Companion.EXTRA_SHOULD_SAVE_NEWS_ID
import com.abizer_r.newsapp.ui.newsWebView.WebViewActivity.Companion.EXTRA_URL
import com.abizer_r.newsapp.ui.theme.NewsAppTheme

@Composable
fun HomeScreen(
    goToSavedScreen: () -> Unit = {},
    viewModel: NewsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val navigateToSavedScreen by viewModel.navigateToSavedScreenState.collectAsStateWithLifecycle()

    LaunchedEffect(navigateToSavedScreen) {
        if (navigateToSavedScreen) {
            goToSavedScreen()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val shouldSavedNewsId = result.data?.getStringExtra(EXTRA_SHOULD_SAVE_NEWS_ID)
            viewModel.saveNews(shouldSavedNewsId)
        }
    }

    when (screenState) {
        is HomeScreenState.Loading -> LoadingView()

        is HomeScreenState.Failure -> RetryView(
            errorText = (screenState as HomeScreenState.Failure).errorMessage,
            onRetryClicked = {
                viewModel.fetchTopHeadlines()
            }
        )

        is HomeScreenState.Success -> {
            NewsListVertical(
                newsList = screenState.getNewsList(),
                onItemClick = { item ->
                    launcher.launch(
                        Intent(context, WebViewActivity::class.java).apply {
                            putExtra(EXTRA_URL, item.newsUrl)
                            putExtra(EXTRA_NEWS_ID, item.id)
                        }
                    )
                },
                onReload = { viewModel.fetchTopHeadlines() },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val newsList = arrayListOf<NewsItem>()
    repeat(10) { index ->
        val item = NewsItem("$index", "Dummy Heading $index", "Dummy Desc $index", "", "")
        newsList.add(item)
    }
    NewsAppTheme {
        NewsListVertical(newsList)
    }
}
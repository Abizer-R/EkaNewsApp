package com.abizer_r.newsapp.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abizer_r.newsapp.R
import com.abizer_r.newsapp.ui.common.error.RetryView
import com.abizer_r.newsapp.ui.common.loading.LoadingView
import com.abizer_r.newsapp.ui.home.model.NewsItem
import com.abizer_r.newsapp.ui.newsWebView.WebViewActivity
import com.abizer_r.newsapp.ui.newsWebView.WebViewActivity.Companion.EXTRA_NEWS_ID
import com.abizer_r.newsapp.ui.newsWebView.WebViewActivity.Companion.EXTRA_SHOULD_SAVE_NEWS_ID
import com.abizer_r.newsapp.ui.newsWebView.WebViewActivity.Companion.EXTRA_URL
import com.abizer_r.newsapp.ui.theme.NewsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    goToSavedScreen: () -> Unit = {},
    viewModel: NewsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val navigateToSavedScreen by viewModel.navigateToSavedScreenState.collectAsStateWithLifecycle()
    val isRefreshingByPull by remember { mutableStateOf(false) }

    val isFromCache by remember {
        derivedStateOf {
            screenState is HomeScreenState.Success && (screenState as HomeScreenState.Success).isOldCachedData
        }
    }

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

    when {
        screenState is HomeScreenState.Loading && isRefreshingByPull.not() -> LoadingView()

        screenState is HomeScreenState.Failure  -> RetryView(
            errorText = (screenState as HomeScreenState.Failure).errorMessage,
            onRetryClicked = {
                viewModel.fetchTopHeadlines()
            }
        )

        else -> {
            PullToRefreshBox(
                isRefreshing = isRefreshingByPull,
                onRefresh = { viewModel.fetchTopHeadlines() },
                modifier = Modifier.fillMaxSize()
            ) {
                HomeScreenContent(
                    screenState,
                    launcher,
                    context,
                    isFromCache,
                    onRetryClicked = { viewModel.fetchTopHeadlines() }
                )
            }
        }
    }
}

@Composable
private fun HomeScreenContent(
    screenState: HomeScreenState,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    context: Context,
    isFromCache: Boolean,
    onRetryClicked: () -> Unit
) {
    Column {
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
            emptyListView = {
                RetryView(
                    errorText = stringResource(R.string.no_news_found),
                    onRetryClicked = onRetryClicked
                )
            },
            modifier = Modifier.weight(1f)
        )

        AnimatedVisibility(
            visible = isFromCache
        ) {
            val remoteError = (screenState as HomeScreenState.Success).errorMessage
                ?: stringResource(R.string.showing_cached_data_error)
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Info, contentDescription = "")
                Text(
                    text = remoteError,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
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
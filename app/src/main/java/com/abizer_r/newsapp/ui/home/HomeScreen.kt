package com.abizer_r.newsapp.ui.home

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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

@Composable
fun HomeScreen(
    goToSavedScreen: () -> Unit = {},
    viewModel: NewsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val navigateToSavedScreen by viewModel.navigateToSavedScreenState.collectAsStateWithLifecycle()

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

    when (screenState) {
        is HomeScreenState.Loading -> LoadingView()

        is HomeScreenState.Failure -> RetryView(
            errorText = (screenState as HomeScreenState.Failure).errorMessage,
            onRetryClicked = {
                viewModel.fetchTopHeadlines()
            }
        )

        is HomeScreenState.Success -> {
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
                    onReload = { viewModel.fetchTopHeadlines() },
                    modifier = Modifier.weight(1f)
                )

                AnimatedVisibility(
                    visible = isFromCache
                ) {
                    val remoteError = (screenState as HomeScreenState.Success).errorMessage
                        ?: stringResource(R.string.showing_cached_data_error)
                    val errorText = if (viewModel.isNetworkAvailable.not()) {
                        stringResource(R.string.showing_cached_data_error)
                    } else remoteError
                    OutdatedDataBanner(
                        errorMessage = errorText,
                        onRetryClicked = {
                            viewModel.fetchTopHeadlines()
                        }
                    )
                }
            }
        }
    }
    if (isFromCache) {
        LaunchedEffect(Unit) {
            val error = (screenState as HomeScreenState.Success).errorMessage
            SnackbarHostState().showSnackbar(
                message = error ?: context.getString(R.string.showing_cached_data_error)
            )
        }
    }
}

@Composable
fun OutdatedDataBanner(errorMessage: String?, onRetryClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = errorMessage ?: stringResource(R.string.showing_cached_data_error),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Button(onClick = onRetryClicked) {
            Text(stringResource(R.string.retry))
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
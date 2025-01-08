package com.abizer_r.newsapp.ui.saved

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abizer_r.newsapp.ui.common.error.RetryView
import com.abizer_r.newsapp.ui.common.loading.LoadingView
import com.abizer_r.newsapp.ui.home.NewsListVertical
import com.abizer_r.newsapp.ui.newsWebView.WebViewActivity
import com.abizer_r.newsapp.ui.newsWebView.WebViewActivity.Companion.EXTRA_IS_SAVED
import com.abizer_r.newsapp.ui.newsWebView.WebViewActivity.Companion.EXTRA_NEWS_ID
import com.abizer_r.newsapp.ui.newsWebView.WebViewActivity.Companion.EXTRA_URL

@Composable
fun SavedScreen(
    viewModel: SavedNewsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val savedNewsId = result.data?.getStringExtra(EXTRA_NEWS_ID)
            // TODO: delete the saved item from the database
            // TODO: allow WebViewScreen itself to check if item "url" is in database or not
        }
    }

    when (screenState) {
        is SavedNewsScreenState.Loading -> LoadingView()

        is SavedNewsScreenState.Failure -> RetryView(
            errorText = (screenState as SavedNewsScreenState.Failure).errorMessage,
            onRetryClicked = {
                viewModel.fetchSavedNews()
            }
        )

        is SavedNewsScreenState.Success -> {
            NewsListVertical(
                newsList = screenState.getNewsList(),
                showDelete = true,
                onItemClick = { item ->
                    launcher.launch(
                        Intent(context, WebViewActivity::class.java).apply {
                            putExtra(EXTRA_URL, item.newsUrl)
                            putExtra(EXTRA_NEWS_ID, item.id)
                            putExtra(EXTRA_IS_SAVED, true)
                        }
                    )
                },
                onDeleteClick = { item ->
                    viewModel.deleteSavedNews(item)
                },
                onReload = { viewModel.fetchSavedNews() },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
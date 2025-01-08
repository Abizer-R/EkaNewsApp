package com.abizer_r.newsapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.abizer_r.newsapp.R
import com.abizer_r.newsapp.ui.common.error.RetryView
import com.abizer_r.newsapp.ui.home.model.NewsItem

@Composable
fun NewsListVertical(
    newsList: List<NewsItem>,
    onItemClick: (NewsItem) -> Unit = {},
    onReload: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (newsList.isEmpty()) {
        RetryView(
            errorText = stringResource(R.string.no_news_found),
            onRetryClicked = onReload
        )
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(newsList) { item ->
            NewsItemRow(
                item = item,
                onItemClick = onItemClick
            )
        }
    }
}
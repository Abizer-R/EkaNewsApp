package com.abizer_r.newsapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abizer_r.newsapp.R
import com.abizer_r.newsapp.ui.common.error.RetryView
import com.abizer_r.newsapp.ui.home.model.NewsItem
import com.abizer_r.newsapp.ui.theme.NewsAppTheme

@Composable
fun NewsListVertical(
    newsList: List<NewsItem>,
    showDelete: Boolean = false,
    emptyListView: @Composable () -> Unit = {},
    onItemClick: (NewsItem) -> Unit = {},
    onDeleteClick: (NewsItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (newsList.isEmpty()) {
        emptyListView()
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
                onItemClick = onItemClick,
                showDelete = showDelete,
                onDeleteClick = onDeleteClick
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
        NewsListVertical(
            newsList,
            showDelete = true
        )
    }
}
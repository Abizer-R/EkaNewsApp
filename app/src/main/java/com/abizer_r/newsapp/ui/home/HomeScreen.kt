package com.abizer_r.newsapp.ui.home

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.abizer_r.data.news.model.Article
import com.abizer_r.newsapp.R
import com.abizer_r.newsapp.ui.newsWebView.WebViewActivity
import com.abizer_r.newsapp.ui.newsWebView.WebViewActivity.Companion.EXTRA_NEWS_ID
import com.abizer_r.newsapp.ui.newsWebView.WebViewActivity.Companion.EXTRA_URL
import com.abizer_r.newsapp.ui.theme.NewsAppTheme
import java.util.UUID

@Composable
fun HomeScreen(
    viewModel: NewsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val savedNewsId = result.data?.getStringExtra(EXTRA_NEWS_ID)
            savedNewsId?.let {
                // TODO: save item in ROOM database
                Log.d("HomeScreen", "HomeScreen: News item saved with ID: $savedNewsId")
            }
        }
    }


    when (screenState) {
        HomeScreenState.Loading -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
        is HomeScreenState.Failure -> {
            val errorMessage = (screenState as HomeScreenState.Failure).errorMessage
                ?: stringResource(R.string.something_went_wrong)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        is HomeScreenState.Success -> {
            val newsList = (screenState as HomeScreenState.Success).articles
            HomeScreenContent(
                newsList = newsList,
                onItemClick = { item ->
                    launcher.launch(
                        Intent(context, WebViewActivity::class.java).apply {
                            putExtra(EXTRA_URL, item.newsUrl)
                            putExtra(EXTRA_NEWS_ID, item.localId)
                        }
                    )
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun HomeScreenContent(
    newsList: List<NewsItem>,
    onItemClick: (NewsItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (newsList.isEmpty()) {
        Box(modifier.fillMaxSize()) {
            Text(
                text = stringResource(R.string.no_news_found),
                modifier = Modifier.align(Alignment.Center)
            )
        }
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

@Composable
fun NewsItemRow(
    item: NewsItem,
    onItemClick: (NewsItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary.copy(0.1f))
            .clickable { onItemClick(item) }
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.thumbnailUrl,
            placeholder = painterResource(id = R.drawable.ic_image_placeholder),
            error = painterResource(id = R.drawable.ic_image_placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .weight(0.4f)
                .aspectRatio(1f)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.heading,
                style = MaterialTheme.typography.titleMedium.copy(
                    lineHeight = 20.sp
                ),
                maxLines = 2,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.size(2.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = AnnotatedString("Read more..."),
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary)
            )
        }
    }
}

data class NewsItem(
    val heading: String,
    val description: String,
    val thumbnailUrl: String,
    val newsUrl: String,
    val localId: String = UUID.randomUUID().toString()
)

fun Article.toDomainNewsItem(): NewsItem {
    return NewsItem(
        heading = this.title ?: "",
        description = this.description ?: "",
        thumbnailUrl = this.urlToImage ?: "",
        newsUrl = this.url ?: ""
    )
}

@Preview
@Composable
private fun Preview() {
    val newsList = arrayListOf<NewsItem>()
    repeat(10) { index ->
        val item = NewsItem("Dummy Heading $index", "Dummy Desc $index", "", "")
        newsList.add(item)
    }
    NewsAppTheme {
        HomeScreenContent(newsList)
    }
}
package com.abizer_r.newsapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abizer_r.newsapp.R
import com.abizer_r.newsapp.ui.theme.NewsAppTheme

@Composable
fun HomeScreen() {
    val newsItems = listOf(
        NewsItem(
            thumbnailResId = R.drawable.ic_image_placeholder,
            heading = "Breaking News: Compose Updates",
            description = "Jetpack Compose has released a new update that includes exciting new features and enhancements. Stay tuned for more details!"
        ),
        NewsItem(
            thumbnailResId = R.drawable.ic_image_placeholder,
            heading = "Tech Giants Collaborate",
            description = "Top tech companies are joining forces to tackle pressing global challenges. This initiative marks a significant milestone in the industry."
        ),
        NewsItem(
            thumbnailResId = R.drawable.ic_image_placeholder,
            heading = "Space Exploration Advances",
            description = "A new era of space exploration begins as private companies and governments invest heavily in lunar and Martian missions."
        )
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(newsItems) { item ->
            NewsItemRow(item = item)
        }
    }
}

@Composable
fun NewsItemRow(
    item: NewsItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background.copy(0.8f))
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(item.thumbnailResId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .size(72.dp)
                .aspectRatio(1f)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.heading,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            ClickableText(
                text = AnnotatedString("Read more..."),
                onClick = {},
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary)
            )
        }
    }
}

data class NewsItem(
    val thumbnailResId: Int,
    val heading: String,
    val description: String
)

@Preview
@Composable
private fun Preview() {
    NewsAppTheme {
        HomeScreen()
    }
}
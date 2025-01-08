package com.abizer_r.newsapp.ui.newsWebView

import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.abizer_r.newsapp.R
import com.abizer_r.newsapp.ui.theme.NewsAppTheme

@Composable
fun WebViewScreen(
    url: String,
    onBackPressed: () -> Unit,
    onSaveClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box {
        Column(modifier = modifier.fillMaxSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier.weight(0.2f)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = stringResource(R.string.news_details),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
            }
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        loadUrl(url)
                    }
                },
                modifier = Modifier.weight(1f).fillMaxSize()
            )
        }
        FloatingActionButton(
            onClick = {
                onSaveClicked()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Outlined.BookmarkBorder, contentDescription = "Save")
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    NewsAppTheme {
        WebViewScreen(
            url = "https://www.google.com",
            onBackPressed = {},
            onSaveClicked = {}
        )
    }
}
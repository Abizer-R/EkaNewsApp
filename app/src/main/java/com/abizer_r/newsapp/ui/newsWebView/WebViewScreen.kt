package com.abizer_r.newsapp.ui.newsWebView

import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abizer_r.newsapp.R
import com.abizer_r.newsapp.ui.common.error.RetryView
import com.abizer_r.newsapp.ui.common.loading.LoadingView
import com.abizer_r.newsapp.ui.theme.NewsAppTheme

@Composable
fun WebViewScreen(
    url: String,
    viewModel: WebViewScreenViewModel = hiltViewModel(),
    onBackPressed: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isSaved by viewModel.isSaved.collectAsStateWithLifecycle()
    val newsUnSaved by viewModel.newsUnsaved.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    val handleFabClicked: () -> Unit = remember { {
        if (showDialog.not()) {
            showDialog = true
        }
    } }

    LaunchedEffect(Unit) {
        viewModel.checkIfSaved(url)
    }

    LaunchedEffect(newsUnSaved) {
        if (newsUnSaved) {
            Toast.makeText(context, context.getString(R.string.unsave_successful), Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }

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
            WebViewCompose(
                url = url,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            )
        }
        FloatingActionButton(
            onClick = handleFabClicked,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(WindowInsets.navigationBars.asPaddingValues())
                .padding(16.dp)
        ) {
            val icon = if (isSaved) {
                Icons.Default.Bookmark
            } else Icons.Outlined.BookmarkBorder
            Icon(
                imageVector = icon,
                contentDescription = "Save",
                modifier = Modifier.clickable { handleFabClicked() }
            )
        }
    }

    if (showDialog) {
        ConfirmationDialog(
            isSaved = isSaved,
            onConfirm = {
                if (isSaved) {
                    viewModel.deleteNews(url)
                } else {
                    onSaveClicked()
                }
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}


@Composable
private fun ConfirmationDialog(
    isSaved: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val title = stringResource(if (isSaved) R.string.unsave else R.string.save)
    val text = stringResource(if (isSaved) R.string.unsave_dialog_text else R.string.save_dialog_text )
    val positiveBtnText = title
    val negativeBtnText = stringResource(R.string.cancel)

    AlertDialog(
        onDismissRequest = {},
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(positiveBtnText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(negativeBtnText)
            }
        }
    )
}

@Composable
private fun WebViewCompose(
    url: String,
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }

    Box(modifier) {
        if (hasError) {
            RetryView(
                errorText = stringResource(R.string.failed_to_load_page),
                onRetryClicked = {
                    hasError = false
                    isLoading = true
                }
            )
        } else {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(
                                view: WebView?,
                                url: String?,
                                favicon: Bitmap?
                            ) {
                                super.onPageStarted(view, url, favicon)
                                isLoading = true
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                isLoading = false
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                isLoading = false
                                hasError = true
                            }
                        }
                        loadUrl(url)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

        }
        if (isLoading) {
            LoadingView()
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
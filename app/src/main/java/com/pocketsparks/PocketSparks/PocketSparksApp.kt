package com.pocketsparks.PocketSparks

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pocketsparks.PocketSparks.database.KeyValueDatabase
import com.pocketsparks.PocketSparks.ui.component.PocketSparksBottomBar
import com.pocketsparks.PocketSparks.ui.component.PocketSparksExplorerDrawer
import com.pocketsparks.PocketSparks.ui.component.PocketSparksSettingsSheet
import com.pocketsparks.PocketSparks.ui.theme.PocketSparksTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PocketSparksApp() {
    val context = LocalContext.current
    val settings = remember { KeyValueDatabase(context, "settings") }
    val coroutineScope = rememberCoroutineScope()

    var isBottomBarVisible by remember { mutableStateOf(true) }

    val openDocumentIntent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                if (uri != null) {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                    coroutineScope.launch {
                        settings.set("folderUri", uri)
                    }
                }
            }
        }
    var isExplorerDrawerVisible by remember { mutableStateOf(false) }

    val settingsSheetState = rememberModalBottomSheetState()
    var isSettingsSheetVisible by remember { mutableStateOf(false) }

    PocketSparksTheme {
        Scaffold(
            bottomBar = {
                if (isBottomBarVisible) {
                    PocketSparksBottomBar(
                        onExplorerClick = {
                            coroutineScope.launch {
                                val uri = settings.getUri("folderUri")
                                if (uri == null) {
                                    launcher.launch(openDocumentIntent)
                                } else {
                                    isExplorerDrawerVisible = true
                                    isBottomBarVisible = false
                                }
                            }
                        },
                        onSettingsClick = {
                            coroutineScope.launch { settingsSheetState.show() }
                                .invokeOnCompletion { isSettingsSheetVisible = true }
                        },
                    )
                }
            },
        ) { innerPadding ->
            Text(
                modifier = Modifier.padding(innerPadding), text = stringResource(R.string.text)
            )

            if (isExplorerDrawerVisible) {
                PocketSparksExplorerDrawer(settings = settings, onDismissRequest = {
                    isExplorerDrawerVisible = false
                    isBottomBarVisible = true
                })
            }

            if (isSettingsSheetVisible) {
                PocketSparksSettingsSheet(onDismissRequest = { isSettingsSheetVisible = false })
            }
        }
    }
}

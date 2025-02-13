package com.pocketsparks.PocketSparks

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.documentfile.provider.DocumentFile
import com.pocketsparks.PocketSparks.database.KeyValueDatabase
import com.pocketsparks.PocketSparks.ui.component.PocketSparksBottomBar
import com.pocketsparks.PocketSparks.ui.component.PocketSparksExplorerDrawer
import com.pocketsparks.PocketSparks.ui.component.PocketSparksSettingsSheet
import com.pocketsparks.PocketSparks.ui.theme.PocketSparksTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

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

    var openedFile by remember { mutableStateOf<DocumentFile?>(null) }
    var isFileOpened by remember { mutableStateOf(false) }
    var fileContent by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(fileContent) {
        coroutineScope.launch {
            fileContent?.let { openedFile?.let { it1 -> saveFileContent(context, it1, it) } }
        }
    }

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
            fileContent?.let { text ->
                TextField(value = text, onValueChange = {
                    fileContent = it
                }, modifier = Modifier.padding(innerPadding))
            }

            if (isExplorerDrawerVisible) {
                PocketSparksExplorerDrawer(settings = settings, onDismissRequest = {
                    isExplorerDrawerVisible = false
                    isBottomBarVisible = true
                }, openFileEditor = { file ->
                    isExplorerDrawerVisible = false
                    isBottomBarVisible = true
                    isFileOpened = true
                    openedFile = file
                    coroutineScope.launch {
                        fileContent = readFileContent(context, file)
                    }
                })
            }

            if (isSettingsSheetVisible) {
                PocketSparksSettingsSheet(onDismissRequest = { isSettingsSheetVisible = false })
            }
        }
    }
}

suspend fun readFileContent(context: Context, file: DocumentFile): String =
    withContext(Dispatchers.IO) {
        return@withContext try {
            val inputStream = context.contentResolver.openInputStream(file.uri)

            if (inputStream != null) {
                val byteArrayOutputStream = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var bytesRead: Int

                while (inputStream.read(buffer).also {
                        bytesRead = it
                    } != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead)
                }

                inputStream.close()
                byteArrayOutputStream.toString()
            } else ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

suspend fun saveFileContent(context: Context, file: DocumentFile, text: String) =
    withContext(Dispatchers.IO) {
        try {
            val outputStream = context.contentResolver.openOutputStream(file.uri, "w")

            if (outputStream != null) {
                outputStream.write(text.toByteArray())
                outputStream.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
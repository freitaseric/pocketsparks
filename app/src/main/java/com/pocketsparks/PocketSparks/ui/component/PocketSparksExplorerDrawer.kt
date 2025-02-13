package com.pocketsparks.PocketSparks.ui.component

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.InsertDriveFile
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import com.pocketsparks.PocketSparks.database.KeyValueDatabase
import kotlinx.coroutines.launch

@Composable
fun PocketSparksExplorerDrawer(
    settings: KeyValueDatabase,
    onDismissRequest: () -> Unit,
    openFileEditor: (file: DocumentFile) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var treeData by remember { mutableStateOf<List<TreeItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val uri = settings.getUri("folderUri")
            if (uri != null) {
                treeData = buildTree(context, uri)
            }
        }
    }

    ModalDrawerSheet {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onDismissRequest) {
                    Icon(Icons.Outlined.Close, contentDescription = "Close Drawer")
                }
            }
            TreeView(data = treeData, openFileEditor)
        }
    }
}

data class TreeItem(
    val name: String,
    val children: List<TreeItem> = emptyList(),
    var isExpanded: MutableState<Boolean> = mutableStateOf(false),
    val file: DocumentFile? = null,
    val isDirectory: Boolean = file?.isDirectory == true,
    val level: Int = 0
)

fun buildTree(context: Context, uri: Uri): List<TreeItem> {
    val folder = DocumentFile.fromTreeUri(context, uri) ?: return emptyList()

    return listOf(buildTreeItem(folder, 0))
}

fun buildTreeItem(file: DocumentFile, level: Int): TreeItem {
    val children = mutableListOf<TreeItem>()

    if (file.isDirectory) {
        val files = file.listFiles()
        for (child in files) {
            children.add(buildTreeItem(child, level + 1))
        }
    }

    return TreeItem(
        file.name!!, children, file = file, level = level
    )
}

@Composable
fun TreeView(data: List<TreeItem>, openFileEditor: (file: DocumentFile) -> Unit) {
    LazyColumn {
        items(data) { item ->
            TreeItemRow(item, openFileEditor)
        }
    }
}

@Composable
fun TreeItemRow(item: TreeItem, openFileEditor: (file: DocumentFile) -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            if (item.isDirectory) {
                item.isExpanded.value = !item.isExpanded.value
            } else {
                if (item.file != null) {
                    openFileEditor(item.file)
                }
            }
        }
        .padding(1.dp), verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width((item.level * 16).dp))

        if (item.isDirectory) {
            Icon(
                imageVector = if (item.isExpanded.value) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (item.isExpanded.value) "Contrair" else "Expandir"
            )
        } else {
            Spacer(modifier = Modifier.width(16.dp))
            Icon(Icons.AutoMirrored.Outlined.InsertDriveFile, contentDescription = "File")
        }

        Text(text = item.name)
    }

    if (item.isExpanded.value) {
        item.children.forEach { child ->
            TreeItemRow(child, openFileEditor)
        }
    }
}
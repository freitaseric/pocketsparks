package com.pocketsparks.PocketSparks.ui.component

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun PocketSparksExplorerDrawer(settings: KeyValueDatabase, onDismissRequest: () -> Unit) {
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
            TextButton(onClick = {
                coroutineScope.launch {
                    settings.delete("folderUri")
                }
            }) {
                Text(text = "Apagar Cache")
            }
            TreeView(data = treeData)
        }
    }
}

data class TreeItem(
    val name: String,
    val children: List<TreeItem> = emptyList(),
    var isExpanded: Boolean = false,
    val file: DocumentFile? = null
)

fun buildTree(context: Context, uri: Uri): List<TreeItem> {
    val folder = DocumentFile.fromTreeUri(context, uri) ?: return emptyList()

    return listOf(buildTreeItem(folder))
}

fun buildTreeItem(file: DocumentFile): TreeItem {
    val children = mutableListOf<TreeItem>()

    if (file.isDirectory) {
        val files = file.listFiles()
        for (child in files) {
            children.add(buildTreeItem(child))
        }
    }

    return TreeItem(
        file.name!!, children, file = file
    )
}

@Composable
fun TreeView(data: List<TreeItem>) {
    LazyColumn {
        items(data) { item ->
            TreeItemRow(item)
        }
    }
}

@Composable
fun TreeItemRow(item: TreeItem) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { item.isExpanded = !item.isExpanded }
        .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (item.isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = if (item.isExpanded) "Contrair" else "Expandir"
        )
        Text(text = item.name)
    }

    if (item.isExpanded) {
        item.children.forEach { child ->
            TreeItemRow(child)
        }
    }
}

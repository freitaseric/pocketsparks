package com.pocketsparks.PocketSparks.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pocketsparks.PocketSparks.R

@Composable
fun PocketSparksBottomBar(onExplorerClick: () -> Unit, onSettingsClick: () -> Unit) {
    BottomAppBar(actions = {
        IconButton(onClick = onExplorerClick) {
            Icon(
                Icons.Outlined.Folder,
                contentDescription = stringResource(R.string.bottomBar_action_edit_description)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { /* do something */ }) {
            Icon(
                Icons.Outlined.Search,
                contentDescription = stringResource(R.string.bottomBar_action_search_description),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onSettingsClick) {
            Icon(
                Icons.Outlined.Settings,
                contentDescription = stringResource(R.string.bottomBar_action_settings_description),
            )
        }
    }, contentPadding = PaddingValues(12.dp, 0.dp))
}
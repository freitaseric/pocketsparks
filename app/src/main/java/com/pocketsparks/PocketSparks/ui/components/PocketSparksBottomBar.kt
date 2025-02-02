package com.pocketsparks.PocketSparks.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pocketsparks.PocketSparks.R

@Composable
fun PocketSparksBottomBar() = BottomAppBar(actions = {
    IconButton(onClick = { /* do something */ }) {
        Icon(
            Icons.Filled.Edit,
            contentDescription = stringResource(R.string.bottomBar_action_edit_description)
        )
    }
    Spacer(modifier = Modifier.weight(1f))
    IconButton(onClick = { /* do something */ }) {
        Icon(
            Icons.Filled.Search,
            contentDescription = stringResource(R.string.bottomBar_action_search_description),
        )
    }
    Spacer(modifier = Modifier.weight(1f))
    IconButton(onClick = { /* do something */ }) {
        Icon(
            Icons.Filled.Settings,
            contentDescription = stringResource(R.string.bottomBar_action_settings_description),
        )
    }
}, contentPadding = PaddingValues(12.dp, 0.dp))
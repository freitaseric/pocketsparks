package com.pocketsparks.PocketSparks

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocketsparks.PocketSparks.ui.theme.PocketSparksTheme

@Preview(showBackground = true)
@Composable
fun PocketSparksApp() = PocketSparksTheme {
    Scaffold(
        bottomBar = {
            BottomAppBar(actions = {
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
        },
    ) { innerPadding ->
        Text(
            modifier = Modifier.padding(innerPadding),
            text = stringResource(R.string.text)
        )
    }
}
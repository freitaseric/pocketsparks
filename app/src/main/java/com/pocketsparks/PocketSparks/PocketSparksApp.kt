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
                        Icons.Filled.Edit, contentDescription = "Open workspace selector menu"
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        Icons.Filled.Search, contentDescription = "Open search modal",
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        Icons.Filled.Settings, contentDescription = "Open settings menu",
                    )
                }
            }, contentPadding = PaddingValues(12.dp, 0.dp))
        },
    ) { innerPadding ->
        Text(
            modifier = Modifier.padding(innerPadding),
            text = "Example of a scaffold with a bottom app bar."
        )
    }
}
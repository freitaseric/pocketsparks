package com.pocketsparks.PocketSparks

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
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

    val settingsSheetState = rememberModalBottomSheetState()
    var isSettingsModalVisible by remember { mutableStateOf(false) }

    PocketSparksTheme {
        Scaffold(
            bottomBar = {
                PocketSparksBottomBar(onSettingsClick = {
                    coroutineScope.launch { settingsSheetState.show() }
                        .invokeOnCompletion { isSettingsModalVisible = true }
                })
            },
        ) { innerPadding ->
            Text(
                modifier = Modifier.padding(innerPadding), text = stringResource(R.string.text)
            )

            if (isSettingsModalVisible) {
                ModalBottomSheet(
                    onDismissRequest = { isSettingsModalVisible = false },
                ) {
                    PocketSparksSettingsSheet()
                }
            }
        }
    }
}

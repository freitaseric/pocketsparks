package com.pocketsparks.PocketSparks

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pocketsparks.PocketSparks.ui.components.PocketSparksBottomBar
import com.pocketsparks.PocketSparks.ui.theme.PocketSparksTheme

@Preview(showBackground = true)
@Composable
fun PocketSparksApp() = PocketSparksTheme {
    Scaffold(
        bottomBar = { PocketSparksBottomBar() },
    ) { innerPadding ->
        Text(
            modifier = Modifier.padding(innerPadding), text = stringResource(R.string.text)
        )
    }
}
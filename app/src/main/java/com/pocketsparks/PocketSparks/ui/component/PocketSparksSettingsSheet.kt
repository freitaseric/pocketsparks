package com.pocketsparks.PocketSparks.ui.component

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pocketsparks.PocketSparks.R
import com.pocketsparks.PocketSparks.ui.theme.spark.SparkDarkColorScheme
import com.pocketsparks.PocketSparks.ui.theme.spark.SparkTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PocketSparksSettingsSheet(onDismissRequest: () -> Unit) {
    val context = LocalContext.current

    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.settingsSheet_title),
                style = SparkTypography.titleLarge
            )
            HorizontalDivider(
                thickness = 2.dp,
                color = SparkDarkColorScheme.surface,
                modifier = Modifier.padding(12.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.settingsSheet_option_language),
                    style = SparkTypography.bodyMedium
                )
                IconButton(onClick = {
                    val intent = Intent(Settings.ACTION_APP_LOCALE_SETTINGS)
                    intent.data = Uri.fromParts("package", context.packageName, null)
                    context.startActivity(intent)
                }) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ExitToApp,
                        contentDescription = stringResource(R.string.settingsSheet_option_language_iconDescription)
                    )
                }
            }
        }
    }
}

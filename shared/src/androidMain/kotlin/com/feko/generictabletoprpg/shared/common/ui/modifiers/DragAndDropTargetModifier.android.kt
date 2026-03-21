package com.feko.generictabletoprpg.shared.common.ui.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.io.files.Path

@Composable
actual fun Modifier.dragAndDropTargetKmp(
    supportedExtensions: Array<String>,
    onFilesDropped: (List<Path>) -> Unit
): Modifier = this

package com.feko.generictabletoprpg.shared.common.ui.modifiers

import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferAction.Companion.Copy
import androidx.compose.ui.draganddrop.DragData
import androidx.compose.ui.draganddrop.dragData
import io.github.vinceglb.filekit.utils.toPath
import kotlinx.io.files.Path
import java.net.URI

@Composable
@OptIn(ExperimentalComposeUiApi::class)
actual fun Modifier.dragAndDropTargetKmp(
    supportedExtensions: Array<String>,
    onFilesDropped: (List<Path>) -> Unit
): Modifier =
    dragAndDropTarget(
        shouldStartDragAndDrop = { event ->
            event.action == Copy
                    && (event.dragData() as? DragData.FilesList)
                ?.readFiles()
                ?.any { fileName ->
                    supportedExtensions.any { fileName.endsWith(it) }
                } == true
        },
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                val files = (event.dragData() as DragData.FilesList)
                    .readFiles()
                    .filter { fileName -> supportedExtensions.any { fileName.endsWith(it) } }
                    .map { URI(it).path.toPath() }
                onFilesDropped(files)
                return true
            }
        }
    )
package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.runtime.Immutable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.broken_file_shortcut_dialog_title
import com.feko.generictabletoprpg.confirm_file_shortcut_removal_dialog_title
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.features.tracker.model.FileShortcutEntry

sealed interface IFileShortcutDialogs {
    @Immutable
    data object None : IFileShortcutDialogs

    @Immutable
    data class ConfirmItemRemovalDialog(
        val fileShortcut: FileShortcutEntry,
        val title: IText = Res.string.confirm_file_shortcut_removal_dialog_title.asText()
    ) : IFileShortcutDialogs

    @Immutable
    data class BrokenFileShortcutDialog(
        val fileShortcut: FileShortcutEntry,
        val title: IText = Res.string.broken_file_shortcut_dialog_title.asText()
    ) : IFileShortcutDialogs
}
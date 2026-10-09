package com.hiosdra.commitmessagecleanerplugin

import com.hiosdra.commitmessagecleanerplugin.settings.CleanerSettings
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.editor.Document

/**
 * Applies a cleaned message back onto the commit-message [Document].
 *
 * Replacing the whole document text in a single write is one undoable step in the commit field, so
 * a stray clean can be reverted with a single Undo. When the text would not change we skip the
 * write entirely (no spurious "modified" state, no empty undo step), and — when the user opted in —
 * we surface a before/after preview.
 */
object CommitMessageUpdater {

    /**
     * @return `true` when the document text actually changed.
     */
    fun apply(document: Document, newText: String, settings: CleanerSettings): Boolean {
        val before = document.text
        if (before == newText) {
            return false
        }

        WriteAction.runAndWait<Throwable> {
            document.setText(newText)
        }

        if (settings.showPreviewNotification) {
            CleanerNotifications.preview(before, newText)
        }
        return true
    }
}

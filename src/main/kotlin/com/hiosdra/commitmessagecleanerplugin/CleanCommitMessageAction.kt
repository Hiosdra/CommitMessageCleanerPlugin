@file:Suppress("UnstableApiUsage")

package com.hiosdra.commitmessagecleanerplugin

import com.hiosdra.commitmessagecleanerplugin.settings.CleanerSettingsState
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vcs.VcsDataKeys

class CleanCommitMessageAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val document = event.getData(VcsDataKeys.COMMIT_MESSAGE_DOCUMENT) ?: return

        val settings = CleanerSettingsState.current()
        val updatedMessage = CommitMessageCleaner.cleanWithTicketNumber(document.text, settings)
        CommitMessageUpdater.apply(document, updatedMessage, settings)
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }
}

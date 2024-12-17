@file:Suppress("UnstableApiUsage")

package com.hiosdra.commitmessagecleanerplugin

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.vcs.VcsDataKeys

class CleanCommitMessageAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val checkinPanel = event.getData(VcsDataKeys.COMMIT_MESSAGE_DOCUMENT) ?: return

        val commitMessage = checkinPanel.text
        val updatedMessage = CommitMessageCleaner.clean(commitMessage)
        WriteAction.runAndWait<Throwable> {
            checkinPanel.setText(updatedMessage)
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }
}

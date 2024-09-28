package com.hiosdra.commitmessagecleanerplugin

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.vcs.VcsDataKeys

class RemoveDashAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        val checkinPanel = e.getData(VcsDataKeys.COMMIT_MESSAGE_DOCUMENT)
        if (project == null || checkinPanel == null) return

        val commitMessage = checkinPanel.text
        val updatedMessage = cleanCommitMessage(commitMessage)
        WriteAction.runAndWait<Throwable> {
            checkinPanel.setText(updatedMessage)
        }
    }

    private fun cleanCommitMessage(commitMessage: @NlsSafe String): String {
        val parts = commitMessage.split("-")
        if (parts.size < 3) return commitMessage // Return original if not enough parts

        val ticketNumber = "${parts[0]}-${parts[1]}"
        val restOfMessage = parts.subList(2, parts.size).joinToString(" ")
        val capitalizedRestOfMessage = restOfMessage.replaceFirstChar { it.uppercase() }

        return "$ticketNumber | $capitalizedRestOfMessage"
    }


    override fun update(e: AnActionEvent) {
        val project = e.project
        val checkinPanel = e.getData(VcsDataKeys.COMMIT_MESSAGE_DOCUMENT)
        e.presentation.isEnabledAndVisible = project != null && checkinPanel != null
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }
}

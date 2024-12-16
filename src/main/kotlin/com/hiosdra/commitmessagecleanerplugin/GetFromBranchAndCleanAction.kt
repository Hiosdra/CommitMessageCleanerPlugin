package com.hiosdra.commitmessagecleanerplugin

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.vcs.VcsDataKeys
import git4idea.GitUtil

class GetFromBranchAndCleanAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val repositories = GitUtil.getRepositories(event.project!!)
        val repository = repositories.firstOrNull() ?: run {
            showNotification("Currently you have no repository on this project. To get the branch name, you need to be on a repository.")
            return
        }
        val branch = repository.currentBranch ?: run {
            showNotification("Currently you are not on a branch. To get the branch name, you need to be on a branch.")
            return
        }
        val branchName = branch.name
        val updatedCommitMessage = CommitMessageCleaner.clean(branchName)

        val checkinPanel = event.getData(VcsDataKeys.COMMIT_MESSAGE_DOCUMENT) ?: return
        WriteAction.runAndWait<Throwable> {
            checkinPanel.setText(updatedCommitMessage)
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    private fun showNotification(content: String) {
        Notifications.Bus.notify(
            Notification(
                "Commit Message Cleaner",
                content,
                NotificationType.WARNING
            )
        )
    }
}

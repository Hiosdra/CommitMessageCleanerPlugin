@file:Suppress("UnstableApiUsage")

package com.hiosdra.commitmessagecleanerplugin

import com.hiosdra.commitmessagecleanerplugin.settings.CleanerSettingsState
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vcs.VcsDataKeys

class GetFromBranchAndCleanAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return

        val changes = event.getData(VcsDataKeys.CHANGES)?.toList()
        val repository = RepositorySelector.select(project, changes) ?: run {
            CleanerNotifications.warn(
                "Currently you have no repository on this project. To get the branch name, you need to be on a repository."
            )
            return
        }
        val branch = repository.currentBranch ?: run {
            CleanerNotifications.warn(
                "Currently you are not on a branch. To get the branch name, you need to be on a branch."
            )
            return
        }
        val branchName = branch.name

        val document = event.getData(VcsDataKeys.COMMIT_MESSAGE_DOCUMENT) ?: return

        val settings = CleanerSettingsState.current()
        val cleanCommitMessage = CommitMessageCleaner.cleanWithTicketFromBranch(branchName, document.text, settings)
        CommitMessageUpdater.apply(document, cleanCommitMessage, settings)
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }
}

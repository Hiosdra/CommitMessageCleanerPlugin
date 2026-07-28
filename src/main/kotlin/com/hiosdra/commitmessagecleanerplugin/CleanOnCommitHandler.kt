@file:Suppress("UnstableApiUsage")

package com.hiosdra.commitmessagecleanerplugin

import com.hiosdra.commitmessagecleanerplugin.settings.CleanerSettings
import com.hiosdra.commitmessagecleanerplugin.settings.CleanerSettingsState
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.checkin.CheckinHandler

/**
 * Cleans the commit message automatically, right before the commit is created, when the user has
 * enabled "clean on commit" in the settings. This removes the reliance on remembering to press the
 * action button, so history stays consistent without any manual step.
 */
class CleanOnCommitHandler(private val panel: CheckinProjectPanel) : CheckinHandler() {

    override fun beforeCheckin(): ReturnResult {
        val settings = CleanerSettingsState.current()
        if (!settings.autoCleanOnCommit) return ReturnResult.COMMIT

        val original = panel.commitMessage
        val cleaned = clean(settings, original)
        if (cleaned != original) {
            panel.setCommitMessage(cleaned)
        }
        return ReturnResult.COMMIT
    }

    private fun clean(settings: CleanerSettings, message: String): String {
        val branchName = RepositorySelector.select(panel.project, panel.selectedChanges)
            ?.currentBranch
            ?.name

        return if (branchName != null) {
            CommitMessageCleaner.cleanWithTicketFromBranch(branchName, message, settings)
        } else {
            CommitMessageCleaner.cleanWithTicketNumber(message, settings)
        }
    }
}

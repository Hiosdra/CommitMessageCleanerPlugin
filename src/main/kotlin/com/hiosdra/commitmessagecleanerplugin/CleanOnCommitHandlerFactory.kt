@file:Suppress("UnstableApiUsage")

package com.hiosdra.commitmessagecleanerplugin

import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.changes.CommitContext
import com.intellij.openapi.vcs.checkin.CheckinHandler
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory

/** Registers [CleanOnCommitHandler] with the VCS commit workflow. */
class CleanOnCommitHandlerFactory : CheckinHandlerFactory() {
    override fun createHandler(panel: CheckinProjectPanel, commitContext: CommitContext): CheckinHandler =
        CleanOnCommitHandler(panel)
}

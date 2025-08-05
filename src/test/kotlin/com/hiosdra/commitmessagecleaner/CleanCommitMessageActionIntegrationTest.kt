package com.hiosdra.commitmessagecleaner

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.testFramework.TestActionEvent
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class CleanCommitMessageActionIntegrationTest : BasePlatformTestCase() {

    fun `test clean commit message action`() {
        // given
        val document: Document = EditorFactory.getInstance().createDocument("ABC-123-implement-new-feature")
        val event: AnActionEvent = TestActionEvent.createTestEvent {
            when (it) {
                VcsDataKeys.COMMIT_MESSAGE_DOCUMENT.name -> document
                else -> null
            }
        }

        // when
        val action: AnAction = CleanCommitMessageAction()
        action.actionPerformed(event)

        // then
        assertEquals("ABC-123 | Implement new feature", document.text)
    }
}

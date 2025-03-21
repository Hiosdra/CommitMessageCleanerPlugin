package com.hiosdra.commitmessagecleanerplugin

import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.testFramework.TestActionEvent
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify

class GetFromBranchAndCleanActionIntegrationTest : BasePlatformTestCase() {

    fun `test get from branch and clean action`() {
        // given
        val branchName = "sample-123-feature-branch"
        val project = mockk<Project>()
        val document: Document = EditorFactory.getInstance().createDocument("Initial commit message")
        val event: AnActionEvent = TestActionEvent.createTestEvent {
            when (it) {
                CommonDataKeys.PROJECT.name -> project
                VcsDataKeys.COMMIT_MESSAGE_DOCUMENT.name -> document
                else -> null
            }
        }

        mockkStatic(Notifications.Bus::class)
        every { Notifications.Bus.notify(any()) } returns Unit

        val repository = mockk<GitRepository>()
        every { repository.currentBranch?.name } returns branchName
        mockkStatic(GitRepositoryManager::class)
        every { GitRepositoryManager.getInstance(event.project!!).repositories } returns listOf(repository)

        // when
        val action: AnAction = GetFromBranchAndCleanAction()
        action.actionPerformed(event)

        //then
        assertEquals(CommitMessageCleaner.cleanWithTicketNumber(branchName), document.text)
        verify(exactly = 0) { Notifications.Bus.notify(any()) }
    }

    fun `test show notification when without repository`() {
        // given
        val project = mockk<Project>()
        val document: Document = EditorFactory.getInstance().createDocument("Initial commit message")
        val event: AnActionEvent = TestActionEvent.createTestEvent {
            when (it) {
                CommonDataKeys.PROJECT.name -> project
                VcsDataKeys.COMMIT_MESSAGE_DOCUMENT.name -> document
                else -> null
            }
        }

        mockkStatic(Notifications.Bus::class)
        every { Notifications.Bus.notify(any()) } returns Unit

        mockkStatic(GitRepositoryManager::class)
        every { GitRepositoryManager.getInstance(event.project!!).repositories } returns emptyList()

        // when
        val action: AnAction = GetFromBranchAndCleanAction()
        action.actionPerformed(event)

        // then
        assertEquals("Initial commit message", document.text)
        verify { Notifications.Bus.notify(any()) }
    }

    fun `test show notification when without branch`() {
        // given
        val project = mockk<Project>()
        val document: Document = EditorFactory.getInstance().createDocument("Initial commit message")
        val event: AnActionEvent = TestActionEvent.createTestEvent {
            when (it) {
                CommonDataKeys.PROJECT.name -> project
                VcsDataKeys.COMMIT_MESSAGE_DOCUMENT.name -> document
                else -> null
            }
        }

        mockkStatic(Notifications.Bus::class)
        every { Notifications.Bus.notify(any()) } returns Unit

        val repository = mockk<GitRepository>()
        every { repository.currentBranch } returns null
        mockkStatic(GitRepositoryManager::class)
        every { GitRepositoryManager.getInstance(event.project!!).repositories } returns listOf(repository)

        // when
        val action: AnAction = GetFromBranchAndCleanAction()
        action.actionPerformed(event)

        // then
        assertEquals("Initial commit message", document.text)
        verify { Notifications.Bus.notify(any()) }
    }
}

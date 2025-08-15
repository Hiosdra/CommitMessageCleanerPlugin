package com.hiosdra.commitmessagecleanerplugin

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
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
import io.mockk.unmockkAll

class GetFromBranchAndCleanActionIntegrationTest : BasePlatformTestCase() {

    override fun tearDown() {
        try {
            unmockkAll()
        } finally {
            super.tearDown()
        }
    }

    fun `test get from branch and clean action`() {
        // given
        val branchName = "sample-123-feature-branch"
        val commitMessage = "Initial commit message"
        val project = mockk<Project>()
        val document: Document = EditorFactory.getInstance().createDocument(commitMessage)
        val event: AnActionEvent = TestActionEvent.createTestEvent {
            when (it) {
                CommonDataKeys.PROJECT.name -> project
                VcsDataKeys.COMMIT_MESSAGE_DOCUMENT.name -> document
                else -> null
            }
        }

        val notifications = mutableListOf<Notification>()
        ApplicationManager.getApplication().messageBus.connect(testRootDisposable)
            .subscribe(Notifications.TOPIC, object : Notifications {
                override fun notify(notification: Notification) {
                    notifications.add(notification)
                }
            })

        val repository = mockk<GitRepository>()
        every { repository.currentBranch?.name } returns branchName
        mockkStatic(GitRepositoryManager::class)
        every { GitRepositoryManager.getInstance(event.project!!).repositories } returns listOf(repository)

        // when
        val action: AnAction = GetFromBranchAndCleanAction()
        action.actionPerformed(event)

        // then
        assertEquals(CommitMessageCleaner.cleanWithTicketFromBranch(branchName, commitMessage), document.text)
        assertTrue("Expected no notifications", notifications.isEmpty())
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

        val notifications = mutableListOf<Notification>()
        ApplicationManager.getApplication().messageBus.connect(testRootDisposable)
            .subscribe(Notifications.TOPIC, object : Notifications {
                override fun notify(notification: Notification) {
                    notifications.add(notification)
                }
            })

        mockkStatic(GitRepositoryManager::class)
        every { GitRepositoryManager.getInstance(event.project!!).repositories } returns emptyList()

        // when
        val action: AnAction = GetFromBranchAndCleanAction()
        action.actionPerformed(event)

        // then
        assertEquals("Initial commit message", document.text)
        assertEquals(1, notifications.size)
        notifications.first().let { n ->
            assertEquals("Commit Message Cleaner", n.groupId)
            assertEquals(NotificationType.WARNING, n.type)
            assertEquals(
                "Currently you have no repository on this project. To get the branch name, you need to be on a repository.",
                n.content
            )
        }
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

        val notifications = mutableListOf<Notification>()
        ApplicationManager.getApplication().messageBus.connect(testRootDisposable)
            .subscribe(Notifications.TOPIC, object : Notifications {
                override fun notify(notification: Notification) {
                    notifications.add(notification)
                }
            })

        val repository = mockk<GitRepository>()
        every { repository.currentBranch } returns null
        mockkStatic(GitRepositoryManager::class)
        every { GitRepositoryManager.getInstance(event.project!!).repositories } returns listOf(repository)

        // when
        val action: AnAction = GetFromBranchAndCleanAction()
        action.actionPerformed(event)

        // then
        assertEquals("Initial commit message", document.text)
        assertEquals(1, notifications.size)
        notifications.first().let { n ->
            assertEquals("Commit Message Cleaner", n.groupId)
            assertEquals(NotificationType.WARNING, n.type)
            assertEquals(
                "Currently you are not on a branch. To get the branch name, you need to be on a branch.",
                n.content
            )
        }
    }
}

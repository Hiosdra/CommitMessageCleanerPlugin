package com.hiosdra.commitmessagecleanerplugin

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
class CommitMessageCleanerTest {

    class CleanWithTicketNumberTest {
        @Test
        fun `should handle empty commit message`() {
            val commitMessage = ""
            val result = CommitMessageCleaner.cleanWithTicketNumber(commitMessage)
            assertEquals(commitMessage, result)
        }

        @Test
        fun `should not modify short message with only ticket`() {
            val commitMessage = "ABC-123"
            val result = CommitMessageCleaner.cleanWithTicketNumber(commitMessage)
            assertEquals(commitMessage, result)
        }

        @Test
        fun `should format standard branch-style message`() {
            val commitMessage = "ABC-123-implement-new-feature"
            val expected = "ABC-123 | Implement new feature"
            val result = CommitMessageCleaner.cleanWithTicketNumber(commitMessage)
            assertEquals(expected, result)
        }

        @Test
        fun `should handle message with underscores`() {
            val commitMessage = "ABC-123_implement_new_feature"
            val expected = "ABC-123 | Implement new feature"
            val result = CommitMessageCleaner.cleanWithTicketNumber(commitMessage)
            assertEquals(expected, result)
        }

        @Test
        fun `should handle lowercase ticket`() {
            val commitMessage = "abc-123-implement-new-feature"
            val expected = "abc-123 | Implement new feature"
            val result = CommitMessageCleaner.cleanWithTicketNumber(commitMessage)
            assertEquals(expected, result)
        }

        @Test
        fun `should preserve uppercase words`() {
            val commitMessage = "AbC-123-implement-NEW-feature"
            val expected = "AbC-123 | Implement NEW feature"
            val result = CommitMessageCleaner.cleanWithTicketNumber(commitMessage)
            assertEquals(expected, result)
        }

        @Test
        fun `should handle message without ticket`() {
            val commitMessage = "implement-new-feature"
            val expected = "Implement new feature"
            val result = CommitMessageCleaner.cleanWithTicketNumber(commitMessage)
            assertEquals(expected, result)
        }
    }

    class GetTicketTest {
        @Test
        fun `should extract ticket from branch name`() {
            val branchName = "ABC-123-implement-new-feature"
            val expected = "ABC-123"
            val result = CommitMessageCleaner.getTicket(branchName)
            assertEquals(expected, result)
        }

        @Test
        fun `should return null for branch without ticket`() {
            val branchName = "implement-new-feature"
            val result = CommitMessageCleaner.getTicket(branchName)
            assertEquals(null, result)
        }

        @Test
        fun `should handle branch with underscores`() {
            val branchName = "ABC-123_implement_new_feature"
            val result = CommitMessageCleaner.getTicket(branchName)
            assertEquals("ABC-123", result)
        }

        @Test
        fun `should handle branch with only ticket`() {
            val branchName = "ABC-123"
            val result = CommitMessageCleaner.getTicket(branchName)
            assertEquals("ABC-123", result)
        }

        @Test
        fun `should handle lowercase ticket in branch`() {
            val branchName = "abc-123-implement-new-feature"
            val result = CommitMessageCleaner.getTicket(branchName)
            assertEquals("abc-123", result)
        }

        @Test
        fun `should handle mixed-case ticket in branch`() {
            val branchName = "AbC-123-implement-new-feature"
            val result = CommitMessageCleaner.getTicket(branchName)
            assertEquals("AbC-123", result)
        }
    }

    class CleanWithTicketFromBranchTest {
        @Test
        fun `should replace ticket in standard message`() {
            val branchName = "ABC-123-some-branch-name"
            val commitMessage = "DEF-456-implement-new-feature"
            val result = CommitMessageCleaner.cleanWithTicketFromBranch(branchName, commitMessage)
            assertEquals("ABC-123 | Implement new feature", result)
        }

        @Test
        fun `should handle short message`() {
            val branchName = "ABC-123-branch"
            val commitMessage = "short-message"
            val result = CommitMessageCleaner.cleanWithTicketFromBranch(branchName, commitMessage)
            assertEquals("ABC-123 | short-message", result)
        }

        @Test
        fun `should fall back when branch has no ticket`() {
            val branchName = "no-valid-ticket-branch"
            val commitMessage = "ABC-123-implement-new-feature"
            val result = CommitMessageCleaner.cleanWithTicketFromBranch(branchName, commitMessage)
            assertEquals("ABC-123 | Implement new feature", result)
        }

        @Test
        fun `should handle message with underscores`() {
            val branchName = "ABC-123-branch"
            val commitMessage = "DEF-456_implement_new_feature"
            val result = CommitMessageCleaner.cleanWithTicketFromBranch(branchName, commitMessage)
            assertEquals("ABC-123 | Implement new feature", result)
        }

        @Test
        fun `should handle empty message`() {
            val branchName = "ABC-123-branch"
            val commitMessage = ""
            val result = CommitMessageCleaner.cleanWithTicketFromBranch(branchName, commitMessage)
            assertEquals("ABC-123 | ", result)
        }

        @Test
        fun `should handle plain text message`() {
            val branchName = "ABC-123"
            val commitMessage = "implement new feature"
            val result = CommitMessageCleaner.cleanWithTicketFromBranch(branchName, commitMessage)
            assertEquals("ABC-123 | implement new feature", result)
        }

        @Test
        fun `should preserve already formatted message with same ticket`() {
            val branchName = "ABC-123-implement-new-feature"
            val commitMessage = "ABC-123 | Implement new feature"
            val result = CommitMessageCleaner.cleanWithTicketFromBranch(branchName, commitMessage)
            assertEquals("ABC-123 | Implement new feature", result)
        }

        @Test
        fun `should replace ticket in already formatted message`() {
            val branchName = "XYZ-456-implement-new-feature"
            val commitMessage = "ABC-123 | Implement new feature"
            val result = CommitMessageCleaner.cleanWithTicketFromBranch(branchName, commitMessage)
            assertEquals("XYZ-456 | Implement new feature", result)
        }

        @Test
        fun `should handle space-separated ticket format`() {
            val branchName = "XYZ-456-implement-new-feature"
            val commitMessage = "ABC-123 Implement new feature"
            val result = CommitMessageCleaner.cleanWithTicketFromBranch(branchName, commitMessage)
            assertEquals("XYZ-456 | Implement new feature", result)
        }
    }
}

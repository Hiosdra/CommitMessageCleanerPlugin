package com.hiosdra.commitmessagecleanerplugin

import org.junit.Assert.assertEquals
import org.junit.Test

class CommitMessageCleanerTest {

    @Test
    fun `should clean with simple commit message`() {
        val commitMessage = "ABC-123-implement-new-feature"
        val expected = "ABC-123 | Implement new feature"
        val result = CommitMessageCleaner.cleanWithTicketNumber(commitMessage)
        assertEquals(expected, result)
    }

    @Test
    fun `should do nothing with only ticket number short commit message`() {
        val commitMessage = "ABC-123"
        val result = CommitMessageCleaner.cleanWithTicketNumber(commitMessage)
        assertEquals(commitMessage, result)
    }

    @Test
    fun `should do nothing with empty commit message`() {
        val commitMessage = ""
        val result = CommitMessageCleaner.cleanWithTicketNumber(commitMessage)
        assertEquals(commitMessage, result)
    }

    @Test
    fun `should clean with commit message containing underscore`() {
        val commitMessage = "ABC-123_implement_new_feature"
        val expected = "ABC-123 | Implement new feature"
        val result = CommitMessageCleaner.cleanWithTicketNumber(commitMessage)
        assertEquals(expected, result)
    }

    @Test
    fun `should clean with lowercase commit message`() {
        val commitMessage = "abc-123-implement-new-feature"
        val expected = "abc-123 | Implement new feature"
        val result = CommitMessageCleaner.cleanWithTicketNumber(commitMessage)
        assertEquals(expected, result)
    }

    @Test
    fun `should handle uppercase words correctly`() {
        val commitMessage = "AbC-123-implement-NEW-feature"
        val expected = "AbC-123 | Implement NEW feature"
        val result = CommitMessageCleaner.cleanWithTicketNumber(commitMessage)
        assertEquals(expected, result)
    }

    @Test
    fun `should clean without ticket number`() {
        val commitMessage = "implement-new-feature"
        val expected = "Implement new feature"
        val result = CommitMessageCleaner.cleanWithTicketNumber(commitMessage)
        assertEquals(expected, result)
    }

    @Test
    fun `should clean with ticket number in branch name`() {
        val branchName = "ABC-123-implement-new-feature"
        val expected = "ABC-123"
        val result = CommitMessageCleaner.getTicket(branchName)
        assertEquals(expected, result)
    }

    @Test
    fun `should do nothing with branch name without ticket number`() {
        val branchName = "implement-new-feature"
        val result = CommitMessageCleaner.getTicket(branchName)
        assertEquals(null, result)
    }

    @Test
    fun `should clean with ticket number and underscore`() {
        val branchName = "ABC-123_implement_new_feature"
        val result = CommitMessageCleaner.getTicket(branchName)
        assertEquals("ABC-123", result)
    }

    @Test
    fun `should clean with only ticket number in branch name`() {
        val branchName = "ABC-123"
        val result = CommitMessageCleaner.getTicket(branchName)
        assertEquals("ABC-123", result)
    }

    @Test
    fun `should handle lowercase ticket number in branch name`() {
        val branchName = "abc-123-implement-new-feature"
        val result = CommitMessageCleaner.getTicket(branchName)
        assertEquals("abc-123", result)
    }

    @Test
    fun `should handle mixedcase ticket number in branch name`() {
        val branchName = "AbC-123-implement-new-feature"
        val result = CommitMessageCleaner.getTicket(branchName)
        assertEquals("AbC-123", result)
    }

    @Test
    fun `should clean with ticket from branch and commit message with sufficient parts`() {
        val branchName = "ABC-123-some-branch-name"
        val commitMessage = "DEF-456-implement-new-feature"
        val result = CommitMessageCleaner.cleanWithTicketFromBranch(branchName, commitMessage)
        assertEquals("ABC-123 | Implement new feature", result)
    }

    @Test
    fun `should handle commit message with insufficient parts when branch has ticket`() {
        val branchName = "ABC-123-branch"
        val commitMessage = "short-message" // Only 2 parts
        val result = CommitMessageCleaner.cleanWithTicketFromBranch(branchName, commitMessage)
        assertEquals("ABC-123 | short-message", result)
    }

    @Test
    fun `should fall back to cleanWithTicketNumber when branch has no valid ticket`() {
        val branchName = "no-valid-ticket-branch"
        val commitMessage = "ABC-123-implement-new-feature"
        val result = CommitMessageCleaner.cleanWithTicketFromBranch(branchName, commitMessage)
        assertEquals("ABC-123 | Implement new feature", result)
    }

    @Test
    fun `should handle commit message with underscores`() {
        val branchName = "ABC-123-branch"
        val commitMessage = "DEF-456_implement_new_feature"
        val expected = "ABC-123 | Implement new feature"
        val result = CommitMessageCleaner.cleanWithTicketFromBranch(branchName, commitMessage)
        assertEquals(expected, result)
    }

    @Test
    fun `should handle empty commit message`() {
        val branchName = "ABC-123-branch"
        val commitMessage = ""
        val expected = "ABC-123 | "
        val result = CommitMessageCleaner.cleanWithTicketFromBranch(branchName, commitMessage)
        assertEquals(expected, result)
    }

    @Test
    fun `should handle when commit message are normal words`() {
        val branchName = "ABC-123"
        val commitMessage = "implement new feature"
        val result = CommitMessageCleaner.cleanWithTicketFromBranch(branchName, commitMessage)
        assertEquals("ABC-123 | implement new feature", result)
    }

    @Test
    fun `should handle multiple already formatted commit messages`() {
        val branchName = "ABC-123-implement-new-feature"
        val commitMessage = "ABC-123 | Implement new feature"
        val result = CommitMessageCleaner.cleanWithTicketFromBranch(branchName, commitMessage)
        assertEquals("ABC-123 | Implement new feature", result)
    }

    @Test
    fun `should handle multiple already formatted commit messages but with other ticket`() {
        val branchName = "XYZ-456-implement-new-feature"
        val commitMessage = "ABC-123 | Implement new feature"
        val result = CommitMessageCleaner.cleanWithTicketFromBranch(branchName, commitMessage)
        assertEquals("XYZ-456 | Implement new feature", result)
    }

    @Test
    fun `should handle partially formatted commit messages but with other ticket`() {
        val branchName = "XYZ-456-implement-new-feature"
        val commitMessage = "ABC-123 Implement new feature"
        val result = CommitMessageCleaner.cleanWithTicketFromBranch(branchName, commitMessage)
        assertEquals("XYZ-456 | Implement new feature", result)
    }
}

package com.hiosdra.commitmessagecleanerplugin

import org.junit.Assert.assertEquals
import org.junit.Test


class CommitMessageCleanerTest {

    @Test
    fun `should clean with simple commit message`() {
        val commitMessage = "ABC-123-implement-new-feature"
        val expected = "ABC-123 | Implement new feature"
        val result = CommitMessageCleaner.clean(commitMessage)
        assertEquals(expected, result)
    }

    @Test
    fun `should do nothing with only ticket number short commit message`() {
        val commitMessage = "ABC-123"
        val result = CommitMessageCleaner.clean(commitMessage)
        assertEquals(commitMessage, result)
    }

    @Test
    fun `should do nothing with empty commit message`() {
        val commitMessage = ""
        val result = CommitMessageCleaner.clean(commitMessage)
        assertEquals(commitMessage, result)
    }

    @Test
    fun `should clean with commit message containing underscore`() {
        val commitMessage = "ABC-123_implement_new_feature"
        val expected = "ABC-123 | Implement new feature"
        val result = CommitMessageCleaner.clean(commitMessage)
        assertEquals(expected, result)
    }

    @Test
    fun `should clean with lowercase commit message`() {
        val commitMessage = "abc-123-implement-new-feature"
        val expected = "abc-123 | Implement new feature"
        val result = CommitMessageCleaner.clean(commitMessage)
        assertEquals(expected, result)
    }

    @Test
    fun `should handle uppercase words correctly`() {
        val commitMessage = "AbC-123-implement-NEW-feature"
        val expected = "AbC-123 | Implement NEW feature"
        val result = CommitMessageCleaner.clean(commitMessage)
        assertEquals(expected, result)
    }
}

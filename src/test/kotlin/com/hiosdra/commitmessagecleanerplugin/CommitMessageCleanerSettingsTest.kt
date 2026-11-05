package com.hiosdra.commitmessagecleanerplugin

import com.hiosdra.commitmessagecleanerplugin.settings.CleanerSettings
import com.hiosdra.commitmessagecleanerplugin.settings.FormatStyle
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
class CommitMessageCleanerSettingsTest {

    class TicketPrefixConfigTest {
        @Test
        fun `should use a custom separator`() {
            val settings = CleanerSettings(separator = ":")
            val result = CommitMessageCleaner.cleanWithTicketNumber("ABC-123-implement-new-feature", settings)
            assertEquals("ABC-123 : Implement new feature", result)
        }

        @Test
        fun `should not capitalize when disabled`() {
            val settings = CleanerSettings(capitalizeFirstLetter = false)
            val result = CommitMessageCleaner.cleanWithTicketNumber("ABC-123-implement-new-feature", settings)
            assertEquals("ABC-123 | implement new feature", result)
        }

        @Test
        fun `should apply custom separator when taking ticket from branch`() {
            val settings = CleanerSettings(separator = "::")
            val result = CommitMessageCleaner.cleanWithTicketFromBranch(
                "ABC-123-branch",
                "DEF-456-implement-new-feature",
                settings,
            )
            assertEquals("ABC-123 :: Implement new feature", result)
        }
    }

    class ConventionalCommitsTest {
        private val settings = CleanerSettings(formatStyle = FormatStyle.CONVENTIONAL_COMMITS)

        @Test
        fun `should format branch-style message with ticket as scope`() {
            val result = CommitMessageCleaner.cleanWithTicketNumber("ABC-123-implement-new-feature", settings)
            assertEquals("feat(ABC-123): implement new feature", result)
        }

        @Test
        fun `should take scope from branch and detect the type`() {
            val result = CommitMessageCleaner.cleanWithTicketFromBranch(
                "ABC-123-login",
                "fix login crash",
                settings,
            )
            assertEquals("fix(ABC-123): login crash", result)
        }

        @Test
        fun `should preserve an already conventional message`() {
            val result = CommitMessageCleaner.cleanWithTicketFromBranch(
                "ABC-123-x",
                "feat(XYZ-1): add thing",
                settings,
            )
            assertEquals("feat(XYZ-1): add thing", result)
        }

        @Test
        fun `should backfill an empty scope from the branch ticket`() {
            val result = CommitMessageCleaner.cleanWithTicketFromBranch(
                "ABC-123-x",
                "feat: add thing",
                settings,
            )
            assertEquals("feat(ABC-123): add thing", result)
        }

        @Test
        fun `should fall back to the default type without scope`() {
            val result = CommitMessageCleaner.cleanWithTicketNumber("implement new feature", settings)
            assertEquals("feat: implement new feature", result)
        }

        @Test
        fun `should honor a custom default type`() {
            val custom = settings.copy(conventionalDefaultType = "chore")
            val result = CommitMessageCleaner.cleanWithTicketNumber("update deps", custom)
            assertEquals("chore: update deps", result)
        }
    }
}

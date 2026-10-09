package com.hiosdra.commitmessagecleanerplugin.settings

/**
 * The output format the cleaner produces.
 */
enum class FormatStyle {
    /** `TICKET-123 | Message` — the classic behavior of this plugin. */
    TICKET_PREFIX,

    /** `type(scope): description` — https://www.conventionalcommits.org */
    CONVENTIONAL_COMMITS,
}

/**
 * Immutable snapshot of the user's cleaning preferences.
 *
 * The [DEFAULT] value reproduces the plugin's original, hard-coded behavior exactly, so existing
 * users see no change until they open the settings panel.
 */
data class CleanerSettings(
    /** Separator placed between the ticket and the message in [FormatStyle.TICKET_PREFIX]. */
    val separator: String = "|",

    /** Regex (without anchors or groups) that recognizes a ticket such as `ABC-123`. */
    val ticketRegex: String = "[A-Za-z]+-\\d+",

    /** Capitalize the first letter of the normalized message. */
    val capitalizeFirstLetter: Boolean = true,

    /** Which output format to produce. */
    val formatStyle: FormatStyle = FormatStyle.TICKET_PREFIX,

    /** Default Conventional Commits type used when none can be detected in the message. */
    val conventionalDefaultType: String = "feat",

    /** Clean the commit message automatically right before a commit is created. */
    val autoCleanOnCommit: Boolean = false,

    /** Show a "before → after" balloon after a manual clean action. */
    val showPreviewNotification: Boolean = false,
) {
    /** The compiled ticket matcher, anchored to the start of the text. */
    val ticketPattern: Regex by lazy { Regex("^($ticketRegex)") }

    companion object {
        val DEFAULT = CleanerSettings()

        /** Conventional Commits types recognized when parsing an existing message. */
        val CONVENTIONAL_TYPES = listOf(
            "feat", "fix", "chore", "docs", "style", "refactor",
            "perf", "test", "build", "ci", "revert",
        )
    }
}

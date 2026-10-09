@file:Suppress("UnstableApiUsage")

package com.hiosdra.commitmessagecleanerplugin

import com.hiosdra.commitmessagecleanerplugin.settings.CleanerSettings
import com.hiosdra.commitmessagecleanerplugin.settings.FormatStyle
import com.intellij.openapi.util.NlsSafe

/**
 * Formats commit messages. The behavior is driven by [CleanerSettings]; calling the methods without
 * a settings argument uses [CleanerSettings.DEFAULT], which reproduces the plugin's original,
 * hard-coded behavior exactly.
 */
object CommitMessageCleaner {

    private val defaultFormatter = Formatter(CleanerSettings.DEFAULT)

    private fun formatterFor(settings: CleanerSettings): Formatter =
        if (settings == CleanerSettings.DEFAULT) defaultFormatter else Formatter(settings)

    fun cleanWithTicketNumber(
        commitMessage: @NlsSafe String,
        settings: CleanerSettings = CleanerSettings.DEFAULT,
    ): String = formatterFor(settings).cleanWithTicketNumber(commitMessage)

    fun getTicket(
        branchName: @NlsSafe String,
        settings: CleanerSettings = CleanerSettings.DEFAULT,
    ): String? = formatterFor(settings).getTicket(branchName)

    fun cleanWithTicketFromBranch(
        branchName: @NlsSafe String,
        commitMessage: @NlsSafe String,
        settings: CleanerSettings = CleanerSettings.DEFAULT,
    ): String = formatterFor(settings).cleanWithTicketFromBranch(branchName, commitMessage)

    /**
     * Stateless formatter bound to a single [CleanerSettings] snapshot. All the compiled patterns
     * that depend on the configured separator or ticket regex are built once here.
     */
    private class Formatter(private val settings: CleanerSettings) {

        private val escapedSeparator = Regex.escape(settings.separator)
        private val ticketAnchored = "^(${settings.ticketRegex})"

        private val formattedMessagePattern = Regex("$ticketAnchored $escapedSeparator .+")
        private val ticketWithMessagePattern = Regex("$ticketAnchored $escapedSeparator (.+)$")
        private val ticketSpaceMessagePattern = Regex("$ticketAnchored\\s+(.+)$")
        private val conventionalPattern = Regex("^([A-Za-z]+)(\\(([^)]*)\\))?(!)?:\\s+(.*)$")

        fun cleanWithTicketNumber(commitMessage: String): String = when (settings.formatStyle) {
            FormatStyle.CONVENTIONAL_COMMITS -> cleanConventional(commitMessage, getTicket(commitMessage))
            FormatStyle.TICKET_PREFIX -> cleanTicketPrefix(commitMessage)
        }

        fun cleanWithTicketFromBranch(branchName: String, commitMessage: String): String {
            val branchTicket = getTicket(branchName)
            return when (settings.formatStyle) {
                FormatStyle.CONVENTIONAL_COMMITS ->
                    cleanConventional(commitMessage, branchTicket ?: getTicket(commitMessage))
                FormatStyle.TICKET_PREFIX ->
                    cleanTicketPrefixFromBranch(branchTicket, commitMessage)
            }
        }

        fun getTicket(branchName: String): String? {
            val parts = splitIntoParts(branchName)
            if (parts.size < 2) return null

            val ticketPrefix = parts[0]
            val ticketNumber = parts[1]

            return if (isValidTicketNumber(ticketNumber)) "$ticketPrefix-$ticketNumber" else null
        }

        // --- TICKET_PREFIX style ------------------------------------------------------------

        private fun cleanTicketPrefix(commitMessage: String): String {
            if (commitMessage.isEmpty() || isAlreadyFormatted(commitMessage)) {
                return commitMessage
            }

            val parts = splitIntoParts(commitMessage)
            if (parts.size < 3) return commitMessage

            val ticketPrefix = parts[0]
            val ticketNumber = parts[1]

            return if (isValidTicketNumber(ticketNumber)) {
                formatWithTicket("$ticketPrefix-$ticketNumber", extractMessageParts(parts, 2))
            } else {
                normalizeText(parts)
            }
        }

        private fun cleanTicketPrefixFromBranch(branchTicket: String?, commitMessage: String): String {
            if (branchTicket == null) return cleanTicketPrefix(commitMessage)

            val formattedMatch = ticketWithMessagePattern.find(commitMessage)
            if (formattedMatch != null) {
                val message = formattedMatch.groupValues.last()
                return formatWithTicket(branchTicket, message)
            }

            val ticketPrefixMatch = ticketSpaceMessagePattern.find(commitMessage)
            if (ticketPrefixMatch != null) {
                val messageContent = ticketPrefixMatch.groupValues.last()
                return formatWithTicket(branchTicket, messageContent)
            }

            val parts = splitIntoParts(commitMessage)
            if (parts.size < 3 || !isValidTicketNumber(parts[1])) {
                return formatWithTicket(branchTicket, commitMessage)
            }

            val messageWords = extractMessageParts(parts, 2)
            return formatWithTicket(branchTicket, normalizeText(messageWords))
        }

        // --- CONVENTIONAL_COMMITS style ----------------------------------------------------

        private fun cleanConventional(commitMessage: String, scopeTicket: String?): String {
            val message = commitMessage.trim()

            conventionalPattern.matchEntire(message)?.let { match ->
                val type = match.groupValues[1]
                val existingScope = match.groupValues[3]
                val bang = match.groupValues[4]
                val description = match.groupValues[5]
                // Already conventional: only backfill an empty scope from the branch ticket.
                return if (existingScope.isBlank() && !scopeTicket.isNullOrBlank()) {
                    "$type($scopeTicket)$bang: $description"
                } else {
                    message
                }
            }

            var body = message
            val ownMatch = settings.ticketPattern.find(body)
            if (ownMatch != null) {
                body = body.substring(ownMatch.range.last + 1)
            }
            val scope = scopeTicket ?: ownMatch?.groupValues?.get(1)

            val tokens = body.split('-', '_', ' ', '\t', '\n')
                .filter { it.isNotBlank() }
                .toMutableList()

            var type = settings.conventionalDefaultType
            if (tokens.isNotEmpty() && tokens.first().lowercase() in CleanerSettings.CONVENTIONAL_TYPES) {
                type = tokens.removeAt(0).lowercase()
            }

            val description = tokens.joinToString(" ")
            val header = if (scope.isNullOrBlank()) type else "$type($scope)"
            return "$header: $description"
        }

        // --- shared helpers ----------------------------------------------------------------

        private fun splitIntoParts(text: String): List<String> = text.split("-", "_")

        private fun extractMessageParts(parts: List<String>, startIndex: Int): List<String> =
            parts.subList(startIndex, parts.size)

        private fun isAlreadyFormatted(message: String): Boolean =
            formattedMessagePattern.matches(message)

        private fun isValidTicketNumber(ticketNumber: String): Boolean =
            ticketNumber.matches(Regex("^\\d+$"))

        private fun normalizeText(words: List<String>): String {
            val joined = words.joinToString(" ")
            return if (settings.capitalizeFirstLetter) {
                joined.replaceFirstChar { it.uppercase() }
            } else {
                joined
            }
        }

        private fun formatWithTicket(ticket: String, message: String): String =
            "$ticket ${settings.separator} $message"

        private fun formatWithTicket(ticket: String, messageParts: List<String>): String =
            formatWithTicket(ticket, normalizeText(messageParts))
    }
}

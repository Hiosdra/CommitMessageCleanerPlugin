@file:Suppress("UnstableApiUsage")

package com.hiosdra.commitmessagecleaner

import com.intellij.openapi.util.NlsSafe

object CommitMessageCleaner {
    private val TICKET_PATTERN = "^([A-Za-z]+-\\d+)".toRegex()
    private val FORMATTED_MESSAGE_PATTERN = "$TICKET_PATTERN \\| .+".toRegex()
    private val TICKET_WITH_MESSAGE_PATTERN = "$TICKET_PATTERN \\| (.+)$".toRegex()
    private val TICKET_SPACE_MESSAGE_PATTERN = "$TICKET_PATTERN\\s+(.+)$".toRegex()

    fun cleanWithTicketNumber(commitMessage: @NlsSafe String): String {
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

    fun getTicket(branchName: @NlsSafe String): String? {
        val parts = splitIntoParts(branchName)
        if (parts.size < 2) return null

        val ticketPrefix = parts[0]
        val ticketNumber = parts[1]

        return if (isValidTicketNumber(ticketNumber)) {
            "$ticketPrefix-$ticketNumber"
        } else {
            null
        }
    }

    fun cleanWithTicketFromBranch(branchName: @NlsSafe String, commitMessage: @NlsSafe String): String {
        val branchTicket = getTicket(branchName) ?: return cleanWithTicketNumber(commitMessage)

        val formattedMatch = TICKET_WITH_MESSAGE_PATTERN.find(commitMessage)
        if (formattedMatch != null) {
            val (_, message) = formattedMatch.destructured
            return formatWithTicket(branchTicket, message)
        }

        val ticketPrefixMatch = TICKET_SPACE_MESSAGE_PATTERN.find(commitMessage)
        if (ticketPrefixMatch != null) {
            val (_, messageContent) = ticketPrefixMatch.destructured
            return formatWithTicket(branchTicket, messageContent)
        }

        val parts = splitIntoParts(commitMessage)
        if (parts.size < 3 || !isValidTicketNumber(parts[1])) {
            return formatWithTicket(branchTicket, commitMessage)
        }

        val messageWords = extractMessageParts(parts, 2)
        return formatWithTicket(branchTicket, normalizeText(messageWords))
    }

    private fun splitIntoParts(text: String): List<String> =
        text.split("-", "_")

    private fun extractMessageParts(parts: List<String>, startIndex: Int): List<String> =
        parts.subList(startIndex, parts.size)

    private fun isAlreadyFormatted(message: String): Boolean =
        FORMATTED_MESSAGE_PATTERN.matches(message)

    private fun isValidTicketNumber(ticketNumber: String): Boolean =
        ticketNumber.matches("^\\d+$".toRegex())

    private fun normalizeText(words: List<String>): String =
        words.joinToString(" ")
            .replaceFirstChar { it.uppercase() }

    private fun formatWithTicket(ticket: String, message: String): String =
        "$ticket | $message"

    private fun formatWithTicket(ticket: String, messageParts: List<String>): String =
        formatWithTicket(ticket, normalizeText(messageParts))
}

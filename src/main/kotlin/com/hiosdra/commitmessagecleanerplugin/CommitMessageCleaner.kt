@file:Suppress("UnstableApiUsage")

package com.hiosdra.commitmessagecleanerplugin

import com.intellij.openapi.util.NlsSafe

object CommitMessageCleaner {
    private val FORMATTED_MESSAGE_PATTERN = "^[A-Za-z]+-\\d+ \\| .+".toRegex()
    private val TICKET_PATTERN = "^([A-Za-z]+-\\d+) \\| (.+)$".toRegex()

    fun cleanWithTicketNumber(commitMessage: @NlsSafe String): String {
        val parts = commitMessage
            .split("-", "_")
        if (parts.size < 3) return commitMessage // Return original if not enough parts

        val ticketPrefix = parts[0]
        val ticketNumber = parts[1]
        val ticket = "$ticketPrefix-$ticketNumber"

        return if (isValidTicketNumber(ticketNumber)) {
            "$ticket | ${normalizeCommitMessage(parts.subList(2, parts.size))}"
        } else {
            normalizeCommitMessage(parts)
        }
    }

    fun getTicket(branchName: @NlsSafe String): String? {
        val parts = branchName
            .split("-", "_")
        if (parts.size < 2) return null // Return null if not enough parts

        val ticketPrefix = parts[0]
        val ticketNumber = parts[1]
        val ticket = "$ticketPrefix-$ticketNumber"

        return if (isValidTicketNumber(ticketNumber)) {
            ticket
        } else {
            null
        }
    }

    fun cleanWithTicketFromBranch(branchName: @NlsSafe String, commitMessage: @NlsSafe String): String {
        val branchTicket = getTicket(branchName) ?: return cleanWithTicketNumber(commitMessage)

        if (isAlreadyFormatted(commitMessage)) {
            val matchResult = TICKET_PATTERN.find(commitMessage)
            if (matchResult != null) {
                val (_, message) = matchResult.destructured
                return "$branchTicket | $message"
            }
        }

        val ticketPrefixMatch = "^([A-Za-z]+-\\d+)\\s+(.+)$".toRegex().find(commitMessage)
        if (ticketPrefixMatch != null) {
            val (_, messageContent) = ticketPrefixMatch.destructured
            return "$branchTicket | $messageContent"
        }

        val parts = commitMessage.split("-", "_")
        if (parts.size < 3) return "$branchTicket | $commitMessage" // Return original if not enough parts

        val ticketNumber = parts[1]
        if (!isValidTicketNumber(ticketNumber)) {
            return "$branchTicket | $commitMessage" // Return original if ticket number is invalid
        }

        val justCommitMessage = parts.subList(2, parts.size).joinToString("-")
        return "$branchTicket | ${normalizeCommitMessage(justCommitMessage.split("-", "_"))}"
    }

    private fun isAlreadyFormatted(commitMessage: String): Boolean {
        return FORMATTED_MESSAGE_PATTERN.matches(commitMessage)
    }

    private fun isValidTicketNumber(ticketNumber: String) = ticketNumber.matches("^\\d+$".toRegex())

    private fun normalizeCommitMessage(words: List<String>) =
        words.joinToString(" ")
            .replaceFirstChar { it.uppercase() }
}

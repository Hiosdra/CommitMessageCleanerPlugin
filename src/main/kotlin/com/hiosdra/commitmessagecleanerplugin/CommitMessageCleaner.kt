package com.hiosdra.commitmessagecleanerplugin

import com.intellij.openapi.util.NlsSafe

object CommitMessageCleaner {
    fun clean(commitMessage: @NlsSafe String): String {
        val parts = commitMessage
            .split("-")
            .flatMap { it.split("_") }
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

    private fun isValidTicketNumber(ticketNumber: String) = ticketNumber.matches("^\\d+$".toRegex())

    private fun normalizeCommitMessage(words: List<String>) =
        words.joinToString(" ")
            .replaceFirstChar { it.uppercase() }
}

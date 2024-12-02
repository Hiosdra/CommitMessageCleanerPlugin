package com.hiosdra.commitmessagecleanerplugin

import com.intellij.openapi.util.NlsSafe

object CommitMessageCleaner {
    fun clean(commitMessage: @NlsSafe String): String {
        val parts = commitMessage
            .split("-")
            .flatMap { it.split("_") }
        if (parts.size < 3) return commitMessage // Return original if not enough parts

        val ticketNumber = "${parts[0]}-${parts[1]}"
        val restOfMessage = parts.subList(2, parts.size).joinToString(" ")
        val capitalizedRestOfMessage = restOfMessage.replaceFirstChar { it.uppercase() }

        return "$ticketNumber | $capitalizedRestOfMessage"
    }
}

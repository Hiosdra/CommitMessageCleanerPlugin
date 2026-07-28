package com.hiosdra.commitmessagecleanerplugin

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications

/**
 * Central place for the plugin's balloon notifications. The group id must match the
 * `notificationGroup` declared in `plugin.xml`.
 */
object CleanerNotifications {

    private const val GROUP_ID = "Commit Message Cleaner"

    fun warn(content: String) = notify(content, NotificationType.WARNING)

    private fun info(content: String) = notify(content, NotificationType.INFORMATION)

    /** Shows a "before → after" preview of a cleaning operation. */
    fun preview(before: String, after: String) =
        info("Cleaned commit message:\n$before\n→\n$after")

    private fun notify(content: String, type: NotificationType) {
        Notifications.Bus.notify(Notification(GROUP_ID, content, type))
    }
}

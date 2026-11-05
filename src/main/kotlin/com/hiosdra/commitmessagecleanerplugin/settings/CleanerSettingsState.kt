package com.hiosdra.commitmessagecleanerplugin.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 * Application-level, IDE-wide persistence for [CleanerSettings].
 *
 * Formatting style is a personal preference rather than a per-project one, so it lives at the
 * application level and follows the user across every project.
 */
@Service(Service.Level.APP)
@State(
    name = "com.hiosdra.commitmessagecleanerplugin.CleanerSettings",
    storages = [Storage("CommitMessageCleaner.xml")],
)
class CleanerSettingsState : PersistentStateComponent<CleanerSettingsState.State> {

    /** Mutable, serializable mirror of [CleanerSettings]. */
    class State {
        var separator: String = CleanerSettings.DEFAULT.separator
        var ticketRegex: String = CleanerSettings.DEFAULT.ticketRegex
        var capitalizeFirstLetter: Boolean = CleanerSettings.DEFAULT.capitalizeFirstLetter
        var formatStyle: FormatStyle = CleanerSettings.DEFAULT.formatStyle
        var conventionalDefaultType: String = CleanerSettings.DEFAULT.conventionalDefaultType
        var autoCleanOnCommit: Boolean = CleanerSettings.DEFAULT.autoCleanOnCommit
        var showPreviewNotification: Boolean = CleanerSettings.DEFAULT.showPreviewNotification
    }

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    /** Read the current preferences as an immutable snapshot. */
    fun toSettings(): CleanerSettings = CleanerSettings(
        separator = state.separator,
        ticketRegex = state.ticketRegex.ifBlank { CleanerSettings.DEFAULT.ticketRegex },
        capitalizeFirstLetter = state.capitalizeFirstLetter,
        formatStyle = state.formatStyle,
        conventionalDefaultType = state.conventionalDefaultType.ifBlank { CleanerSettings.DEFAULT.conventionalDefaultType },
        autoCleanOnCommit = state.autoCleanOnCommit,
        showPreviewNotification = state.showPreviewNotification,
    )

    /** Overwrite the stored preferences from an immutable snapshot. */
    fun fromSettings(settings: CleanerSettings) {
        state.separator = settings.separator
        state.ticketRegex = settings.ticketRegex
        state.capitalizeFirstLetter = settings.capitalizeFirstLetter
        state.formatStyle = settings.formatStyle
        state.conventionalDefaultType = settings.conventionalDefaultType
        state.autoCleanOnCommit = settings.autoCleanOnCommit
        state.showPreviewNotification = settings.showPreviewNotification
    }

    companion object {
        fun getInstance(): CleanerSettingsState =
            ApplicationManager.getApplication().getService(CleanerSettingsState::class.java)

        /** Convenience accessor for the current snapshot. */
        fun current(): CleanerSettings = getInstance().toSettings()
    }
}

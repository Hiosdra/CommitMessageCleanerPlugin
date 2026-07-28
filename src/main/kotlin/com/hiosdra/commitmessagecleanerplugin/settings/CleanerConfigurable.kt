package com.hiosdra.commitmessagecleanerplugin.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.toNullableProperty
import javax.swing.JComponent

/**
 * Settings panel under `Settings/Preferences | Tools | Commit Message Cleaner`.
 *
 * Lets the user tailor the plugin to their team's convention instead of the single hard-coded style,
 * so it stays useful beyond Jira-style `PREFIX-123 | Message` commit messages.
 */
class CleanerConfigurable : Configurable {

    /** Mutable mirror the UI binds to; seeded from and written back to the persistent state. */
    private class Model(source: CleanerSettings) {
        var separator = source.separator
        var ticketRegex = source.ticketRegex
        var capitalizeFirstLetter = source.capitalizeFirstLetter
        var formatStyle = source.formatStyle
        var conventionalDefaultType = source.conventionalDefaultType
        var autoCleanOnCommit = source.autoCleanOnCommit
        var showPreviewNotification = source.showPreviewNotification

        fun toSettings() = CleanerSettings(
            separator = separator,
            ticketRegex = ticketRegex,
            capitalizeFirstLetter = capitalizeFirstLetter,
            formatStyle = formatStyle,
            conventionalDefaultType = conventionalDefaultType,
            autoCleanOnCommit = autoCleanOnCommit,
            showPreviewNotification = showPreviewNotification,
        )
    }

    private val model = Model(CleanerSettingsState.current())
    private var dialogPanel: DialogPanel? = null

    override fun getDisplayName(): String = "Commit Message Cleaner"

    override fun createComponent(): JComponent {
        val builtPanel = panel {
            group("Format") {
                row("Style:") {
                    comboBox(
                        FormatStyle.entries.toList(),
                        SimpleListCellRenderer.create("") { style -> style?.let(::styleLabel) },
                    ).bindItem(model::formatStyle.toNullableProperty())
                }
                row("Separator:") {
                    textField()
                        .bindText(model::separator)
                        .comment("Placed between the ticket and the message, e.g. <code>ABC-123 | Message</code>.")
                }
                row {
                    checkBox("Capitalize the first letter of the message")
                        .bindSelected(model::capitalizeFirstLetter)
                }
                row("Default Conventional Commits type:") {
                    textField()
                        .bindText(model::conventionalDefaultType)
                        .comment("Used only in the Conventional Commits style when no type is detected.")
                }
            }
            group("Ticket Detection") {
                row("Ticket regex:") {
                    textField()
                        .bindText(model::ticketRegex)
                        .comment("Regex that matches a ticket such as <code>ABC-123</code>.")
                }
            }
            group("Behavior") {
                row {
                    checkBox("Clean the commit message automatically before committing")
                        .bindSelected(model::autoCleanOnCommit)
                }
                row {
                    checkBox("Show a before/after preview notification after cleaning")
                        .bindSelected(model::showPreviewNotification)
                }
            }
        }
        dialogPanel = builtPanel
        return builtPanel
    }

    override fun isModified(): Boolean {
        val panel = dialogPanel ?: return false
        panel.apply()
        return model.toSettings() != CleanerSettingsState.current()
    }

    override fun apply() {
        dialogPanel?.apply()
        CleanerSettingsState.getInstance().fromSettings(model.toSettings())
    }

    override fun reset() {
        val current = CleanerSettingsState.current()
        model.separator = current.separator
        model.ticketRegex = current.ticketRegex
        model.capitalizeFirstLetter = current.capitalizeFirstLetter
        model.formatStyle = current.formatStyle
        model.conventionalDefaultType = current.conventionalDefaultType
        model.autoCleanOnCommit = current.autoCleanOnCommit
        model.showPreviewNotification = current.showPreviewNotification
        dialogPanel?.reset()
    }

    override fun disposeUIResources() {
        dialogPanel = null
    }

    private fun styleLabel(style: FormatStyle): String = when (style) {
        FormatStyle.TICKET_PREFIX -> "Ticket prefix  (ABC-123 | Message)"
        FormatStyle.CONVENTIONAL_COMMITS -> "Conventional Commits  (feat(ABC-123): message)"
    }
}

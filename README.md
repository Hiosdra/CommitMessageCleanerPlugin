# CommitMessageCleanerPlugin

[![Build](https://github.com/Hiosdra/CommitMessageCleanerPlugin/actions/workflows/build.yml/badge.svg)](https://github.com/Hiosdra/CommitMessageCleanerPlugin/actions/workflows/build.yml)
[![Version](https://img.shields.io/jetbrains/plugin/v/25991.svg)](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/25991.svg)](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID)

<!-- Plugin description -->
<h2>Commit Message Cleaner</h2>
<p>
  This plugin provides two actions for cleaning commit messages in IntelliJ IDEA.
  <br />
  The <code>Clean Commit Message</code> action processes the commit message by removing hyphens (\-\) and underscores (\_) and formats it with a ticket prefix if detected.
  <br />
  The <code>Get Ticket From Branch And Clean</code> action uses the current Git branch name to extract a ticket identifier and applies it to the commit message.
</p>
<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "CommitMessageCleanerPlugin"</kbd> >
  <kbd>Install</kbd>
  
- Using JetBrains Marketplace:

  Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID) and install it by clicking the <kbd>Install to ...</kbd> button in case your IDE is running.

  You can also download the [latest release](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID/versions) from JetBrains Marketplace and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manually:

  Download the [latest release](https://github.com/Hiosdra/CommitMessageCleanerPlugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template]. Current template version base: `2.0.2`

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation

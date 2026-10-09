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
<p>Both actions are configurable under <code>Settings/Preferences | Tools | Commit Message Cleaner</code>:</p>
<ul>
  <li><b>Format style</b> — the classic <code>ABC-123 | Message</code> ticket-prefix style, or <b>Conventional Commits</b> (<code>feat(ABC-123): message</code>).</li>
  <li><b>Separator</b> and <b>capitalization</b> of the message.</li>
  <li><b>Ticket regex</b> so teams that don't use Jira-style ids are supported too.</li>
  <li><b>Clean on commit</b> — clean the message automatically right before committing, so it never gets forgotten.</li>
  <li><b>Preview notification</b> — an optional before/after balloon after each clean.</li>
</ul>
<p>
  In multi-repository (monorepo) projects the ticket is taken from the repository that owns the files
  being committed, and both actions are also available via keyboard shortcuts and the <code>Git</code> menu.
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
Plugin based on the [IntelliJ Platform Plugin Template][template]. Current template version base: `2.6.0`

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation

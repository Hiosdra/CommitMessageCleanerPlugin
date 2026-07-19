<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# CommitMessageCleanerPlugin Changelog

## [Unreleased]

### Changed

- Sync scaffolding with the IntelliJ Platform Plugin Template `2.6.0` base (was `2.0.2`):
  upgrade Gradle Wrapper to `9.5.0`, bump `org.jetbrains.intellij.platform` to `2.16.0`,
  move IntelliJ Platform/Maven repositories to `settings.gradle.kts`, drop the now-redundant
  `signing`/`publishing`/`pluginVerification` blocks in `build.gradle.kts` (defaulted since
  IntelliJ Platform Gradle Plugin `2.14.0`), update GitHub Actions (`actions/checkout`,
  `gradle/actions/setup-gradle`, `actions/upload-artifact`) and sandbox paths in `.run/*.xml`
  and `.idea/gradle.xml` to match the plugin's `.intellijPlatform/sandbox` layout.

### Fixed

- `release.yml`: fix the `Create Pull Request` step condition, which referenced a
  non-existent `steps.properties.outputs.changelog` and never actually ran.
- `build.yml`: handle an empty release-drafts list in the cleanup step with `xargs -r`,
  and add the missing Java/Gradle setup to the `releaseDraft` job.

## [0.2.3] - 2025-08-07

**Full Changelog**: https://github.com/Hiosdra/CommitMessageCleanerPlugin/compare/0.2.2...0.2.3

## [0.2.1] - 2025-08-05

- Bump io.mockk:mockk from 1.13.17 to 1.14.2 by @dependabot[bot] in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/53
- Bump org.gradle.toolchains.foojay-resolver-convention from 0.9.0 to 1.0.0 by @dependabot[bot] in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/58
- Changelog update - `0.2.0` by @github-actions[bot] in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/47
- Bump JetBrains/qodana-action from 2024.3.4 to 2025.1.1 by @dependabot[bot] in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/54
- Bump org.jetbrains.qodana from 2024.3.4 to 2025.1.1 by @dependabot[bot] in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/55
- Bump org.jetbrains.kotlin.jvm from 2.1.20 to 2.1.21 by @dependabot[bot] in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/56
- Initialize JetBrains Junie 🚀 by @jetbrains-junie[bot] in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/59
- Bump io.mockk:mockk from 1.14.2 to 1.14.4 by @dependabot[bot] in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/60
- Bump org.jetbrains.kotlin.jvm from 2.1.21 to 2.2.0 by @dependabot[bot] in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/61
- @jetbrains-junie[bot] made their first contribution in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/59

## [0.2.0] - 2025-03-21

- Changelog update - `0.1.1` by @github-actions in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/38
- Bump io.mockk:mockk from 1.13.14 to 1.13.16 by @dependabot in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/35
- Bump org.jetbrains.kotlin.jvm from 2.1.0 to 2.1.10 by @dependabot in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/39
- Bump io.mockk:mockk from 1.13.16 to 1.13.17 by @dependabot in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/41
- Bump org.jetbrains.intellij.platform from 2.2.1 to 2.3.0 by @dependabot in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/40
- Bump org.jetbrains.kotlin.jvm from 2.1.10 to 2.1.20 by @dependabot in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/43
- Bump org.jetbrains.intellij.platform from 2.3.0 to 2.4.0 by @dependabot in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/42
- Copilot friendly cleaning by @Hiosdra in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/44
- Update intellij platform minimum & 0.2.0 by @Hiosdra in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/46

## [0.1.1] - 2025-01-20

- Changelog update - `0.1.0` by @github-actions in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/30
- Bump org.jetbrains.qodana from 2024.3.3 to 2024.3.4 by @dependabot in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/33
- Bump io.mockk:mockk from 1.13.13 to 1.13.14 by @dependabot in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/32
- Bump JetBrains/qodana-action from 2024.3.3 to 2024.3.4 by @dependabot in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/31
- Bump org.jetbrains.kotlinx.kover from 0.9.0 to 0.9.1 by @dependabot in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/34
- **Tested & updated to Intellij 2025.01 EAP by @Hiosdra in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/36**
- v0.1.1 by @Hiosdra in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/37
- @github-actions made their first contribution in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/30

## [0.1.0] - 2024-12-20

- Changelog update - 0.0.3 by @Hiosdra in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/18
- Bump org.jetbrains.intellij.platform from 2.1.0 to 2.2.0 by @dependabot in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/19
- Get from branch action by @Hiosdra in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/24
- Bump org.jetbrains.intellij.platform from 2.2.0 to 2.2.1 by @dependabot in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/23
- Bump io.mockk:mockk from 1.13.3 to 1.13.13 by @dependabot in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/25
- Bump org.jetbrains.kotlinx.kover from 0.8.3 to 0.9.0 by @dependabot in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/22
- Bump org.jetbrains.qodana from 2024.2.6 to 2024.3.3 by @dependabot in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/26
- Bump JetBrains/qodana-action from 2024.2.6 to 2024.3.2 by @dependabot in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/20
- Bump JetBrains/qodana-action from 2024.3.2 to 2024.3.3 by @dependabot in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/27
- Repair codeql workflow by @Hiosdra in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/28
- 0.1.0 release by @Hiosdra in https://github.com/Hiosdra/CommitMessageCleanerPlugin/pull/29

## [0.0.3] - 2024-12-03

- Enable plugin signing
- Proper versioning from now on

[Unreleased]: https://github.com/Hiosdra/CommitMessageCleanerPlugin/compare/v0.2.3...HEAD
[0.2.3]: https://github.com/Hiosdra/CommitMessageCleanerPlugin/compare/v0.2.1...v0.2.3
[0.2.1]: https://github.com/Hiosdra/CommitMessageCleanerPlugin/compare/v0.2.0...v0.2.1
[0.2.0]: https://github.com/Hiosdra/CommitMessageCleanerPlugin/compare/v0.1.1...v0.2.0
[0.1.1]: https://github.com/Hiosdra/CommitMessageCleanerPlugin/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/Hiosdra/CommitMessageCleanerPlugin/compare/v0.0.3...v0.1.0
[0.0.3]: https://github.com/Hiosdra/CommitMessageCleanerPlugin/commits/v0.0.3

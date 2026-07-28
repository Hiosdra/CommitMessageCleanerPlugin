package com.hiosdra.commitmessagecleanerplugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.Change
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryManager

/**
 * Picks the Git repository whose branch should provide the ticket.
 *
 * In a single-repository project this is trivial, but in a monorepo / multi-module setup the first
 * repository is not necessarily the one being committed to. When the set of changes is known, we
 * resolve the repository from the files actually included in the commit, and only fall back to the
 * first repository when nothing better can be determined.
 */
object RepositorySelector {

    /**
     * @param changes the changes included in the current commit, if available.
     * @return the best matching repository, or `null` when the project has no Git repositories.
     */
    fun select(project: Project, changes: Collection<Change>?): GitRepository? {
        val manager = GitRepositoryManager.getInstance(project)
        val repositories = manager.repositories
        if (repositories.size <= 1) return repositories.firstOrNull()

        val fromChanges = changes
            ?.asSequence()
            ?.mapNotNull { change -> change.virtualFile ?: change.beforeRevision?.file?.virtualFile }
            ?.mapNotNull { file -> manager.getRepositoryForFileQuick(file) }
            ?.firstOrNull()

        return fromChanges ?: repositories.first()
    }
}

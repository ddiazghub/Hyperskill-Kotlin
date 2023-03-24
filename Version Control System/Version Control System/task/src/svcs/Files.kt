package svcs

import java.nio.file.Path
import java.security.MessageDigest
import kotlin.io.path.*

val CONFIG_FILE = Path("vcs", "config.txt")
val INDEX_FILE = Path("vcs", "index.txt")
val LOG_FILE = Path("vcs", "log.txt")
val VCS_ROOT = Path("vcs")
val COMMIT_DIR = Path("vcs", "commits")
val DIRS = arrayOf(VCS_ROOT, COMMIT_DIR)
val FILES = arrayOf(CONFIG_FILE, INDEX_FILE, LOG_FILE)

fun getLastCommits(): List<String> {
    val commits = COMMIT_DIR.listDirectoryEntries()
        .sortedBy(Path::getLastModifiedTime)
        .map { it.fileName.name }

    return when (commits.size) {
        0 -> emptyList()
        1 -> commits.subList(0, 1)
        else -> commits.subList(commits.size - 2, commits.size)
    }
}

fun Path.treeHash(digest: MessageDigest) {
    if (this.exists()) {
        val filename = this.fileName.name.toByteArray()

        if (this.isDirectory()) {
            digest.update(filename)

            for (file in this.listDirectoryEntries())
                file.treeHash(digest)
        } else digest.update(filename + this.readBytes())
    }
}

fun Path.treeCopy(destination: Path) {
    if (this.exists()) {
        this.toFile()
            .copyRecursively(destination.toFile(), true)
    }
}
package svcs

import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun ByteArray.toHex(): String {
    return this.joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }
}

fun hasChanges(lastCommits: List<String>): Boolean {
    val lastCommit = when (lastCommits.size) {
        0 -> return true
        1 -> ""
        else -> lastCommits[1]
    }

    val hashed = hashCommit(lastCommit)

    return !MessageDigest.isEqual(hashed.toByteArray(), lastCommits[0].toByteArray())
}

fun hashCommit(lastCommit: String): String {
    val digest = MessageDigest.getInstance("SHA-1")
    digest.update(lastCommit.toByteArray())

    for (path in INDEX_FILE.readLines()) {
        Path(path).treeHash(digest)
    }

    return digest.digest().toHex()
}
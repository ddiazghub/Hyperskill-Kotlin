package svcs

import kotlin.io.path.*

class Commands {
    companion object {
        val COMMANDS = Command.values()
            .associateBy { it.commandName }

        fun run(args: Array<String>): String {
            return when (val arg = args.getOrNull(0)) {
                "--help", null -> help()
                else -> when (val command = COMMANDS[arg]) {
                    null -> "'${arg}' is not a SVCS command."
                    else -> command.handler(args)
                }
            }
        }

        fun help(): String {
            return "These are SVCS commands:\n" + COMMANDS.values.joinToString("\n")
        }

        fun config(args: Array<String>): String {
            return when (val name = args.getOrNull(1))  {
                null -> when (val username = CONFIG_FILE.readText()) {
                    "" -> "Please, tell me who you are."
                    else -> "The username is $username."
                } else -> {
                    CONFIG_FILE.writeText("$name")
                    "The username is $name."
                }
            }
        }

        fun add(args: Array<String>): String {
            return when (val filename = args.getOrNull(1))  {
                null -> when (val tracked = INDEX_FILE.readText()) {
                    "" -> Command.Add.description
                    else -> "Tracked files:\n$tracked"
                } else -> {
                    if (Path(filename).exists()) {
                        INDEX_FILE.appendText("$filename\n")
                        "The file '$filename' is tracked."
                    } else "Can't find '$filename'."
                }
            }
        }

        fun log(args: Array<String>): String {
            return when (val commits = LOG_FILE.readText()) {
                "" -> "No commits yet."
                else -> commits.split("\n\n")
                    .asReversed()
                    .joinToString("\n\n")
                    .trim()
            }
        }

        fun commit(args: Array<String>): String {
            val lastCommits = getLastCommits()

            return if (hasChanges(lastCommits)) {
                val author = CONFIG_FILE.readText()
                val description = args.getOrNull(1) ?: return "Message was not passed."
                val hash = hashCommit(lastCommits.lastOrNull() ?: "")
                doCommit(hash)
                LOG_FILE.appendText("commit $hash\nAuthor: $author\n$description\n\n")
                "Changes are committed."
            } else "Nothing to commit."
        }

        fun doCommit(hash: String) {
            val commitDir = COMMIT_DIR.resolve(hash)
            commitDir.createDirectory()

            for (file in INDEX_FILE.readLines())
                Path(file).treeCopy(commitDir.resolve(file))
        }

        fun checkout(args: Array<String>): String {
            return when (val commit = args.getOrNull(1))  {
                null -> "Commit id was not passed."
                else -> {
                    val commitDir = COMMIT_DIR.resolve(commit)

                    if (commitDir.exists()) {
                        for (file in INDEX_FILE.readLines()) {
                            val fileInCommit = commitDir.resolve(file)
                            val trackedFile = VCS_ROOT.resolve("..").resolve(file)

                            if (fileInCommit.exists())
                                fileInCommit.treeCopy(trackedFile)
                            else
                                trackedFile.toFile().deleteRecursively()
                        }


                        "Switched to commit $commit."
                    } else "Commit does not exist."
                }
            }
        }
    }
}

enum class Command(val commandName: String, val description: String, val handler: (Array<String>) -> String) {
    Config("config", "Get and set a username.", Commands::config),
    Add("add", "Add a file to the index.", Commands::add),
    Log("log", "Show commit logs.", Commands::log),
    Commit("commit", "Save changes.", Commands::commit),
    Checkout("checkout","Restore a file.", Commands::checkout);

    override fun toString(): String {
        return this.commandName.padEnd(11, ' ') + this.description
    }
}

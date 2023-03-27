package wordsvirtuoso

import kotlin.io.path.Path
import kotlin.io.path.exists

fun main(args: Array<String>) {
    try {
        val conditions = arrayOf(
            { args.size == 2 } to { VirtuosoException.WrongArgs },
            { Path(args[0]).exists() } to { VirtuosoException.NoSuchWordsFile(args[0]) },
            { Path(args[1]).exists() } to { VirtuosoException.NoSuchCandidatesFile(args[1]) }
        )

        for ((condition, exception) in conditions)
            if (!condition())
                throw exception()

        val words = Dictionary(args[0])
        val candidates = Dictionary(args[1])
        val included = candidates.words.count { it in words.words }

        if (included < candidates.words.size)
            throw VirtuosoException.CandidatesNotInWords(args[0], candidates.words.size - included)

        val game = WordsVirtuoso(words, candidates)
        game.start()
    } catch (e: VirtuosoException) {
        println("Error: ${e.message}")
    }
}

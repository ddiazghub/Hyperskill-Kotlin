data class Article(val name: String, val pages: Int, val author: String)

fun getArticleByName(articles: MutableList<Article>, name: String): Article? {
    var index = -1
    for ((title, pages, author) in articles) {
        index += 1
        if (title == name) return articles[index]
    }
    return null
}
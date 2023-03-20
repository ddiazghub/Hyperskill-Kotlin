val VOWELS = setOf('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U')

fun countVowels(charSequence: CharSequence): Int = charSequence.count { it in VOWELS }
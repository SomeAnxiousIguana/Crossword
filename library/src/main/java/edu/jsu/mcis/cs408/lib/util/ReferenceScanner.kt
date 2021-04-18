package edu.jsu.mcis.cs408.crossword.lib.util

import edu.jsu.mcis.cs408.crossword.lib.core.Crossword

import java.util.ArrayList
import java.util.regex.Pattern


object ReferenceScanner {

    private const val WORD_ACROSS_EN = "Across"
    private const val WORD_DOWN_EN = "Down"

    private val NUMBER_FINDER = Pattern.compile("\\b(\\d{1,3})\\b")
    private val DIRECTION_FINDER_EN = Pattern.compile("((?:\\b$WORD_ACROSS_EN\\b)|(?:\\b$WORD_DOWN_EN\\b))",
            Pattern.CASE_INSENSITIVE)

    data class WordReference(val number: Int = 0,
                             val direction: Int = 0,
                             val start: Int = 0,
                             val end: Int = 0) {

        override fun toString(): String = when (direction) {
            Crossword.Word.DIR_ACROSS -> "$number Across [$start..$end]"
            Crossword.Word.DIR_DOWN -> "$number Down [$start..$end]"
            else -> "??"
        }
    }

    @JvmStatic
    fun findReferences(hint: String, crossword: Crossword): List<WordReference> {
        val refs = ArrayList<WordReference>()

        val m = NUMBER_FINDER.matcher(hint)
        while (m.find()) {
            // Find any numbers
            val number = m.group(1).toInt()
            val start = m.start(1)
            val end = m.end(1)

            // Find closest directional marker
            val m2 = DIRECTION_FINDER_EN.matcher(hint)
            if (m2.find(end)) {
                val dir = when {
                    WORD_ACROSS_EN.equals(m2.group(1), ignoreCase = true) -> Crossword.Word.DIR_ACROSS
                    else -> Crossword.Word.DIR_DOWN
                }

                // Confirm that the word exists in the crossword
                crossword.findWord(dir, number)?.let {
                    // It exists, add the reference
                    refs += WordReference(
                            number = number,
                            direction = dir,
                            start = start,
                            end = end)
                }
            }
        }

        return refs
    }
}

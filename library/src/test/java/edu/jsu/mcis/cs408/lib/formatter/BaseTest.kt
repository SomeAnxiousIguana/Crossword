package edu.jsu.mcis.cs408.crossword.lib.formatter

import edu.jsu.mcis.cs408.crossword.lib.formatter.Metadata
import edu.jsu.mcis.cs408.crossword.lib.formatter.Proof
import edu.jsu.mcis.cs408.crossword.lib.core.Crossword
import edu.jsu.mcis.cs408.crossword.lib.core.buildCrossword
import edu.jsu.mcis.cs408.crossword.lib.io.CrosswordFormatter
import org.junit.Assert

import java.io.File


open class BaseTest {

    val root = File("src/test/res")

    protected fun CrosswordFormatter.load(path: String): Crossword =
            File(root, path).inputStream().use { s -> buildCrossword { read(this, s) } }

    protected fun assertMetadata(actual: Crossword, expected: Metadata) {
        Assert.assertEquals("Width mismatch!", expected.width, actual.width)
        Assert.assertEquals("Height mismatch!", expected.height, actual.height)
        Assert.assertEquals("SquareCount mismatch!", expected.squareCount, actual.squareCount)
        Assert.assertEquals("Flag mismatch!", expected.flags, actual.flags)
        Assert.assertEquals("Title mismatch!", expected.title, actual.title)
        Assert.assertEquals("Description mismatch!", expected.description, actual.description)
        Assert.assertEquals("Author mismatch!", expected.author, actual.author)
        Assert.assertEquals("Copyright mismatch!", expected.copyright, actual.copyright)
        Assert.assertEquals("Comment mismatch!", expected.comment, actual.comment)
        Assert.assertEquals("Date mismatch!", expected.date, actual.date)
        Assert.assertEquals("Hash mismatch!", expected.hash, actual.hash)
    }

    protected fun assertLayout(actual: Crossword,
                               expected: Array<Array<String?>>) {
        actual.cellMap.forEachIndexed { i, actualRow ->
            actualRow.forEachIndexed { j, actualCell ->
                Assert.assertEquals("Layout mismatch @char[$i,$j]",
                        expected[i][j], actualCell?.chars)
            }
        }
    }

    protected fun assertAttrLayout(actual: Crossword,
                                   expected: Array<Array<String?>>) {
        actual.cellMap.forEachIndexed { i, actualRow ->
            actualRow.forEachIndexed { j, actualCell ->
                Assert.assertEquals("Layout mismatch @attr[$i,$j]",
                        when (expected[i][j]) {
                            "." -> 0
                            "O" -> Crossword.Cell.ATTR_CIRCLED
                            else -> null
                }, actualCell?.attrFlags?.toInt())
            }
        }
    }

    protected fun assertHints(actual: Crossword,
                              expected: Array<String>) {
        val actualHints = actual.wordsAcross + actual.wordsDown
        Assert.assertEquals("Hint count mismatch", actualHints.size, expected.size)

        (0..actualHints.lastIndex).forEach { i ->
            Assert.assertEquals("Hint mismatch @[$i]",
                    expected[i], actualHints[i].hintSynopsis())
        }
    }

    protected fun assertProof(actual: Crossword, expected: Proof) {
        expected.metadata?.let { m ->
            println("\tassertMetadata")
            assertMetadata(actual, m)
        }

        expected.hints?.let { h ->
            println("\tassertHints")
            assertHints(actual, h)
        }

        expected.layout?.let { l ->
            println("\tassertLayout")
            assertLayout(actual, Array(l.size) { row ->
                l[row].chunked(1).map { when (it) { "#" -> null else -> it } }.toTypedArray()
            })
        }
        expected.attrLayout?.let { l ->
            println("\tassertAttrLayout")
            assertAttrLayout(actual, Array(l.size) { row ->
                l[row].chunked(1).map { when (it) { "#" -> null else -> it } }.toTypedArray()
            })
        }
    }

    @Suppress("unused")
    protected fun Crossword.dumpMetadata() {
        println("width = $width,")
        println("height = $height,")
        println("squareCount = $squareCount,")
        println("title = ${title.enquote()},")
        println("flags = $flags,")
        println("description = ${description.enquote()},")
        println("author = ${author.enquote()},")
        println("copyright = ${copyright.enquote()},")
        println("comment = ${comment.enquote()},")
        println("date = $date,")
        println("hash = ${hash.enquote()}")
    }

    @Suppress("unused")
    protected fun Crossword.dumpLayout() {
        cellMap.forEach { row ->
            println("\"${row.toList().joinToString("") { it?.chars ?: "#" }}\",")
        }
    }

    @Suppress("unused")
    protected fun Crossword.dumpAttrLayout() {
        cellMap.forEach { row ->
            println(row.toList().joinToString("") {
                val flag: Int? = it?.attrFlags?.toInt()
                when {
                    flag?.and(Crossword.Cell.ATTR_CIRCLED) == Crossword.Cell.ATTR_CIRCLED -> "O"
                    flag == 0 -> "."
                    flag == null -> "#"
                    else -> "!"
                }
            })
        }
    }

    @Suppress("unused")
    protected fun Crossword.dumpHints() {
        (wordsAcross + wordsDown).forEach {
            println("\"${it.hintSynopsis().replace("\"", "\\\"")}\",")
        }
    }

    @Suppress("unused")
    protected fun Crossword.dumpAll() {
        dumpMetadata()
        dumpLayout()
        dumpAttrLayout()
        dumpHints()
    }

    private fun Crossword.Word.hintSynopsis(): String {
        val dir = when (direction) {
            Crossword.Word.DIR_ACROSS -> "A"
            Crossword.Word.DIR_DOWN -> "D"
            else -> "?"
        }
        return "$number$dir.$hint"
    }

    private fun String?.enquote(): String? =
            this?.let { "\"${it.replace("\"", "\\\"") }\"" }
}

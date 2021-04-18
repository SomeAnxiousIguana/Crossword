package edu.jsu.mcis.cs408.crossword.lib.formatter

import edu.jsu.mcis.cs408.crossword.lib.formatter.BaseTest
import edu.jsu.mcis.cs408.crossword.lib.core.Crossword
import edu.jsu.mcis.cs408.crossword.lib.core.CrosswordReader
import edu.jsu.mcis.cs408.crossword.lib.core.CrosswordWriter
import edu.jsu.mcis.cs408.crossword.lib.io.PuzFormatter
import org.junit.Test
import org.junit.Assert

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class TestReaderWriter: BaseTest() {

    @Test
    fun crossword_testReadWrite() {
        checkReadWrite(PuzFormatter().load("puzzle.puz"))
    }

    private fun checkReadWrite(crossword: Crossword) {
        println("Checking write..")

        val content = ByteArrayOutputStream().use { stream ->
            CrosswordWriter(stream).use {
                it.write(crossword)
                stream.flush()
            }
            stream.toByteArray()
        }

        println("Checking read...")

        val cw = ByteArrayInputStream(content).use { s ->
            CrosswordReader(s).use { it.read() }
        }

        Assert.assertEquals("Width mismatch!", crossword.width, cw.width)
        Assert.assertEquals("Height mismatch!", crossword.height, cw.height)
        Assert.assertEquals("SquareCount mismatch!", crossword.squareCount, cw.squareCount)
        Assert.assertEquals("Title mismatch!", crossword.title, cw.title)
        Assert.assertEquals("Description mismatch!", crossword.description, cw.description)
        Assert.assertEquals("Author mismatch!", crossword.author, cw.author)
        Assert.assertEquals("Copyright mismatch!", crossword.copyright, cw.copyright)
        Assert.assertEquals("Comment mismatch!", crossword.comment, cw.comment)
        Assert.assertEquals("Date mismatch!", crossword.date, cw.date)
        Assert.assertEquals("Hash mismatch!", crossword.hash, cw.hash)
    }
}
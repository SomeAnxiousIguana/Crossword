package edu.jsu.mcis.cs408.crossword.lib.formatter

import edu.jsu.mcis.cs408.crossword.lib.formatter.BaseTest
import edu.jsu.mcis.cs408.crossword.lib.core.Crossword
import edu.jsu.mcis.cs408.crossword.lib.io.PzzlFormatter
import edu.jsu.mcis.cs408.crossword.lib.util.ReferenceScanner
import org.junit.Assert
import org.junit.Test


class TestReferenceScanner: BaseTest() {

    val crossword = PzzlFormatter().load("nytsyncrossword.pzzl")

    @Test
    fun crossword_testMetadata() {
        tests.forEach {
            val found = ReferenceScanner.findReferences(it.hint, crossword)
            Assert.assertEquals("Reference mismatch", it.expectedRefs, found)
        }
    }

    class RefTest(val hint: String,
                  val expectedRefs: List<ReferenceScanner.WordReference>)

    companion object {
        val tests = arrayOf(
                RefTest("See 31-Across",
                        listOf(ReferenceScanner.WordReference(31, Crossword.Word.DIR_ACROSS, 4, 6))),
                RefTest("See 2-Across", listOf()), // there is no 2 across
                RefTest("10 Downing Street", listOf()),
                RefTest("With 43-Across, \"What is it?\"",
                        listOf(ReferenceScanner.WordReference(43, Crossword.Word.DIR_ACROSS, 5, 7))),
//                Foo("3 Doors Down", listOf()), // FIXME: broken
                RefTest("Photo badges, e.g.", listOf()),
                RefTest("\"How's ___?\"", listOf()),
                RefTest("2 hours after sundown", listOf()),
                RefTest("View across", listOf()))
    }
}

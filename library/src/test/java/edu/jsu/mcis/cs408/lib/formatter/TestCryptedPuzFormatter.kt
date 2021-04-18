package edu.jsu.mcis.cs408.crossword.lib.formatter

import edu.jsu.mcis.cs408.crossword.lib.formatter.BaseTest
import edu.jsu.mcis.cs408.crossword.lib.core.buildCrossword
import edu.jsu.mcis.cs408.crossword.lib.io.FormatException
import edu.jsu.mcis.cs408.crossword.lib.io.PuzFormatter
import org.junit.Test
import java.io.File


class TestCryptedPuzFormatter: BaseTest() {

    val crossword = PuzFormatter().load("crypted.puz")

    @Test
    fun crossword_testKey() {
        // FIXME: read file stream to RAM and use a RAM-based inputstream
        for (key in 0..9999) {
            val success = try {
                File(root, "crypted.puz").inputStream().use { s ->
                    buildCrossword { PuzFormatter().readWithKey(this, s, key) } }
                true
            } catch (e: FormatException) {
                false
            }

            assert((key == actualKey) == success)
        }
    }

    @Test
    fun crossword_testLayout() {
        assertLayout(crossword, Array(charMap.size) { row ->
            charMap[row].chunked(1).map { when (it) { "#" -> null else -> it } }.toTypedArray()
        })
    }

    companion object {
        val actualKey = 2949
        val charMap = arrayOf(
                "WHEN#CANWE#MAYI",
                "WACO#BLAHS#INOT",
                "WHOWASTHAT#DYKE",
                "#ALOT###TAPROOM",
                "###READY#SHIN##",
                "FRAS#LOOS#OBEYS",
                "LIRE#TRUEST#HOW",
                "AGE#WHERETO#OUI",
                "WHY#ROMANI#IMIN",
                "STORY#INTL#NENE",
                "##UIES#GOTIT###",
                "YESORNO###DRAC#",
                "ABUT#ARTTHOUNOT",
                "RARE#CLEAR#SEMI",
                "DYED#KYLES#TWOS")
    }
}

package edu.jsu.mcis.cs408.crossword.lib.core

import edu.jsu.mcis.cs408.crossword.lib.io.FormatException
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.io.ObjectInputStream


class CrosswordReader @Throws(IOException::class) constructor(stream: InputStream) : Closeable {

    private val inStream = ObjectInputStream(stream)

    @Throws(IOException::class)
    fun read(): Crossword {
        // Check magic number
        if (inStream.readInt() != CrosswordWriter.MAGIC_NUMBER) {
            throw FormatException("Magic number mismatch")
        }

        // Check version number
        val version = inStream.readByte().toInt()
        if (version > CrosswordWriter.VERSION_CURRENT) {
            throw IllegalArgumentException("Version $version not supported")
        }

        // Read the puzzle
        return readCrossword(version)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readCrossword(version: Int): Crossword {
        val width = inStream.readInt()
        val height = inStream.readInt()
        val squareCount = inStream.readInt()
        val title = inStream.readObject() as String?
        val description = inStream.readObject() as String?
        val author = inStream.readObject() as String?
        val copyright = inStream.readObject() as String?
        val comment = if (version >= 2) inStream.readObject() as String? else null
        val alphabet = (inStream.readObject() as CharArray).toSet()
        val date = inStream.readLong()
        val flags = if (version >= 4) inStream.readInt() else 0

        val wordsAcross = ArrayList<Crossword.Word>()
        (0 until inStream.readInt()).forEach { _ ->
            wordsAcross.add(readWord(version, Crossword.Word.DIR_ACROSS))
        }
        val wordsDown = ArrayList<Crossword.Word>()
        (0 until inStream.readInt()).forEach { _ ->
            wordsDown.add(readWord(version, Crossword.Word.DIR_DOWN))
        }

        return Crossword(
                width = width,
                height = height,
                squareCount = squareCount,
                flags = flags,
                title = title,
                description = description,
                author = author,
                copyright = copyright,
                comment = comment,
                date = date,
                wordsAcross = wordsAcross,
                wordsDown = wordsDown,
                alphabet = alphabet)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readWord(version: Int, direction: Int): Crossword.Word {
        val number = inStream.readShort().toInt()
        val hint = inStream.readObject() as String?
        val startRow = inStream.readShort().toInt()
        val startColumn = inStream.readShort().toInt()
        val hintUrl = inStream.readObject() as String?
        val citation = inStream.readObject() as String?
        val cells = ArrayList<Crossword.Cell>().apply {
            (0 until inStream.readInt()).forEach { _ -> add(readCell(version)) }
        }

        return Crossword.Word(
                number = number,
                hint = hint,
                startRow = startRow,
                startColumn = startColumn,
                direction = direction,
                hintUrl = hintUrl,
                citation = citation,
                cells = cells.toTypedArray())
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readCell(version: Int): Crossword.Cell {
        val attrFlags = inStream.readByte()
        val chars = when {
            version >= 3 -> inStream.readObject() as String
            else -> String(inStream.readObject() as CharArray)
        }

        return Crossword.Cell(chars, attrFlags)
    }

    @Throws(IOException::class)
    override fun close() { inStream.close() }
}

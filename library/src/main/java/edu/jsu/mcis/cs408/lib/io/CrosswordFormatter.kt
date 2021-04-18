package edu.jsu.mcis.cs408.crossword.lib.io

import edu.jsu.mcis.cs408.crossword.lib.core.Crossword

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


interface CrosswordFormatter {


    fun setEncoding(encoding: String)

    @Throws(IOException::class)
    fun read(builder: Crossword.Builder, inputStream: InputStream)


    @Throws(IOException::class)
    fun write(crossword: Crossword, outputStream: OutputStream)


    fun canRead(): Boolean


    fun canWrite(): Boolean
}

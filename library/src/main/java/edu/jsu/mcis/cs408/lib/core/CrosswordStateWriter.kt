package edu.jsu.mcis.cs408.crossword.lib.core

import java.io.Closeable
import java.io.IOException
import java.io.ObjectOutputStream
import java.io.OutputStream


class CrosswordStateWriter @Throws(IOException::class) constructor(stream: OutputStream) : Closeable {

    private val outStream = ObjectOutputStream(stream)

    @Throws(IOException::class)
    fun write(state: CrosswordState) {
        outStream.writeInt(MAGIC_NUMBER)
        outStream.writeByte(VERSION_CURRENT)

        writeState(state)
    }

    @Throws(IOException::class)
    private fun writeState(state: CrosswordState) {
        outStream.writeInt(state.width)
        outStream.writeInt(state.height)
        outStream.writeShort(state.squaresSolved.toInt())
        outStream.writeShort(state.squaresCheated.toInt())
        outStream.writeShort(state.squaresWrong.toInt())
        outStream.writeShort(state.squaresUnknown.toInt())
        outStream.writeShort(state.squareCount.toInt())
        outStream.writeLong(state.playTimeMillis)
        outStream.writeLong(state.lastPlayed)
        outStream.writeInt(state.selection)
        outStream.writeObject(state.charMatrix.flatten().toTypedArray())
        outStream.writeObject(IntArray(state.height * state.width) {
            state.attrMatrix[it / state.width][it % state.width] })
    }

    @Throws(IOException::class)
    override fun close() { outStream.close() }

    companion object {
        internal const val VERSION_CURRENT = 3
        internal const val MAGIC_NUMBER = -0x45522113
    }
}

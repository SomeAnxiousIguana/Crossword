package edu.jsu.mcis.cs408.crossword.lib.util

import java.util.Arrays


// Not thread-safe!
internal class SparseArray<E>(capacity: Int = DEFAULT_CAPACITY) {

    private var keys = IntArray(capacity)
    private var values = arrayOfNulls<Any?>(capacity)
    private var size: Int = 0

    @Suppress("UNCHECKED_CAST")
    operator fun get(key: Int, defaultIfNotFound: E?): E? {
        val index = Arrays.binarySearch(keys, 0, size, key)
        return if (index >= 0) {
            values[index] as E
        } else defaultIfNotFound
    }

    operator fun get(key: Int): E? = get(key, null)

    operator fun set(key: Int, value: E) { put(key, value) }

    @Suppress("UNCHECKED_CAST")
    fun forEach(block: (k: Int, v: E) -> Unit) {
        (0 until size).forEach { block(keys[it], values[it] as E) }
    }

    fun put(key: Int, value: E) {
        var index = Arrays.binarySearch(keys, 0, size, key)
        if (index >= 0) {
            keys[index] = key
            values[index] = value
        } else {
            index = index.inv()
            if (size >= keys.size) {
                val newCapacity = size + CAPACITY_INCREMENT
                val newKeys = IntArray(newCapacity)
                val newValues = arrayOfNulls<Any>(newCapacity)

                // Copy head
                System.arraycopy(keys, 0, newKeys, 0, index)
                System.arraycopy(values, 0, newValues, 0, index)
                // Copy tail
                System.arraycopy(keys, index, newKeys, index + 1, size - index)
                System.arraycopy(values, index, newValues, index + 1, size - index)

                keys = newKeys
                values = newValues
            } else {
                for (i in size - 1 downTo index) {
                    keys[i + 1] = keys[i]
                    values[i + 1] = values[i]
                }
            }

            keys[index] = key
            values[index] = value
            size++
        }
    }

    fun clear() {
        val capacity = keys.size

        keys = IntArray(capacity)
        values = arrayOfNulls(capacity)
        size = 0
    }

    fun size(): Int = size

    fun capacity(): Int = keys.size

    fun keyAt(index: Int): Int {
        if (index < 0 || index >= size) throw ArrayIndexOutOfBoundsException(index)
        return keys[index]
    }

    @Suppress("UNCHECKED_CAST")
    fun valueAt(index: Int): E {
        if (index < 0 || index >= size) throw ArrayIndexOutOfBoundsException(index)
        return values[index] as E
    }

    override fun toString(): String = buildString {
        append("(")
        (0 until size).forEach {
            if (it > 0) append(",")
            append("${keys[it]}=${values[it]}")
        }
        append(")")
    }

    companion object {
        const val DEFAULT_CAPACITY = 10
        const val CAPACITY_INCREMENT = 16
    }
}

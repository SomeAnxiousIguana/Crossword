package edu.jsu.mcis.cs408.crossword.lib.io

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory

import java.io.IOException
import java.io.InputStream
import java.util.Stack


abstract class SimpleXmlParser {

    private val stack: Stack<String> = Stack()

    class SimpleXmlPath(private val list: List<String>) {

        private var pos: Int = 0

        val isDeadEnd: Boolean
            get() = isEqualTo()

        fun startsWith(vararg names: String): Boolean {
            val listSize = list.size
            if (names.size > listSize - pos) return false

            names.forEachIndexed { i, name ->
                val stackName = list[i + pos]
                when {
                    name.endsWith("*") -> {
                        // Trailing wildcard match
                        val prefix = name.substring(0, name.length - 1)
                        if (!stackName.startsWith(prefix)) return false
                    }
                    else -> // Exact match
                        if (stackName != name && "?" != name) return false
                }
            }

            pos += names.size

            return true
        }

        fun isEqualTo(vararg names: String): Boolean =
                names.size == list.size - pos && startsWith(*names)

        override fun toString(): String =
                list.slice(pos..list.lastIndex).joinToString("/", "/")
    }

    @Throws(XmlPullParserException::class, IOException::class)
    protected fun parseXml(inputStream: InputStream) {
        stack.clear()

        val parser = XmlPullParserFactory.newInstance().apply {
            isNamespaceAware = false
        }.newPullParser()

        parser.setInput(inputStream, "UTF-8")
        parser.setFeature(XML_RELAXED, true)

        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    stack.push(parser.name)
                    onStartElement(SimpleXmlPath(stack), parser)
                }
                XmlPullParser.END_TAG -> stack.pop()
                XmlPullParser.TEXT -> onTextContent(SimpleXmlPath(stack), parser.text)
            }

            eventType = parser.next()
        }
    }

    protected open fun onStartElement(path: SimpleXmlPath, parser: XmlPullParser) {}

    protected open fun onTextContent(path: SimpleXmlPath, text: String) {}

    protected fun XmlPullParser.intValue(name: String, defaultValue: Int): Int =
            stringValue(name)?.toIntOrNull() ?: defaultValue

    protected fun XmlPullParser.stringValue(name: String): String? =
            getAttributeValue(null, name)

    companion object {
        private const val XML_RELAXED = "http://xmlpull.org/v1/doc/features.html#relaxed"
    }
}

package edu.jsu.mcis.cs408.crossword.lib.util

import org.junit.Assert
import org.junit.Test


class TestHtmlUtility {

    @Test
    fun testEntityStripper() {
        strings.forEach { p ->
            Assert.assertEquals("String mismatch",
                    p.second, HtmlUtility.stripEntities(p.first))
        }
    }

    // Pair(before, expected)
    private val strings = listOf(
            Pair("", ""),
            Pair("&#;", ""), // Invalid num. entity
            Pair("&#", "&#"),
            Pair("&;", ""), // Invalid named entity
            Pair("&", "&"),
            Pair("&amp;", "&"),
            Pair("&amp", "&amp"),
            Pair("&am;", ""), // Invalid named entity
            Pair("It's all about the &#36;&#36;", "It's all about the $$"),
            Pair("5 &#8805; 2", "5 ≥ 2"),
            Pair("&amp;hf346,1", "&hf346,1"),
            Pair("Here&apos;s Johnny!", "Here's Johnny!"),
            Pair("&quot;Quote un-quote&quot;", "\"Quote un-quote\""),
            Pair("Here's a fraction: &frac34;", "Here's a fraction: ¾"),

            Pair("Sister of Chekhov&rsquo;s Masha and Irina",
                    "Sister of Chekhov’s Masha and Irina"),
            Pair("Ovid&rsquo;s first work",
                    "Ovid’s first work"),
            Pair("&ldquo;...upon the stair, ___ man who wasn&rsquo;t there&rdquo;",
                    "“...upon the stair, ___ man who wasn’t there”"),
            Pair("Cariou of &ldquo;Blue Bloods&rdquo;",
                    "Cariou of “Blue Bloods”")
    )
}

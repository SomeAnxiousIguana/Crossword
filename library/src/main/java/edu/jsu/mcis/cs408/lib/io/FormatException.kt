package edu.jsu.mcis.cs408.crossword.lib.io


class FormatException(message: String, cause: Throwable?) : RuntimeException(message, cause) {

    constructor(message: String) : this(message, null)
}

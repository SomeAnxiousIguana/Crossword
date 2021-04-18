package edu.jsu.mcis.cs408.crossword.lib.formatter


data class Metadata(val width: Int,
                    val height: Int,
                    val squareCount: Int,
                    val flags: Int,
                    val title: String?,
                    val description: String?,
                    val author: String?,
                    val copyright: String?,
                    val comment: String?,
                    val date: Long,
                    val hash: String)

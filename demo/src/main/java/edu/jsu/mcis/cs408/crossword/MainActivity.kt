package edu.jsu.mcis.cs408.crossword

import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast

import edu.jsu.mcis.cs408.crossword.lib.core.Crossword
import edu.jsu.mcis.cs408.crossword.lib.core.buildCrossword
import edu.jsu.mcis.cs408.crossword.lib.io.PuzFormatter
import edu.jsu.mcis.cs408.crossword.lib.view.CrosswordView

class MainActivity : AppCompatActivity(), CrosswordView.OnLongPressListener, CrosswordView.OnStateChangeListener, CrosswordView.OnSelectionChangeListener {

    private var crosswordView: CrosswordView? = null
    private var hint: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        crosswordView = findViewById(R.id.crossword)
        hint = findViewById(R.id.hint)

        val crossword = readPuzzle(R.raw.puzzle)

        crosswordView!!.let { cv ->
            cv.crossword = crossword
            cv.onLongPressListener = this
            cv.onStateChangeListener = this
            cv.onSelectionChangeListener = this
            cv.inputValidator = { ch -> !ch.first().isISOControl() }
            cv.undoMode = CrosswordView.UNDO_NONE
            cv.markerDisplayMode = CrosswordView.MARKER_CHEAT

            onSelectionChanged(cv, cv.selectedWord, cv.selectedCell)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        crosswordView!!.restoreState(savedInstanceState.getParcelable("state")!!)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable("state", crosswordView!!.state)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_restart -> {
                crosswordView!!.reset()
                return true
            }
            R.id.menu_solve_cell -> {
                crosswordView!!.solveChar(crosswordView!!.selectedWord!!,
                        crosswordView!!.selectedCell)
                return true
            }
            R.id.menu_solve_word -> {
                crosswordView!!.solveWord(crosswordView!!.selectedWord!!)
                return true
            }
            R.id.menu_solve_puzzle -> {
                crosswordView!!.solveCrossword()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCellLongPressed(view: CrosswordView,
                                   word: Crossword.Word, cell: Int) {
        Toast.makeText(this, "Show popup menu for " + word.hint!!,
                Toast.LENGTH_SHORT).show()
    }

    override fun onCrosswordChanged(view: CrosswordView) {}

    override fun onCrosswordSolved(view: CrosswordView) {
        Toast.makeText(this, R.string.youve_solved_the_puzzle,
                Toast.LENGTH_SHORT).show()
    }

    override fun onCrosswordUnsolved(view: CrosswordView) { }

    private fun readPuzzle(@RawRes resourceId: Int): Crossword =
            resources.openRawResource(resourceId).use { s ->
                buildCrossword { PuzFormatter().read(this, s) }
            }

    override fun onSelectionChanged(view: CrosswordView,
                                    word: Crossword.Word?, position: Int) {
        hint!!.text = when (word?.direction) {
            Crossword.Word.DIR_ACROSS -> getString(R.string.across, word.number, word.hint)
            Crossword.Word.DIR_DOWN -> getString(R.string.down, word.number, word.hint)
            else -> ""
        }
    }
}

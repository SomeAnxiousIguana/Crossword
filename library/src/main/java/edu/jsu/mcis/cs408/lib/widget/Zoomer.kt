package edu.jsu.mcis.cs408.crossword.lib.widget

import android.content.Context
import android.os.SystemClock
import android.view.animation.DecelerateInterpolator


@Suppress("unused", "MemberVisibilityCanBePrivate")
class Zoomer(context: Context) {

    private val interpolator = DecelerateInterpolator()

    private val animationDurationMillis: Int = context.resources.getInteger(
            android.R.integer.config_shortAnimTime)

    var isFinished = true
        private set

    var currZoom: Float = 0f
        private set

    private var startRTC: Long = 0

    private var endZoom: Float = 0f

    fun forceFinished(finished: Boolean) {
        isFinished = finished
    }

    fun abortAnimation() {
        isFinished = true
        currZoom = endZoom
    }

    fun startZoom(endZoom: Float) {
        startRTC = SystemClock.elapsedRealtime()
        this.endZoom = endZoom

        isFinished = false
        currZoom = 1f
    }

    fun computeZoom(): Boolean {
        if (isFinished) {
            return false
        }

        val tRTC = SystemClock.elapsedRealtime() - startRTC
        if (tRTC >= animationDurationMillis) {
            isFinished = true
            currZoom = endZoom
            return false
        }

        val t = tRTC * 1f / animationDurationMillis
        currZoom = endZoom * interpolator.getInterpolation(t)
        return true
    }
}

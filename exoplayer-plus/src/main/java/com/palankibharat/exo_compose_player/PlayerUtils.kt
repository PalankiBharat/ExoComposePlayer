package com.palankibharat.exo_compose_player

import android.app.Activity
import android.content.Context
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import androidx.annotation.FloatRange
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.math.abs
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class PlayerUtils {

    fun setWindowBrightness(activity: Activity?, brightness: Int) {
        val lp = activity?.window?.attributes ?: return
        lp.screenBrightness = brightness / 225.0f
        if (lp.screenBrightness > 1) {
            lp.screenBrightness = 1f
        } else if (lp.screenBrightness < 0) {
            lp.screenBrightness = 0f
        }
        activity.window?.attributes = lp
    }
}

fun ExoPlayer.seekForward(timeInMills:Long){
    this.seekTo(this.currentPosition + timeInMills)
}

fun ExoPlayer.seekBackward(timeInMills:Long){
    this.seekTo(this.currentPosition - timeInMills)
}

fun Player.remainingTimeFlow(
    updateFrequency: Duration = 1.seconds,
) = flow {
    while (true) {
        if (isPlaying) emit(abs(duration - currentPosition).toDuration(DurationUnit.MILLISECONDS))
        delay(updateFrequency)
    }
}.flowOn(Dispatchers.Main)

fun Player.currentPositionFlow(
    updateFrequency: Duration = 500.milliseconds,
) = flow {
    while (true) {
        if (isPlaying) emit(currentPosition)
        delay(updateFrequency)
    }
}.flowOn(Dispatchers.Main)

fun Long.formatMinSec(): String {
    return if (this <= 0L) {
        "..."
    } else {
        val hours = this / (1000 * 60 * 60)
        val minutes = (this % (1000 * 60 * 60)) / (1000 * 60)
        val seconds = (this % (1000 * 60)) / 1000
        when {
            hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
            minutes > 0 -> String.format("%02d:%02d", minutes, seconds)
            else -> String.format("00:%02d", seconds)
        }
    }
}

fun Context.changeBrightnessLevel(@FloatRange(0.0, 1.0) percent: Float) {
    val activity = this as Activity
    val layout: WindowManager.LayoutParams? = activity.window?.attributes
    layout?.screenBrightness = percent
    activity.window?.attributes = layout
}

fun Context.getCurrentBrightness(): Float {
    var curBrightness =
        Settings.System.getFloat(this.contentResolver, Settings.System.SCREEN_BRIGHTNESS)
    curBrightness /= 100f
    if (curBrightness > 1) {
        curBrightness = 1f
    } else if (curBrightness < 0) {
        curBrightness = 0f
    }
    Log.d("TAG", "getCurrentBrightness: $curBrightness")
    return curBrightness
}
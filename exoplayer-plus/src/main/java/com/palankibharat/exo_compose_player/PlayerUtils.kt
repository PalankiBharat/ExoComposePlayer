package com.palankibharat.exo_compose_player

import android.app.Activity
import androidx.media3.exoplayer.ExoPlayer

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
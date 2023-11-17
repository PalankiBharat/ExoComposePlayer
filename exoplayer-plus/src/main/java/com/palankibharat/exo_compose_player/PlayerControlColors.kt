package com.palankibharat.exo_compose_player

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.time.Duration.Companion.seconds

/**
 * <a href="" class="external" target="_blank">Player Controls Style</a>.
 *
 * PlayerControlColors will help you customize the color of your player controls
 *
 * @param centerControlColors is the color of the center controls such as play pause, forward and backward icons
 * @param seekbarColor is the color of the seekbar
 * @param seekbarThickness is the thickness of the seekbar
 * @param runtimeColor is the color of the runtime value (Value in the left side of the seekbar)
 * @param remainingTimeColor is the color of the remaining time of the video value (Value in the right side of the seekbar)
 */
data class PlayerControlsStyle(
    val centerControlColors:Color ,
    val seekbarColor:Color ,
    val runtimeColor:Color ,
    val remainingTimeColor:Color,
    val seekbarThickness:Dp
)

/**
 * <a href="" class="external" target="_blank">Player Control Colors</a>.
 *
 * PlayerControlColors will help you customize the color of your player controls
 *
 * @param shouldShowCenterControls is the color of the center controls such as play pause, forward and backward icons
 * @param shouldShowSeekbar is the color of the seekbar
 * @param forwardClickIntervalTime is the time (in Milliseconds) the video will skip on click of forward button
 * @param replayClickIntervalTime is the time (in Milliseconds) the video will skip on click of backward button
 * @param isDoubleTapToForwardBackwardEnabled is if the double tap upon the player should work or not (time will be same as the forwardClickTime and replayClickTime )
 * @param isBrightnessSliderEnabled is if to enable the brightness control slider (present at the right side)
 * @param isVolumeSliderEnabled is if to enable the volume control slider (present at the left side)
 */

data class PlayerControlsConfiguration(
    val shouldShowCenterControls:Boolean,
    val shouldShowSeekbar:Boolean,
    val forwardClickIntervalTime:Long,
    val replayClickIntervalTime:Long,
    val isDoubleTapToForwardBackwardEnabled :Boolean,
    val isBrightnessSliderEnabled :Boolean,
    val isVolumeSliderEnabled :Boolean,
    val isFullScreenEnabled :Boolean,
)

object PlayerDefaults{
    val defaultPlayerControls = PlayerControlsStyle(
        centerControlColors = Color.White,
        seekbarColor =Color.White,
        runtimeColor =Color.White,
        remainingTimeColor =Color.White,
        seekbarThickness =3.dp
    )

    val defaultPlayerControlsConfiguration = PlayerControlsConfiguration(
        shouldShowCenterControls = true,
        shouldShowSeekbar = true,
        forwardClickIntervalTime = 10.seconds.inWholeMilliseconds,
        replayClickIntervalTime = 10.seconds.inWholeMilliseconds,
        isDoubleTapToForwardBackwardEnabled = true,
        isBrightnessSliderEnabled = true,
        isVolumeSliderEnabled = true,
        isFullScreenEnabled = true
    )

}



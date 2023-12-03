package com.palankibharat.exo_compose_player.models

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

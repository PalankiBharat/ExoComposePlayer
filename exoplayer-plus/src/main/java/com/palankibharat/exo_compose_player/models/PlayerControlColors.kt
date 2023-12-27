package com.palankibharat.exo_compose_player.models

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
 * @param seekbarActiveColor is the color of the seekbar
 * @param seekbarThickness is the thickness of the seekbar
 * @param runtimeColor is the color of the runtime value text (Value in the left side of the seekbar)
 * @param remainingTimeColor is the color of the remaining time of the video value text (Value in the right side of the seekbar)
 */
data class PlayerControlsStyle(
    val centerControlColors:Color = PlayerDefaults.defaultPlayerControls.centerControlColors,
    val seekbarActiveColor:Color = PlayerDefaults.defaultPlayerControls.seekbarActiveColor,
    val seekbarInactiveColor:Color = PlayerDefaults.defaultPlayerControls.seekbarInactiveColor,
    val runtimeColor:Color = PlayerDefaults.defaultPlayerControls.runtimeColor,
    val remainingTimeColor:Color = PlayerDefaults.defaultPlayerControls.remainingTimeColor,
    val seekbarThickness:Dp = PlayerDefaults.defaultPlayerControls.seekbarThickness
)



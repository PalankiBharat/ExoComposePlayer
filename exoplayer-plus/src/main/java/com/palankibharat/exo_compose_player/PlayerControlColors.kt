package com.palankibharat.exo_compose_player

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
    val centerControlColors:Color = Color.White,
    val seekbarColor:Color = Color.White,
    val runtimeColor:Color = Color.White,
    val remainingTimeColor:Color = Color.White,
    val seekbarThickness:Dp= 3.dp
)



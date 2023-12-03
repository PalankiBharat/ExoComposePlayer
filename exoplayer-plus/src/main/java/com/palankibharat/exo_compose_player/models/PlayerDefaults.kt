package com.palankibharat.exo_compose_player.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.time.Duration.Companion.seconds

object PlayerDefaults{
    val defaultPlayerControls = PlayerControlsStyle(
        centerControlColors = Color.White,
        seekbarActiveColor =Color.White,
        runtimeColor =Color.White,
        remainingTimeColor =Color.White,
        seekbarThickness =3.dp,
        seekbarInactiveColor = Color.Gray
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
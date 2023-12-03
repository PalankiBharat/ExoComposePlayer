package com.palankibharat.exo_compose_player

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.palankibharat.exo_compose_player.models.PlayerControlsConfiguration
import com.palankibharat.exo_compose_player.models.PlayerControlsStyle
import com.palankibharat.exo_compose_player.models.PlayerDefaults

@Composable
fun CenterPlayerControls(
    modifier: Modifier = Modifier,
    playerControlsStyle: PlayerControlsStyle = PlayerDefaults.defaultPlayerControls,
    playerControlsConfiguration: PlayerControlsConfiguration = PlayerDefaults.defaultPlayerControlsConfiguration,
    onReplayClick: () -> Unit = {},
    onPlayPauseToggle: () -> Unit = {},
    onForwardClick: () -> Unit = {},
    currentDuration: Long = 0L,
    totalDuration: Long = 0L,
    seekbarPosition: Float = 0f,
    onSeekBarValueChange: (Float) -> Unit = {},
    onBrightnessChange: (value: Float) -> Unit = {},
    brightnessLevel: Float,
    onVolumeChange: (value: Float) -> Unit = {},
    onPlayerModeChangeClick: () -> Unit = {},
    volumeLevel: Float,
    playerMode: PlayerModes = PlayerModes.MINI_PLAYER,
    isPlaying: Boolean,
) {
    val context = LocalContext.current
    val activity = context as Activity

    var videoPlaybackPosition by remember {
        mutableFloatStateOf(seekbarPosition)
    }

    LaunchedEffect(key1 = currentDuration) {
        videoPlaybackPosition = (currentDuration.toFloat() / totalDuration.toFloat())
    }

    val isInFullPlayerMode = (playerMode == PlayerModes.FULL_PLAYER)

    // black overlay across the video player
    Box(modifier = modifier.background(Color.Black.copy(alpha = 0.6f))) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(bottom = 30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Brightness Slider
            val shouldShowBrightnessSlider =
                isInFullPlayerMode && playerControlsConfiguration.isBrightnessSliderEnabled
            if (shouldShowBrightnessSlider) {
                Column {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_brightness_medium),
                            contentDescription = "Brightness Level",
                            tint = playerControlsStyle.centerControlColors
                        )
                    }
                    HSlider(
                        modifier = Modifier.fillMaxHeight(0.7f),
                        defaultValue = brightnessLevel,
                        onValueChange = {
                            Log.d("TAG", "CenterPlayerControls: Brightness Level $brightnessLevel")
                            onBrightnessChange(it)
                        })
                }
            }

            // replay button - Backward Button
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f)
            ) {
                Box(
                    Modifier
                        .align(Alignment.CenterEnd)
                ) {
                    DoubleTapToForwardIcon(
                        isForward = false,
                        forwardIntervalTime = playerControlsConfiguration.replayClickIntervalTime/1000,
                        color = playerControlsStyle.centerControlColors
                    ) {
                        onReplayClick()
                    }
                }
            }

            // pause/play toggle button
            Box(modifier = Modifier
                .padding(horizontal = 10.dp)
                .size(30.dp)
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                }) {
                ComposePlayPauseButton(
                    modifier = Modifier.fillMaxSize(),
                    iconColor = playerControlsStyle.centerControlColors,
                    isVideoPlaying = isPlaying
                ) {
                    onPlayPauseToggle()
                }
            }

            // forward button
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    DoubleTapToForwardIcon(
                        isForward = true,
                        color = playerControlsStyle.centerControlColors,
                        forwardIntervalTime = playerControlsConfiguration.forwardClickIntervalTime/1000
                    ) {
                        onForwardClick()
                    }
                }
            }

            // Volume Slider
            val shouldShowVolumeSlider =
                isInFullPlayerMode && playerControlsConfiguration.isVolumeSliderEnabled
            if (shouldShowVolumeSlider) {
                Column {
                    IconButton(onClick = { }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_volume_high),
                            contentDescription = "Volume Level",
                        )
                    }
                    HSlider(
                        Modifier.fillMaxHeight(0.7f),
                        defaultValue = volumeLevel,
                        onValueChange = {})
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = currentDuration.formatMinSec(),
                color = Color.White,
                fontSize = 12.sp
            )
            Slider(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 14.dp),
                value = videoPlaybackPosition,
                onValueChange = {
                    videoPlaybackPosition = it
                    onSeekBarValueChange(it)
                },
                colors = SliderDefaults.colors(
                    activeTrackColor = playerControlsStyle.seekbarActiveColor,
                    inactiveTrackColor = playerControlsStyle.seekbarInactiveColor,
                    thumbColor = playerControlsStyle.seekbarActiveColor
                )

            )
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = totalDuration.formatMinSec(),
                color = Color.White,
                fontSize = 12.sp
            )
        }


        IconButton(modifier =Modifier
            .padding(top = 20.dp, end = 10.dp)
            .size(28.dp)
            .align(Alignment.TopEnd), onClick = {
            val orientation =
                if (isInFullPlayerMode) ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR else ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            activity.requestedOrientation = orientation
            onPlayerModeChangeClick()
        }) {
            Image(
                contentScale = ContentScale.Fit,
                painter = painterResource(id = if (isInFullPlayerMode) R.drawable.ic_fullscreen_exit else R.drawable.ic_fullscreen),
                contentDescription = "Mini Screen",
            )
        }
    }
}


@Preview(showBackground = true, device = Devices.AUTOMOTIVE_1024p, widthDp = 480)
@Preview(device = Devices.PIXEL_4_XL, showSystemUi = true)
@Composable
fun CenterPlayerControlsPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
    )
    {
        val configuration = LocalConfiguration.current
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            CenterPlayerControls(
                brightnessLevel = 50f,
                volumeLevel = 20f,
                isPlaying = true,
                playerMode = PlayerModes.FULL_PLAYER
            )
        } else {
            CenterPlayerControls(
                brightnessLevel = 50f,
                volumeLevel = 20f,
                isPlaying = true,
                playerMode = PlayerModes.MINI_PLAYER
            )
        }

    }
}


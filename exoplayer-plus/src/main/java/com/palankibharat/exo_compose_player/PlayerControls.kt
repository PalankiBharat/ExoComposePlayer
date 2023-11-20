package com.palankibharat.exo_compose_player

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.SliderPositions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    volumeLevel: Float,
    playerMode: PlayerModes = PlayerModes.FULL_PLAYER,
    isPlaying: Boolean,
) {
    val context = LocalContext.current

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
            val shouldShowBrightnessSlider = isInFullPlayerMode && playerControlsConfiguration.isBrightnessSliderEnabled
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

            // replay button
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
                        forwardIntervalTime = playerControlsConfiguration.forwardClickIntervalTime,
                        color = playerControlsStyle.centerControlColors
                    ) {
                        onReplayClick()
                    }
                }
            }
            /* IconButton(modifier = Modifier.size(40.dp), onClick = {
                 onReplayClick()
             }) {
                 Image(
                     modifier = Modifier.fillMaxSize(),
                     contentScale = ContentScale.Crop,
                     painter = painterResource(id = R.drawable.ic_exo_icon_rewind),
                     contentDescription = "Replay 15 seconds",
                 )
             }*/

            // pause/play toggle button
            Box(modifier = Modifier
                .padding(horizontal = 10.dp)
                .size(30.dp)
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    // onPlayPauseToggle(isPlaying)
                }) {
                ComposePlayPauseButton(modifier = Modifier.fillMaxSize(), iconColor = playerControlsStyle.centerControlColors, isVideoPlaying = isPlaying){
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
                    DoubleTapToForwardIcon(isForward = true, color = playerControlsStyle.centerControlColors, forwardIntervalTime = playerControlsConfiguration.forwardClickIntervalTime) {
                        onForwardClick()
                    }
                }
            }

            /* IconButton(modifier = Modifier.size(40.dp), onClick = {
                 onForwardClick()
             }) {
                 Image(
                     modifier = Modifier.fillMaxSize(),
                     contentScale = ContentScale.Crop,
                     painter = painterResource(id = R.drawable.ic_exo_icon_fastforward),
                     contentDescription = "Forward 10 seconds",
                 )
             }*/

            // Volume Slider
            val shouldShowVolumeSlider = isInFullPlayerMode && playerControlsConfiguration.isVolumeSliderEnabled
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

        /*if (playerMode == PlayerModes.MINI_PLAYER) {
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.BottomEnd)
                    .clickable {
                        val activity = context as Activity

                        activity.requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    },
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.ic_fullscreen),
                contentDescription = "Full Screen",
            )
        }
        else{
            Image(
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.BottomEnd)
                    .clickable {
                        val activity = context as Activity

                        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    },
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.ic_fullscreen_exit),
                contentDescription = "Mini Screen",
            )
        }*/
    }
}


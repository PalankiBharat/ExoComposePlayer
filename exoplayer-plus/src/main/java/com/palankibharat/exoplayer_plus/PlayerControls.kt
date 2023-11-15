package com.palankibharat.exoplayer_plus

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import androidx.annotation.FloatRange
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun CenterPlayerControls(
    modifier: Modifier = Modifier,
    onReplayClick: () -> Unit = {},
    onPlayPauseToggle: (isPlaying: Boolean) -> Unit = {},
    onForwardClick: () -> Unit = {},
    currentDuration: Long = 0L,
    totalDuration: Long = 0L,
    seekbarPosition: Float = 0f,
    onSeekBarValueChange: (Float) -> Unit = {},
    onBrightnessChange: (value: Float) -> Unit = {},
    brightnessLevel: Float,
    onVolumeChange: (value: Float) -> Unit = {},
    volumeLevel: Float,
    playerMode: PlayerModes,
) {
    val context = LocalContext.current

    var isPlayingValue by remember {
        mutableStateOf(false)
    }

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
            if (isInFullPlayerMode) {
                Column {
                    IconButton(onClick = { }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_brightness_medium),
                            contentDescription = "Brightness Level",
                        )
                    }
                    HSlider(
                        modifier = Modifier.fillMaxHeight(0.7f),
                        defaultValue = brightnessLevel,
                        onValueChange = {
                            context.changeBrightnessLevel(percent = it)
                            onBrightnessChange(it)
                        })
                }
            }

            // replay button
            Box(modifier = Modifier.height(48.dp).weight(1f)) {
                Box(
                    Modifier
                        .align(Alignment.CenterEnd)
                ) {
                    DoubleTapToForwardIcon(isForward = false){
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
            IconButton(modifier = Modifier.size(40.dp), onClick = {
                isPlayingValue = !isPlayingValue
                onPlayPauseToggle(isPlayingValue)
            }) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    painter = if (isPlayingValue) {
                        painterResource(id = R.drawable.ic_play_triangle)
                    } else {
                        painterResource(id = R.drawable.ic_pause)
                    },
                    contentDescription = "Play/Pause",
                )
            }

            // forward button

            Box(modifier = Modifier.height(48.dp).weight(1f)) {
                Box(modifier = Modifier.fillMaxWidth()){
                    DoubleTapToForwardIcon(isForward = true){
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
            if (isInFullPlayerMode) {
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

@Preview(heightDp = 360, widthDp = 800)
@Composable
fun PlayerControlsFillPlayerPreview() {
    CenterPlayerControls(
        brightnessLevel = 50f,
        volumeLevel = 0f,
        playerMode = PlayerModes.FULL_PLAYER,
        currentDuration = 100000,
        totalDuration = 500000
    )
}

@Preview
@Composable
fun PlayerControlsPreview() {
    Column(modifier = Modifier.fillMaxHeight()) {
        CenterPlayerControls(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            brightnessLevel = 50f,
            volumeLevel = 0f,
            playerMode = PlayerModes.MINI_PLAYER,
            currentDuration = 100000,
            totalDuration = 500000,
        )
    }
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
    updateFrequency: Duration = 1.seconds,
) = flow {
    while (true) {
        if (isPlaying) emit(currentPosition)
        delay(updateFrequency)
    }
}.flowOn(Dispatchers.Main)

fun Long.formatMinSec(): String {
    return if (this == 0L) {
        "..."
    } else {
        String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(this),
            TimeUnit.MILLISECONDS.toSeconds(this) -
                    TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(this),
                    ),
        )
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
    Log.d("TAG", "getCurrentBrightness: $curBrightness")
    curBrightness /= 100f
    if (curBrightness > 1) {
        curBrightness = 1f
    } else if (curBrightness < 0) {
        curBrightness = 0f
    }
    return curBrightness
}
package com.example.exoplayerplus

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView


@Composable
fun CustomExoplayer(
    modifier: Modifier = Modifier,
    mediaUrl: String = "",
    exoplayerBuilder: ExoPlayer.Builder? = null
) {
    val context = LocalContext.current
    var areControlsVisible by remember {
        mutableStateOf(false)
    }
    val exoPlayer = remember {
        exoplayerBuilder?.build()
            ?: ExoPlayer.Builder(context).build().apply {
                setMediaItem(
                    MediaItem.fromUri(mediaUrl)
                )
                prepare()
                playWhenReady = true
            }

    }

    Box(modifier = modifier) {
        DisposableEffect(key1 = Unit) { onDispose { exoPlayer.release() } }

        AndroidView(
            modifier = Modifier.clickable {
                areControlsVisible = !areControlsVisible
            },
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = false
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                }
            }
        )
        Column(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = areControlsVisible,
                enter = fadeIn(), exit = fadeOut()
            ) {
                CenterPlayerControls(
                    onReplayClick = { exoPlayer.seekTo(exoPlayer.currentPosition - 15000) },
                    onPlayPauseToggle = {

                    },
                    onForwardClick = {
                        exoPlayer.seekTo(exoPlayer.currentPosition + 15000)
                    },
                    onBrightnessChange = {

                    },
                    brightnessLevel = 50f
                )
            }
        }


    }

}

@Composable
fun CenterPlayerControls(
    modifier: Modifier = Modifier,
    onReplayClick: () -> Unit = {},
    onPlayPauseToggle: (isPlaying: Boolean) -> Unit = {},
    onForwardClick: () -> Unit = {},
    onBrightnessChange: (value: Float) -> Unit = {},
    brightnessLevel: Float,
    isInFullPlayerMode: Boolean = true
) {
    val isPlayingValue by remember {
        mutableStateOf(false)
    }


    //black overlay across the video player
    Box(modifier = modifier.background(Color.Black.copy(alpha = 0.6f))) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            //Brightness Slider
            if (isInFullPlayerMode)
            {
                Column {
                    IconButton(onClick = { }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_brightness_medium),
                            contentDescription = "Brightness Level"
                        )
                    }
                    HSlider(defaultValue = 0.5f, onValueChange = {})
                }
            }


            //replay button
            IconButton(modifier = Modifier.size(40.dp), onClick = {
                onReplayClick()
            }) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = R.drawable.ic_exo_icon_rewind),
                    contentDescription = "Replay 15 seconds"
                )
            }

            //pause/play toggle button
            IconButton(modifier = Modifier.size(40.dp), onClick = {
                onPlayPauseToggle(isPlayingValue)
            }) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    painter = if (isPlayingValue) painterResource(id = R.drawable.ic_play_triangle) else painterResource(
                        id = R.drawable.ic_pause
                    ),
                    contentDescription = "Play/Pause"
                )
            }

            //forward button
            IconButton(modifier = Modifier.size(40.dp), onClick = {
                onForwardClick()
            }) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = R.drawable.ic_exo_icon_fastforward),
                    contentDescription = "Forward 10 seconds"
                )
            }

            //Volume Slider
            if (isInFullPlayerMode) {
                Column {
                    IconButton(onClick = { }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_volume_high),
                            contentDescription = "Volume Level"
                        )
                    }
                    HSlider(defaultValue = 0.5f, onValueChange = {})
                }
            }
        }
    }
}

@Preview(heightDp = 360, widthDp = 800)
@Composable
fun PlayerControlsFillPlayerPreview() {
    CenterPlayerControls(brightnessLevel = 50f, isInFullPlayerMode = true)
}

@Preview
@Composable
fun PlayerControlsPreview() {
    CenterPlayerControls(brightnessLevel = 50f, isInFullPlayerMode = false)
}
package com.example.exoplayerplus

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.delay


@Composable
fun CustomExoplayer(
    modifier: Modifier = Modifier,
    mediaUrl: String = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    exoplayerBuilder: ExoPlayer.Builder? = null,
) {
    val context = LocalContext.current
    var areControlsVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = areControlsVisible) {
        if (areControlsVisible) {
            delay(5000)
            areControlsVisible = false
        }
    }
    var totalDuration by remember {
        mutableLongStateOf(0L)
    }

    var brightnessLevel by remember {
        mutableFloatStateOf(context.getCurrentBrightness())
    }

    val exoPlayer = remember {
        exoplayerBuilder?.build()
            ?: ExoPlayer.Builder(context).build().apply {
                setMediaItem(
                    MediaItem.fromUri(mediaUrl),
                )
                prepare()
                addListener(object : Player.Listener {
                    override fun onEvents(player: Player, events: Player.Events) {
                        totalDuration = player.duration
                    }
                })
                playWhenReady = true
            }
    }

    val currentExoplayerPosition = exoPlayer.currentPositionFlow().collectAsState(initial = 0L)

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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                }
            },
        )
        Column(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = areControlsVisible,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                CenterPlayerControls(
                    modifier = Modifier.fillMaxSize(),
                    onReplayClick = { exoPlayer.seekTo(exoPlayer.currentPosition - 15000) },
                    onPlayPauseToggle = { isPlaying ->
                        if (isPlaying) {
                            exoPlayer.pause()
                        } else {
                            exoPlayer.play()
                        }
                    },
                    onForwardClick = {
                        exoPlayer.seekTo(exoPlayer.currentPosition + 15000)
                    },
                    onBrightnessChange = {
                        brightnessLevel = it
                    },
                    brightnessLevel = brightnessLevel,
                    volumeLevel = 50f,
                    onSeekBarValueChange = { percent ->
                        exoPlayer.seekTo((exoPlayer.duration * percent).toLong())
                    },
                    currentDuration = currentExoplayerPosition.value,
                    totalDuration = totalDuration,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ExoplayerPreview() {
    CustomExoplayer(
        modifier = Modifier.fillMaxSize()
    )
}



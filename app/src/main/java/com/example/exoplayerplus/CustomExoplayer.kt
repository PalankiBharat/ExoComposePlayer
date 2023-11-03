package com.example.exoplayerplus

import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomExoplayer(
    modifier: Modifier = Modifier,
    mediaUrl: String = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
    exoplayerBuilder: ExoPlayer.Builder? = null,
    onFullScreenClick: () -> Unit = {},
    getExoplayer: (ExoPlayer) -> Unit = {},
    playerModes: PlayerModes = PlayerModes.MINI_PLAYER
) {
    val context = LocalContext.current
    val activity = context as Activity
    var areControlsVisible by remember {
        mutableStateOf(false)
    }
    val playerMode by remember {
        mutableStateOf(playerModes)
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

    var videPlaybackPosition by remember {
        mutableFloatStateOf(0f)
    }

    val exoPlayer = remember {
        exoplayerBuilder?.build() ?: ExoPlayer.Builder(context).build().apply {
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
    LaunchedEffect(key1 = exoPlayer) {
        getExoplayer(exoPlayer)
    }

    val currentExoplayerPosition = exoPlayer.currentPositionFlow().collectAsState(initial = 0L)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .then(modifier)
    ) {
        DisposableEffect(key1 = Unit) { onDispose { exoPlayer.release() } }
        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                val width = this.size.width
                detectTapGestures(onTap = {
                    areControlsVisible = !areControlsVisible
                }, onDoubleTap = {
                    if (it.x > width / 2) {
                        Log.d("TAG", "CustomExoplayer: Double tap on right")
                        exoPlayer.seekTo(exoPlayer.currentPosition + 10000)
                    } else {
                        Log.d("TAG", "CustomExoplayer: Double tap on left")
                        exoPlayer.seekTo(exoPlayer.currentPosition - 10000)

                    }
                })
            }) {

        }
        AndroidView(
            modifier = Modifier.combinedClickable(
                onClick = {
                    areControlsVisible = !areControlsVisible
                },
                onLongClick = {

                },
                onDoubleClick = {

                },
            ),
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
                        // videPlaybackPosition = percent
                        exoPlayer.seekTo((exoPlayer.duration * percent).toLong())
                    },
                    currentDuration = currentExoplayerPosition.value,
                    totalDuration = totalDuration,
                    playerMode = playerMode

                    // seekbarPosition = videPlaybackPosition
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


enum class PlayerModes {
    FULL_PLAYER, MINI_PLAYER
}



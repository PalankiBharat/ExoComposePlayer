package com.example.exoplayerplus

import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
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
                    },
                    brightnessLevel = 50f,
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
    isInFullPlayerMode: Boolean = true,
) {
    var isPlayingValue by remember {
        mutableStateOf(false)
    }

    var seekbarPosition by remember {
        mutableStateOf(0f)
    }

    LaunchedEffect(key1 = currentDuration) {
        seekbarPosition = (currentDuration.toFloat() / totalDuration.toFloat())
        Log.d("TAG", "CenterPlayerControls: a $currentDuration  $totalDuration" + seekbarPosition)
    }

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
                    HSlider(modifier = Modifier.fillMaxHeight(0.7f), defaultValue = 0.5f, onValueChange = {})
                }
            }

            // replay button
            IconButton(modifier = Modifier.size(40.dp), onClick = {
                onReplayClick()
            }) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = R.drawable.ic_exo_icon_rewind),
                    contentDescription = "Replay 15 seconds",
                )
            }

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
            IconButton(modifier = Modifier.size(40.dp), onClick = {
                onForwardClick()
            }) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = R.drawable.ic_exo_icon_fastforward),
                    contentDescription = "Forward 10 seconds",
                )
            }

            // Volume Slider
            if (isInFullPlayerMode) {
                Column {
                    IconButton(onClick = { }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_volume_high),
                            contentDescription = "Volume Level",
                        )
                    }
                    HSlider(Modifier.fillMaxHeight(0.7f), defaultValue = 0.5f, onValueChange = {})
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
            Text(modifier = Modifier.padding(horizontal = 16.dp), text = currentDuration.formatMinSec(), color = Color.White, fontSize = 12.sp)
            Slider(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 14.dp),
                value = seekbarPosition,
                onValueChange = {
                    seekbarPosition = it
                    onSeekBarValueChange(it)
                },
            )
            Text(modifier = Modifier.padding(horizontal = 16.dp), text = totalDuration.formatMinSec(), color = Color.White, fontSize = 12.sp)
        }
    }
}

@Preview(heightDp = 360, widthDp = 800)
@Composable
fun PlayerControlsFillPlayerPreview() {
    CenterPlayerControls(brightnessLevel = 50f, isInFullPlayerMode = true, currentDuration = 100000, totalDuration = 500000)
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
            isInFullPlayerMode = false,
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
        Log.d("TAG", "currentPositionFlow: " + currentPosition)
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

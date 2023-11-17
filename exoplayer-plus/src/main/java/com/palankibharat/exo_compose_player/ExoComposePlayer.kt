package com.palankibharat.exo_compose_player

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun ExoComposePlayer(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(16f / 9f),
    mediaUrl: String = "",
    playerControllerStyle: PlayerControlsStyle = PlayerDefaults.defaultPlayerControls,
    playerControlsConfiguration: PlayerControlsConfiguration = PlayerDefaults.defaultPlayerControlsConfiguration,
    //exoplayerBuilder: ExoPlayer.Builder? = null,
    onFullScreenClick: () -> Unit = {},
    //getExoplayer: (ExoPlayer) -> Unit = {},
    playerModes: PlayerModes = PlayerModes.MINI_PLAYER,
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

    LaunchedEffect(key1 = brightnessLevel) {
        //changing brightness level
      //  PlayerUtils().setWindowBrightness(activity, brightnessLevel.toInt())
    }

    var videPlaybackPosition by remember {
        mutableFloatStateOf(0f)
    }

    var isPlayingValue by remember {
        mutableStateOf(false)
    }

    val exoPlayer = remember {
        val subtitle = MediaItem.SubtitleConfiguration.Builder(Uri.parse("https://drive.google.com/uc?export=download&id=1n3VLrbMmEDNvJX48KWxWn8534t_sVPD-"))
            .setMimeType(MimeTypes.APPLICATION_SUBRIP)
            .setLanguage("en")
            .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
            .build()
        // exoplayerBuilder?.build() ?:
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(
               // MediaItem.fromUri(mediaUrl),

                MediaItem.Builder().setUri(RawResourceDataSource.buildRawResourceUri(R.raw.vid)).setSubtitleConfigurations(listOf(subtitle))
                    //.setMimeType(Util.getAdaptiveMimeTypeForContentType(Util.inferContentType(Uri.parse(mediaUrl))))
                    .build()
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
        // getExoplayer(exoPlayer)
    }

    val currentExoplayerPosition = exoPlayer.currentPositionFlow().collectAsState(initial = 0L)

    Box(
        modifier = modifier,
    ) {
        DisposableEffect(key1 = Unit) { onDispose { exoPlayer.release() } }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    val width = this.size.width
                    detectTapGestures(onTap = {
                        areControlsVisible = !areControlsVisible
                    }, onDoubleTap = {
                        if (playerControlsConfiguration.isDoubleTapToForwardBackwardEnabled) {
                            if (it.x > width / 2) {
                                Log.d("TAG", "CustomExoplayer: Double tap on right")
                                exoPlayer.seekForward(playerControlsConfiguration.forwardClickIntervalTime)
                            } else {
                                Log.d("TAG", "CustomExoplayer: Double tap on left")
                                exoPlayer.seekBackward(playerControlsConfiguration.replayClickIntervalTime)
                            }
                        }
                    })
                },
        ) {
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
                   // subtitleView?.visibility = View.VISIBLE
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
                    onReplayClick = { exoPlayer.seekBackward(playerControlsConfiguration.replayClickIntervalTime) },
                    onPlayPauseToggle = { _ ->
                        isPlayingValue = !isPlayingValue
                        if (isPlayingValue) {
                            exoPlayer.pause()
                        } else {
                            exoPlayer.play()
                        }
                    },
                    onForwardClick = {
                        exoPlayer.seekForward(playerControlsConfiguration.forwardClickIntervalTime)
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
                    playerMode = playerMode,
                    playerControlsConfiguration = playerControlsConfiguration,
                    playerControlsStyle = playerControllerStyle,
                    isPlaying = isPlayingValue
                    // seekbarPosition = videPlaybackPosition
                )
            }
        }
    }
}


enum class PlayerModes {
    FULL_PLAYER, MINI_PLAYER
}

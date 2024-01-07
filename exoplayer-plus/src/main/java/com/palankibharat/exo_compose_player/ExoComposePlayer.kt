package com.palankibharat.exo_compose_player

import android.app.Activity
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_FILL
import androidx.media3.ui.PlayerView
import com.palankibharat.exo_compose_player.models.PlayerControlsConfiguration
import com.palankibharat.exo_compose_player.models.PlayerControlsStyle
import com.palankibharat.exo_compose_player.models.PlayerDefaults
import com.palankibharat.exo_compose_player.models.Subtitle
import kotlinx.coroutines.delay

val TAG = "ExoComposePlayer"

@OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun ExoComposePlayer(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(16f / 9f),
    mediaUrl: String = "",
    listOfSubtitle: List<Subtitle> = emptyList(),
    playerControllerStyle: PlayerControlsStyle = PlayerDefaults.defaultPlayerControls,
    playerControlsConfiguration: PlayerControlsConfiguration = PlayerDefaults.defaultPlayerControlsConfiguration,
    //exoplayerBuilder: ExoPlayer.Builder? = null,
    //getExoplayer: (ExoPlayer) -> Unit = {},
    playerModes: PlayerModes = PlayerModes.FULL_PLAYER,
    exoPlayerState: ExoPlayerState
) {
    InternalExoPlayer(
        modifier = modifier,
        mediaUrl = mediaUrl,
        playerControllerStyle = playerControllerStyle,
        playerControlsConfiguration = playerControlsConfiguration,
        listOfSubtitle = listOfSubtitle,
        exoPlayerState = exoPlayerState,
    )
}

@OptIn(UnstableApi::class)
@Composable
private fun InternalExoPlayer(
    modifier: Modifier = Modifier,
    mediaUrl: String = "",
    playerControllerStyle: PlayerControlsStyle = PlayerDefaults.defaultPlayerControls,
    playerControlsConfiguration: PlayerControlsConfiguration = PlayerDefaults.defaultPlayerControlsConfiguration,
    //exoplayerBuilder: Media= null,
    listOfSubtitle: List<Subtitle> = emptyList(),
    //getExoplayer: (ExoPlayer) -> Unit = {},,
    exoPlayerState: ExoPlayerState
) {
    val context = LocalContext.current
    val activity = context as Activity


    val playerStates by exoPlayerState

    Log.e("Player",playerStates.toString())


    LaunchedEffect(key1 = playerStates.areControlsVisible) {
        Log.d(TAG, "InternalExoPlayer: ${playerStates.areControlsVisible}")
        if (playerStates.areControlsVisible) {
            delay(5000)
            exoPlayerState.updateState(playerStates.copy(areControlsVisible = false))
        }
    }

    LaunchedEffect(key1 = playerStates.brightnessLevel) {
        //changing brightness level
        PlayerUtils().setWindowBrightness(activity, playerStates.brightnessLevel.toInt() * 225)
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(
                MediaItem.Builder()
                    //.setUri(buildRawResourceUri(R.raw.vid))
                    .setUri(mediaUrl.toUri())
                    .setSubtitleConfigurations(listOfSubtitle.toListOfSubtitleConfiguration())
                    .build()
            )
            prepare()
            addListener(object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    exoPlayerState.updateState(playerStates.copy(totalDuration = player.duration))
                }
            })
            playWhenReady = true
        }
    }

    val currentExoplayerPosition = exoPlayer.currentPositionFlow().collectAsState(initial = 0L)
    Box(
        modifier = if (playerStates.playerMode == PlayerModes.FULL_PLAYER) Modifier.fillMaxSize() else modifier,
    ) {
        DisposableEffect(key1 = Unit) {
            onDispose {
                exoPlayer.release()
                exoPlayer.stop()
            }
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    val width = this.size.width
                    detectTapGestures(onTap = {
                        exoPlayerState.updateState(playerStates.copy(areControlsVisible = !playerStates.areControlsVisible))
                        // playerStates.areControlsVisible = !playerStates.areControlsVisible
                    }, onDoubleTap = {
                        if (playerControlsConfiguration.isDoubleTapToForwardBackwardEnabled) {
                            if (it.x > width / 2) {
                                exoPlayer.seekForward(playerControlsConfiguration.forwardClickIntervalTime)
                            } else {
                                exoPlayer.seekBackward(playerControlsConfiguration.replayClickIntervalTime)
                            }
                        }
                    })
                },
        ) {}
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = false
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    resizeMode = RESIZE_MODE_FILL
                }
            },
        )
        Column(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = playerStates.areControlsVisible,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                CenterPlayerControls(
                    modifier = Modifier.fillMaxSize(),
                    onReplayClick = { exoPlayer.seekBackward(playerControlsConfiguration.replayClickIntervalTime) },
                    onPlayPauseToggle = {
                        exoPlayerState.updateState(playerStates.copy(isPlayingValue = !playerStates.isPlayingValue))
                        if (playerStates.isPlayingValue) {
                            exoPlayer.pause()
                        } else {
                            exoPlayer.play()
                        }
                    },
                    onForwardClick = {
                        exoPlayer.seekForward(playerControlsConfiguration.forwardClickIntervalTime)
                    },
                    onBrightnessChange = {
                        exoPlayerState.updateState(playerStates.copy(brightnessLevel = it))
                    },
                    brightnessLevel = playerStates.brightnessLevel,
                    volumeLevel = 50f,
                    onSeekBarValueChange = { percent ->
                        exoPlayer.seekTo((exoPlayer.duration * percent).toLong())
                    },
                    currentDuration = currentExoplayerPosition.value,
                    totalDuration = playerStates.totalDuration,
                    playerMode = playerStates.playerMode,
                    playerControlsConfiguration = playerControlsConfiguration,
                    playerControlsStyle = playerControllerStyle,
                    isPlaying = playerStates.isPlayingValue,
                    onPlayerModeChangeClick = {

                        exoPlayerState.updateState(
                            playerStates.copy(
                                playerMode =
                                if (playerStates.playerMode == PlayerModes.FULL_PLAYER) {
                                    PlayerModes.MINI_PLAYER
                                } else PlayerModes.FULL_PLAYER
                            )
                        )
                    }
                )
            }
        }
    }
}


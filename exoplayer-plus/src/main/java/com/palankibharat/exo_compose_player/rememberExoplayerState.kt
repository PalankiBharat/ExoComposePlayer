package com.palankibharat.exo_compose_player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext


/**
 * Provides a composable to remember the ExoPlayer state with ability to save and restore
 * its instance state across configuration changes.
 * It utilizes the rememberSaveable composable to automatically save and restore the ExoPlayer state.
 * @return [ExoPlayerState] instance with the initial brightness level set from the current context.
 */
@Composable
fun rememberExoplayerState(): ExoPlayerState {
    val context = LocalContext.current
    val defaultBrightness = context.getCurrentBrightness()

    return rememberSaveable(saver = ExoPlayerState.ExoPlayerStateSaver) {
        ExoPlayerState(
            initialBrightnessLevel = defaultBrightness,
        )
    }
}

/**
 * Represents the state of an ExoPlayer with properties for UI control visibility,
 * player mode, total duration, brightness level, and playback status.
 * It implements MutableState to allow for state observation and updates.
 * @property initialBrightnessLevel Initial brightness level of the player.
 */
class ExoPlayerState(
    initialBrightnessLevel: Float = 0f,
) : MutableState<PlayerStates> {

    private val mutableState = mutableStateOf(
        PlayerStates(
            brightnessLevel = initialBrightnessLevel,
        )
    )

    override var value: PlayerStates
        get() = mutableState.value
        set(newValue) {
            mutableState.value = newValue
        }

    override fun component1(): PlayerStates = mutableState.value

    override fun component2(): (PlayerStates) -> Unit = { newValue -> mutableState.value = newValue }

    /**
     * Updates the current state of the ExoPlayer with new player states.
     * @param newPlayerStates New states to be applied to the player.
     */
    fun updateState(newPlayerStates: PlayerStates) {
        value = newPlayerStates
    }

    companion object{

        val ExoPlayerStateSaver = Saver<ExoPlayerState, List<Any>>(
            save = { state ->
                // Save all properties of PlayerStates in a list
                listOf(
                    state.value.areControlsVisible,
                    state.value.playerMode,
                    state.value.totalDuration,
                    state.value.brightnessLevel,
                    state.value.isPlayingValue,
                )
            },
            restore = { restored ->
                // Restore the state from the list
                ExoPlayerState(
                    initialBrightnessLevel = restored[3] as Float,
                ).apply {
                    // Apply the restored state to the mutable state
                    this.updateState(
                        PlayerStates(
                            areControlsVisible = restored[0] as Boolean,
                            playerMode = restored[1] as PlayerModes,
                            totalDuration = restored[2] as Long,
                            brightnessLevel = restored[3] as Float,
                            isPlayingValue = restored[4] as Boolean,
                        )
                    )
                }
            }
        )


    }



}

/**
 * Data class representing various states of a player like control visibility, player mode,
 * total duration, brightness level, and playback status.
 */
data class PlayerStates(
    val areControlsVisible: Boolean = false,
    val playerMode: PlayerModes = PlayerModes.MINI_PLAYER,
    val totalDuration: Long = 0L,
    val brightnessLevel: Float = 0f,
    val isPlayingValue: Boolean = false,
)

/**
 * Enum class defining player modes such as FULL_PLAYER and MINI_PLAYER.
 */
enum class PlayerModes {
    FULL_PLAYER, MINI_PLAYER
}

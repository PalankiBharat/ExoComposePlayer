package com.palankibharat.exoplayerplus

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.palankibharat.exo_compose_player.ExoComposePlayer
import com.palankibharat.exo_compose_player.PipInitializer
import com.palankibharat.exo_compose_player.PlayerStates
import com.palankibharat.exo_compose_player.rememberExoplayerState
import com.palankibharat.exoplayerplus.ui.theme.ExoPlayerPlusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pip = PipInitializer(this)
        pip.initialize()

        setContent {
            ExoPlayerPlusTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {

                    val exoPlayerState = rememberExoplayerState()

                    // access player state
                    val playerState by exoPlayerState

                    /*
                    to update  player state we can use

                    exoPlayerState.updateState(newPlayerStates = PlayerStates())
                    */

                    Column {
                        ExoComposePlayer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f),
                            mediaUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                            exoPlayerState = exoPlayerState
                        )
                    }
                }
            }
        }
    }
}

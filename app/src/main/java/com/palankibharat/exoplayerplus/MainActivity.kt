package com.palankibharat.exoplayerplus

import android.os.Bundle
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
                    Column {
                        ExoComposePlayer(
                            modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f),
                            mediaUrl = "https://hoichoihlsns.akamaized.net/vhoichoiindia2/Renditions/20231109/1699533862239_6e7734/hls/1699533862239_6e7734.m3u8?hdnts=st=1700127008~exp=1700127308~acl=/*~hmac=0841377ddb865f19fcb2dd3af0e36e19ad147990180101cae4d479ded7d873a4"
                           // mediaUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                        )
                    }
                }
            }
        }
    }
}

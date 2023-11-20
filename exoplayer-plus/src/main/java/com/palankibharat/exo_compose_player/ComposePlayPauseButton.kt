package com.palankibharat.exo_compose_player

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ComposePlayPauseButton(modifier: Modifier, iconColor: Color, isPlaying: Boolean = false) {


    /*var startAnimation by remember {
        mutableStateOf(false)
    }*/

     val startRunningAnimation by remember {
         mutableStateOf(false)
     }

    var pauseLineWidthAnimStateVisibility by remember {
        mutableStateOf(false)
    }

    var radius by remember {
        mutableStateOf(false)
    }

    var width by remember {
        mutableStateOf(0f)
    }

    var height by remember {
        mutableStateOf(0f)
    }

    LaunchedEffect(key1 = isPlaying){

    }


/*    val playAnimState by animateDpAsState(
        targetValue =
        with(LocalDensity.current) {
            if (startRunningAnimation) width.toDp() else (width / 5).toDp()
        },
        label = "", animationSpec = tween(200)
    ) */

    val playAnimState by remember {
       mutableStateOf( Animatable(initialValue = (width / 5) ))
    }

    LaunchedEffect(key1 = isPlaying,){
            playAnimState.animateTo(if (isPlaying) width else width/5, animationSpec = tween(0))
    }
    LaunchedEffect(key1 = startRunningAnimation,){
        playAnimState.animateTo(if (isPlaying) width else width/5, animationSpec = tween(200))
    }


    val secondPauseLineOffsetAnimState by animateDpAsState(
        targetValue =
        with(LocalDensity.current) {
            if (startRunningAnimation) 0.dp else (width - (width / 5)).toDp()
        },
        label = "", animationSpec = tween(200)
    )
    val pauseLineWidthAnimState by animateDpAsState(
        targetValue = if (pauseLineWidthAnimStateVisibility) 0.dp else with(LocalDensity.current) { (width / 5).toDp() },
        label = "",
        animationSpec = tween(200)
    )

    val radiusAnimState by animateDpAsState(
        targetValue = if (radius) 14.dp else 18.dp, label = "", animationSpec = tween(200)
    )


    LaunchedEffect(key1 = startRunningAnimation) {
        radius = if (startRunningAnimation) {
            delay(100)
            true
        } else {
            false
        }
    }

    LaunchedEffect(key1 = startRunningAnimation) {
        pauseLineWidthAnimStateVisibility = if (startRunningAnimation) {
            delay(100)
            true
        } else {
            false
        }
    }



    Canvas(modifier = modifier) {
        height = this.size.height
        width = this.size.width

        val pauseLineOnePath = Path().apply {
            moveTo(0f, 0f)
            lineTo(0f, height)
            lineTo(pauseLineWidthAnimState.value.dp.toPx(), height)
            lineTo(playAnimState.value.toDp().toPx(), height / 2)
            lineTo(pauseLineWidthAnimState.value.dp.toPx(), 0f)
            close()
        }

        val pauseLineTwoPath = Path().apply {
            moveTo(secondPauseLineOffsetAnimState.toPx(), 0f)
            lineTo(secondPauseLineOffsetAnimState.toPx(), height)
            lineTo(secondPauseLineOffsetAnimState.toPx() + pauseLineWidthAnimState.toPx(), height)
            lineTo((width - (width / 5)).toDp().toPx() + pauseLineWidthAnimState.toPx(), height / 2)
            lineTo(secondPauseLineOffsetAnimState.toPx() + pauseLineWidthAnimState.toPx(), 0f)
            close()
        }


        drawIntoCanvas { canvas ->
            canvas.drawPath(path = pauseLineOnePath, paint = Paint().apply {
                this.color = iconColor
                this.style = PaintingStyle.Fill
                this.strokeWidth = strokeWidth
                pathEffect = PathEffect.cornerPathEffect(radiusAnimState.value)
            })
        }

        drawIntoCanvas { canvas ->
            canvas.drawPath(path = pauseLineTwoPath, paint = Paint().apply {
                this.color = iconColor
                this.style = PaintingStyle.Fill
                this.strokeWidth = strokeWidth
                pathEffect = PathEffect.cornerPathEffect(radiusAnimState.value)
            })
        }
    }
}



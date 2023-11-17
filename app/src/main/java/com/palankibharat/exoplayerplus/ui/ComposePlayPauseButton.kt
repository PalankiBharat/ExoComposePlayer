package com.palankibharat.exoplayerplus.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ComposePlayPauseButton(modifier: Modifier, iconColor: Color, isPlaying: Boolean = false) {


    /*var startAnimation by remember {
        mutableStateOf(false)
    }*/

    var isPlaying by remember {
        mutableStateOf(isPlaying)
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


    val playAnimState by animateDpAsState(
        targetValue =
        with(LocalDensity.current) {
            if (isPlaying) width.toDp() else (width / 5).toDp()
        },
        label = "", animationSpec = tween(400)
    )
    val secondPauseLineOffsetAnimState by animateDpAsState(
        targetValue =
        with(LocalDensity.current) {
            if (isPlaying) 0.dp else (width - (width / 5)).toDp()
        },
        label = "", animationSpec = tween(400)
    )
    val pauseLineWidthAnimState by animateDpAsState(
        targetValue = if (pauseLineWidthAnimStateVisibility) 0.dp else with(LocalDensity.current) { (width / 5).toDp() },
        label = "",
        animationSpec = tween(400)
    )

    val radiusAnimState by animateDpAsState(
        targetValue = if (radius) 14.dp else 18.dp, label = "", animationSpec = tween(400)
    )

/*
    LaunchedEffect(key1 = isPlaying) {

    }*/
    LaunchedEffect(key1 = isPlaying) {
        radius = if (isPlaying) {
            delay(100)
            true
        } else {
            false
        }
    }

    LaunchedEffect(key1 = isPlaying) {
        pauseLineWidthAnimStateVisibility = if (isPlaying) {
            delay(100)
            true
        } else {
            false
        }
    }



    Canvas(modifier = modifier
        .clickable {
           // isPlaying = !isPlaying
        }
        .background(Color.White)) {
        height = this.size.height
        width = this.size.width

        val pauseLineOnePath = Path().apply {
            moveTo(0f, 0f)
            lineTo(0f, height)
            lineTo(pauseLineWidthAnimState.value.dp.toPx(), height)
            lineTo(playAnimState.value.dp.toPx(), height / 2)
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
                this.color = Color.Red
                this.style = PaintingStyle.Fill
                this.strokeWidth = strokeWidth
                pathEffect = PathEffect.cornerPathEffect(radiusAnimState.value)
            })
        }

        drawIntoCanvas { canvas ->
            canvas.drawPath(path = pauseLineTwoPath, paint = Paint().apply {
                this.color = Color.Red
                this.style = PaintingStyle.Fill
                this.strokeWidth = strokeWidth
                pathEffect = PathEffect.cornerPathEffect(radiusAnimState.value)
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Prevvv() {
    Box(
        modifier = Modifier
            .height(100.dp)
            .width(100.dp)
    ) {
        var isPlaying by remember {
            mutableStateOf(false)
        }
        ComposePlayPauseButton(
            modifier = Modifier
                .align(Alignment.Center)
                .clickable {
                    isPlaying = !isPlaying
                }
                .fillMaxSize(),
            Color.Black,
            isPlaying
        )

    }
}

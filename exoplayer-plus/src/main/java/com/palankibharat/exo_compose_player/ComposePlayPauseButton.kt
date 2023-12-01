package com.palankibharat.exo_compose_player

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
fun ComposePlayPauseButton(
    modifier: Modifier,
    iconColor: Color,
    isVideoPlaying: Boolean = false,
    onClick: () -> Unit
) {

    // isPlaying = true -> Pause Icon
    // isPlaying = true -> Play Icon

    val isPlaying by remember {
        derivedStateOf { isVideoPlaying }
    }

    var startRunningAnimation by remember {
        mutableStateOf(isPlaying)
    }

    var radius by remember {
        mutableStateOf(false)
    }

    var width by remember {
        mutableFloatStateOf(0f)
    }

    var height by remember {
        mutableFloatStateOf(0f)
    }
    
    var pauseLineWidth by remember {
        mutableFloatStateOf(width/5)
    }
    LaunchedEffect(key1 = width ){
        pauseLineWidth = width/5
    }

    val playAnimState by remember {
        mutableStateOf(Animatable(initialValue = if (isPlaying) pauseLineWidth else width))
    }

    val secondPauseLineOffsetAnimState by remember {
        mutableStateOf(Animatable(initialValue = if (isPlaying) (width - pauseLineWidth) else 0f))
    }

    val pauseLineWidthAnimState by remember {
        mutableStateOf(Animatable(initialValue = if (isPlaying) 0f else pauseLineWidth))
    }

    LaunchedEffect(key1 = isPlaying) {
        playAnimState.snapTo(if (isPlaying) width else pauseLineWidth)
        secondPauseLineOffsetAnimState.snapTo(if (isPlaying) 0f else (width - pauseLineWidth))
        pauseLineWidthAnimState.snapTo(if (isPlaying) 0f else pauseLineWidth)
    }
    LaunchedEffect(key1 = startRunningAnimation) {
        playAnimState.animateTo(
            if (startRunningAnimation) width else width / 5,
            animationSpec = tween(200)
        )
    }

    LaunchedEffect(key1 = startRunningAnimation) {
        secondPauseLineOffsetAnimState.animateTo(
            if (startRunningAnimation) 0f else (width - pauseLineWidth),
            animationSpec = tween(200)
        )
    }

    LaunchedEffect(key1 = startRunningAnimation) {
        pauseLineWidthAnimState.animateTo(
            if (startRunningAnimation) 0f else pauseLineWidth,
            animationSpec = tween(200)
        )
    }


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

    Canvas(modifier = modifier.clickable {
        startRunningAnimation = !startRunningAnimation
        onClick()
    }) {
        height = this.size.height
        width = this.size.width

        val pauseLineOnePath = Path().apply {
            moveTo(0f, 0f)
            lineTo(0f, height)
            lineTo(pauseLineWidthAnimState.value.toDp().toPx(), height)
            lineTo(playAnimState.value.toDp().toPx(), height / 2)
            lineTo(pauseLineWidthAnimState.value.toDp().toPx(), 0f)
            close()
        }

        val pauseLineTwoPath = Path().apply {
            moveTo(secondPauseLineOffsetAnimState.value.toDp().toPx(), 0f)
            lineTo(secondPauseLineOffsetAnimState.value.toDp().toPx(), height)
            lineTo(
                secondPauseLineOffsetAnimState.value.toDp()
                    .toPx() + pauseLineWidthAnimState.value.toDp().toPx(),
                height
            )
            lineTo(
                (width - pauseLineWidth).toDp().toPx() + pauseLineWidthAnimState.value.toDp().toPx(),
                height / 2
            )
            lineTo(
                secondPauseLineOffsetAnimState.value.toDp()
                    .toPx() + pauseLineWidthAnimState.value.toDp().toPx(),
                0f
            )
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



package com.palankibharat.exo_compose_player

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.palankibharat.exo_compose_player.models.PlayerDefaults

@Composable
fun DoubleTapToForwardIcon(
    isForward: Boolean = true,
    forwardIntervalTime: Long = 10,
    replayIntervalTime: Long = 10,
    color: Color = PlayerDefaults.defaultPlayerControls.centerControlColors,
    onClick: () -> Unit = {}
) {
    val rotation by remember {
        mutableStateOf(Animatable(0f))
    }

    val sliding by remember {
        mutableStateOf(Animatable(0f))
    }

    val alpha by remember {
        mutableStateOf(Animatable(100f))
    }

    var animationRunning by remember { mutableStateOf(false) }

    var height by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(animationRunning) {
        if (animationRunning) {
            rotation.animateTo(
                targetValue = 60f,
                animationSpec = tween(durationMillis = 150, easing = FastOutLinearInEasing),
            )
        }
    }
    LaunchedEffect(animationRunning) {
        if (animationRunning) {
            sliding.animateTo(
                targetValue = 100f,
                animationSpec = tween(400, easing = LinearEasing),
            )
        }
    }
    LaunchedEffect(animationRunning) {
        if (animationRunning) {
            alpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(400, easing = LinearEasing),
            )
        }
    }

    LaunchedEffect(sliding.value) {
        if (!sliding.isRunning) {
            sliding.snapTo(0f)
            alpha.snapTo(100f)
            animationRunning = false
        }
    }

    LaunchedEffect(rotation.value) {
        if (!rotation.isRunning) {
            rotation.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 40, easing = FastOutLinearInEasing),
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(interactionSource = MutableInteractionSource(), indication = null) {

            },
    ) {
        // Icon Box
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .run {
                    if (!isForward) {
                        this.align(Alignment.CenterEnd)
                    } else {
                        this.align(Alignment.CenterStart)
                    }
                }
                .onGloballyPositioned {
                    height = it.size.height
                },

            ) {
            Icon(
                modifier = Modifier
                    .run {
                        if (!isForward) {
                            this.graphicsLayer {
                                rotationY = 180f
                            }
                        } else {
                            this
                        }
                    }
                    .rotate(-60f)
                    .rotate(rotation.value)
                    .align(Alignment.Center)
                    .clickable {
                        animationRunning = true
                        onClick()
                    },
                contentDescription = "",
                tint = color,
                painter = painterResource(id = R.drawable.forward_only),
            )
            if (alpha.value == 100f) {
                val text = if (isForward) {
                    "$forwardIntervalTime"
                } else {
                    "$replayIntervalTime"
                }
                Text(
                    text = text,
                    color = color,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(4.dp),
                )
            }
        }

        // ++ or -- Text
        if (alpha.value < 100) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = with(LocalDensity.current) { height.toDp() }),
            ) {
                val text = if (isForward) {
                    "++$forwardIntervalTime"
                } else {
                    "--$replayIntervalTime"
                }
                Text(
                    text = text,
                    color = color,
                    modifier = Modifier
                        .alpha(alpha.value / 100)
                        .run {
                            if (isForward) {
                                this
                                    .align(Alignment.CenterStart)
                                    .padding(start = 14.dp + sliding.value.toInt().dp)
                            } else {
                                this
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 14.dp + sliding.value.toInt().dp)
                            }
                        },
                )
            }
        }
    }
}

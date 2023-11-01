package com.example.exoplayerplus

import androidx.annotation.FloatRange
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HSlider(modifier: Modifier = Modifier ,@FloatRange(from = 0.0, to = 1.0) defaultValue: Float, onValueChange: (Float) -> Unit,sliderStyle: SliderStyle = SliderStyle.DefaultSliderStyle) {
    Column {
        var sliderPosition by remember { mutableStateOf(defaultValue) }
        Slider(
            modifier = modifier
                .graphicsLayer {
                    rotationZ = 270f
                    transformOrigin = TransformOrigin(0f, 0f)
                }
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(
                        Constraints(
                            minWidth = constraints.minHeight,
                            maxWidth = constraints.maxHeight,
                            minHeight = constraints.minWidth,
                            maxHeight = constraints.maxHeight,
                        )
                    )
                    layout(placeable.height, placeable.width) {
                        placeable.place(-placeable.width, 0)
                    }
                }.height(sliderStyle.sliderWidth),
            value = sliderPosition,
            onValueChange = {
                onValueChange(sliderPosition)
                sliderPosition = it
            },
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = MutableInteractionSource(),
                    thumbSize = DpSize.Zero
                )
            },
            colors = SliderDefaults.colors(
                activeTrackColor = sliderStyle.activeTrackColor,
                inactiveTrackColor = sliderStyle.inactiveTrackColor
            ),
            track = {
                SliderDefaults.Track(
                    sliderPositions = it,
                    modifier = Modifier.height(20.dp),
                )
            }

        )
    }

}

@Preview
@Composable
fun HSliderPrev() {
    HSlider(modifier = Modifier,0.4f, {})
}

data class SliderStyle(
    val sliderWidth:Dp,
    val activeTrackColor: Color,
    val inactiveTrackColor: Color,
){
    companion object {
        val DefaultSliderStyle = SliderStyle(50.dp, Color.Blue, Color.Gray)
    }
}
package com.palankibharat.exo_compose_player

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap

@Composable
fun DefaultLoader(modifier: Modifier,color: Color){
    CircularProgressIndicator(
        color = color,
        modifier = modifier,
        strokeCap = StrokeCap.Round
    )
}
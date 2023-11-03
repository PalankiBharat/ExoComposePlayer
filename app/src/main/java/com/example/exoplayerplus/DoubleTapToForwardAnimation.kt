package com.example.exoplayerplus


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun DoubleTapToForwardIcon() {
    val rotation by remember {
        mutableStateOf(Animatable(0f))
    }

    var enabled by remember { mutableStateOf(true) }
    val margin by animateDpAsState(targetValue = if (enabled) 5.dp else 100.dp, label = "", animationSpec = tween(1000, easing = LinearEasing))


    LaunchedEffect(Unit) {
        rotation.animateTo(
            targetValue = 360f,
            animationSpec = tween(500, easing = LinearEasing)
        )

    }
    LaunchedEffect (Unit){
        enabled = false

    }

    LaunchedEffect (enabled){
        if (!enabled)
        {
            enabled = true
        }

    }

    /*  Box {
          Canvas(modifier = Modifier
              .fillMaxSize()
              .aspectRatio(1f)
              , contentDescription = "Double tap to forward") {
              val width = this.size.width
              val height = this.size.height

              val path = Path().apply {
                  drawArc(color = Color.White, useCenter = false, startAngle = 0f, sweepAngle = 300f, style = Stroke(width = 8f), size = Size(width*0.8f,height*0.8f), topLeft = Offset((width-(width*0.8f))/2,(height-(height*0.8f))/2) )
                  moveTo(center.x,center.y)

              }
              val measuredText =
                  textMeasurer.measure(
                      AnnotatedString("10"),
                      constraints = Constraints.fixed(
                          width = (size.width ).toInt(),
                          height = (size.height).toInt()
                      ),
                      style = TextStyle(color = Color.White)
                  )
              drawCircle(radius = width/2,center= center, style = Stroke(width = 8f), color = Color.White)
              drawArc(color = Color.White, useCenter = false, startAngle = 0f, sweepAngle = 300f, style = Stroke(width = 8f), size = Size(width*0.8f,height*0.8f), topLeft = Offset((width-(width*0.8f))/2,(height-(height*0.8f))/2) )

              //drawText(textMeasurer = textMeasurer, text = "10", topLeft = center,style = TextStyle(color = Color.White, fontSize = 16.sp) )
              drawText (measuredText, topLeft = center)
          }
      }*/

    Box(Modifier.fillMaxWidth()) {
        Icon(
            modifier = Modifier
                .rotate(rotation.value)
                .align(Alignment.TopStart),
            contentDescription = "",
            painter = painterResource(id = R.drawable.forward_only)
        )
        Row (modifier = Modifier
            .fillMaxWidth()
            .background(Color.Cyan)
            .align(Alignment.CenterStart)){
            Text(text = "10", modifier = Modifier
                .padding(start = margin))
        }

    }

}

@Preview
@Composable
private fun DoubleTapToForwardIconPreview() {
    DoubleTapToForwardIcon()
}


package com.masterbit.segmentedprogresscomposeactivity

import android.graphics.RadialGradient
import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.progressSemantics
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.masterbit.segmentedprogresscomposeactivity.ui.theme.SegmentedProgressComposeActivityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SegmentedProgressComposeActivityTheme {
                Column(Modifier.fillMaxSize()) {
                    val runForward = remember { mutableStateOf(true) }
                    val progress by animateFloatAsState(targetValue = if (runForward.value) 1f else 0f, animationSpec = tween(10_000, easing = LinearEasing))
                    SegmentedProgressBar(progress, runForward)
                    Spacer(modifier = Modifier.height(32.dp))
                }

            }
        }
    }
}

private const val backgroundOpacity = 0.25f
private val SegmentHeight = 4.dp
private val SegmentGap = 8.dp
private const val NoSegments = 8

@Composable
fun SegmentedProgressBar(progress: Float, runForward: MutableState<Boolean>) {
    Surface(color = MaterialTheme.colors.background) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
            DrawProgressBar(
                progress,
                Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, top = 64.dp, end = 32.dp, bottom = 64.dp)
                )
            Spacer(Modifier.height(32.dp))
            Button(onClick = { runForward.value = !(runForward.value) }) {
                Text("Reverse")
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun DrawProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    backgroundColor: Color = color.copy(alpha = backgroundOpacity),
    segmentHeight: Dp = SegmentHeight,
    segmentGap: Dp = SegmentGap,
    noOfSegments: Int = NoSegments
) {
    val gap: Float
    val height: Float
    with(LocalDensity.current) {
        gap = segmentGap.toPx()
        height = segmentHeight.toPx()
    }
    Canvas(modifier = modifier
        .progressSemantics(value = progress)
        .height(segmentHeight)) {
        drawProgressBar(1f, backgroundColor, gap, height, noOfSegments)
        drawProgressBar(progress, color, gap, height, noOfSegments)
    }
}

fun DrawScope.drawProgressBar(progress: Float, color: Color, gap: Float, strokeWidth: Float, segments: Int) {
    val width = size.width
    val start = 0f
    val gaps = (segments - 1) * gap
    val barSize = width - gaps
    val barWidth = barSize / segments
    val end = barSize * progress + ((segments * progress) * gap)

    repeat(segments) { index ->
        val offset = index * (barWidth + gap)
        val segmentEnd = (offset + barWidth).coerceAtMost(end)
        if (offset < end) {
            drawLine(color, Offset(start + offset, 0f), Offset(segmentEnd, 0f), strokeWidth, pathEffect = PathEffect.cornerPathEffect(10f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SegmentedProgressComposeActivityTheme {
        val runForward = remember { mutableStateOf(true) }
        val progress by animateFloatAsState(targetValue = 0f, animationSpec = tween(10_000, easing = LinearEasing))
        SegmentedProgressBar(progress, runForward = runForward)
    }
}
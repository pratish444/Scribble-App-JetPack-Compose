package com.example.scribble_app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import kotlin.math.abs

@Composable
fun DrawingCanvas(
    paths: List<PathData>,
    currentPath: PathData?,
    onAction: (DrawingAction) -> Unit,
    isDarkMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    val canvasBackground = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val canvasBorder = if (isDarkMode) Color(0xFF333333) else Color(0xFFE0E0E0)

    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(canvasBackground)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .pointerInput(true) {
                    detectDragGestures(
                        onDragStart = { onAction(DrawingAction.OnNewPathStart) },
                        onDragEnd = { onAction(DrawingAction.OnPathEnd) },
                        onDrag = { change, _ -> onAction(DrawingAction.OnDraw(change.position)) },
                        onDragCancel = { onAction(DrawingAction.OnPathEnd) },
                    )
                }
        ) {
            // Draw grid pattern for better visual appeal
            if (isDarkMode) {
                drawGrid(Color(0xFF2A2A2A))
            } else {
                drawGrid(Color(0xFFF5F5F5))
            }

            paths.fastForEach { pathData ->
                drawPath(
                    path = pathData.path,
                    color = pathData.color,
                    thickness = pathData.brushSize
                )
            }
            currentPath?.let {
                drawPath(
                    path = it.path,
                    color = it.color,
                    thickness = it.brushSize
                )
            }
        }
    }
}

private fun DrawScope.drawGrid(gridColor: Color) {
    val gridSize = 40f
    val strokeWidth = 1f

    // Draw vertical lines
    var x = 0f
    while (x <= size.width) {
        drawLine(
            color = gridColor,
            start = Offset(x, 0f),
            end = Offset(x, size.height),
            strokeWidth = strokeWidth
        )
        x += gridSize
    }

    // Draw horizontal lines
    var y = 0f
    while (y <= size.height) {
        drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = strokeWidth
        )
        y += gridSize
    }
}

private fun DrawScope.drawPath(
    path: List<Offset>,
    color: Color,
    thickness: Float = 10f
) {
    if (path.isEmpty()) return

    val smoothedPath = Path().apply {
        if (path.isNotEmpty()) {
            moveTo(path.first().x, path.first().y)
            val smoothness = 5
            for (i in 1..path.lastIndex) {
                val from = path[i - 1]
                val to = path[i]
                val dx = abs(from.x - to.x)
                val dy = abs(from.y - to.y)
                if (dx >= smoothness || dy >= smoothness) {
                    quadraticBezierTo(
                        x1 = (from.x + to.x) / 2f,
                        y1 = (from.y + to.y) / 2f,
                        x2 = to.x,
                        y2 = to.y
                    )
                }
            }
        }
    }

    // Draw shadow for depth effect
    drawPath(
        path = smoothedPath,
        color = color.copy(alpha = 0.3f),
        style = Stroke(
            width = thickness + 2f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )

    // Draw main path
    drawPath(
        path = smoothedPath,
        color = color,
        style = Stroke(
            width = thickness,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}
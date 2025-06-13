package com.example.scribble_app

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DrawingState(
    val selectedColor: Color = Color.Black,
    val currentPath: PathData? = null,
    val paths: List<PathData> = emptyList(),
    val isDarkMode: Boolean = false,
    val brushSize: Float = 10f
)

// Enhanced color palette with more modern colors
val allColors = listOf(
    Color.Black,
    Color(0xFFFF6B6B), // Coral Red
    Color(0xFF4ECDC4), // Turquoise
    Color(0xFF45B7D1), // Sky Blue
    Color(0xFF96CEB4), // Mint Green
    Color(0xFFFECE47), // Sunny Yellow
    Color(0xFFAD6EFF), // Lavender Purple
    Color(0xFFFF8A80), // Light Pink
    Color(0xFF80CBC4), // Teal
    Color(0xFFFFAB40), // Orange
    Color(0xFF9C27B0), // Deep Purple
    Color(0xFF795548)  // Brown
)

data class PathData(
    val id: String,
    val color: Color,
    val path: List<Offset>,
    val brushSize: Float = 10f
)

sealed interface DrawingAction {
    data object OnNewPathStart: DrawingAction
    data class OnDraw(val offset: Offset): DrawingAction
    data object OnPathEnd: DrawingAction
    data class OnSelectColor(val color: Color): DrawingAction
    data object OnClearCanvasClick: DrawingAction
    data object OnToggleDarkMode: DrawingAction
    data class OnBrushSizeChange(val size: Float): DrawingAction
    data object OnUndo: DrawingAction
}

class DrawingViewModel: ViewModel() {
    private val _state = MutableStateFlow(DrawingState())
    val state = _state.asStateFlow()

    fun onAction(action: DrawingAction) {
        when(action) {
            DrawingAction.OnClearCanvasClick -> onClearCanvasClick()
            is DrawingAction.OnDraw -> onDraw(action.offset)
            DrawingAction.OnNewPathStart -> onNewPathStart()
            DrawingAction.OnPathEnd -> onPathEnd()
            is DrawingAction.OnSelectColor -> onSelectColor(action.color)
            DrawingAction.OnToggleDarkMode -> onToggleDarkMode()
            is DrawingAction.OnBrushSizeChange -> onBrushSizeChange(action.size)
            DrawingAction.OnUndo -> onUndo()
        }
    }

    private fun onSelectColor(color: Color) {
        _state.update { it.copy(selectedColor = color) }
    }

    private fun onPathEnd() {
        val currentPathData = state.value.currentPath ?: return
        _state.update { it.copy(
            currentPath = null,
            paths = it.paths + currentPathData
        ) }
    }

    private fun onNewPathStart() {
        _state.update { it.copy(
            currentPath = PathData(
                id = System.currentTimeMillis().toString(),
                color = it.selectedColor,
                path = emptyList(),
                brushSize = it.brushSize
            )
        ) }
    }

    private fun onDraw(offset: Offset) {
        val currentPathData = state.value.currentPath ?: return
        _state.update { it.copy(
            currentPath = currentPathData.copy(path = currentPathData.path + offset)
        ) }
    }

    private fun onClearCanvasClick() {
        _state.update { it.copy(currentPath = null, paths = emptyList()) }
    }

    private fun onToggleDarkMode() {
        _state.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    private fun onBrushSizeChange(size: Float) {
        _state.update { it.copy(brushSize = size) }
    }

    private fun onUndo() {
        _state.update {
            it.copy(
                paths = if (it.paths.isNotEmpty()) it.paths.dropLast(1) else it.paths,
                currentPath = null
            )
        }
    }
}
package com.example.scribble_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scribble_app.ui.theme.DrawingInJetpackComposeTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = viewModel<DrawingViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            DrawingInJetpackComposeTheme(darkTheme = state.isDarkMode) {
                val backgroundColor = if (state.isDarkMode) {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0F0F23),
                            Color(0xFF1A1A2E),
                            Color(0xFF16213E)
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF667eea),
                            Color(0xFF764ba2),
                            Color(0xFF8B5FBF)
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .navigationBarsPadding(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Top spacer for better visual balance
                        Spacer(modifier = Modifier.height(16.dp))

                        // Main drawing canvas
                        DrawingCanvas(
                            paths = state.paths,
                            currentPath = state.currentPath,
                            onAction = viewModel::onAction,
                            isDarkMode = state.isDarkMode,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 16.dp)
                        )

                        // Enhanced controls
                        CanvasControls(
                            selectedColor = state.selectedColor,
                            colors = allColors,
                            brushSize = state.brushSize,
                            isDarkMode = state.isDarkMode,
                            onSelectColor = {
                                viewModel.onAction(DrawingAction.OnSelectColor(it))
                            },
                            onClearCanvas = {
                                viewModel.onAction(DrawingAction.OnClearCanvasClick)
                            },
                            onToggleDarkMode = {
                                viewModel.onAction(DrawingAction.OnToggleDarkMode)
                            },
                            onBrushSizeChange = { size ->
                                viewModel.onAction(DrawingAction.OnBrushSizeChange(size))
                            },
                            onUndo = {
                                viewModel.onAction(DrawingAction.OnUndo)
                            }
                        )
                    }
                }
            }
        }
    }

    private fun enableEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}
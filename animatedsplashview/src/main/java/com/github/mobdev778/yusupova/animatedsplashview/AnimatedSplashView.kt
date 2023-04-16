package com.github.mobdev778.yusupova.animatedsplashview

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Suppress("ComplexCondition", "FunctionNaming")
@Composable
fun AnimatedSplashView(
    bitmap: ImageBitmap,
    modifier: Modifier = Modifier,
) {
    var job: Job? = null
    val backgroundScope: CoroutineScope by lazy {
        CoroutineScope(Executors.newFixedThreadPool(1).asCoroutineDispatcher())
    }

    val bitmapPaint = Paint().apply { isAntiAlias = true }
    var state: AnimatedSplashState by remember { mutableStateOf(AnimatedSplashState()) }

    Canvas(
        modifier
    ) {

        val width = size.width.toInt()
        val height = size.height.toInt()

        if (!state.isInitialized() && job?.isActive != true && width > 0 && height > 0) {
            job = backgroundScope.launch {
                val androidBitmap = bitmap.asAndroidBitmap().getResizedBitmap(width, height)
                state = state.initialize(androidBitmap)
            }
        }

        if (state.isInitialized()) {
            state.getBitmap()?.let { bitmap ->
                drawIntoCanvas {
                    it.nativeCanvas.drawBitmap(bitmap, 0f, 0f, bitmapPaint)
                    job = backgroundScope.launch {
                        val newState = state.nextState()
                        if (newState != state) {
                            state = newState
                        }
                    }
                }
            }
        }
    }
}

internal const val ANIMATED_SPLASH_VIEW_FRAMES = 75

private fun Bitmap.getResizedBitmap(newWidth: Int, newHeight: Int): Bitmap {
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
}

// @Preview function is use to see preview
// for our composable function in preview section.
@Preview
@Composable
fun AnimatedSplashViewPreview() {
    MaterialTheme {
        // we are passing our composable
        // function to display its preview.
        AnimatedSplashView(
            bitmap = ImageBitmap.imageResource(id = R.drawable.splash_background),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(16.dp),
        )
    }
}

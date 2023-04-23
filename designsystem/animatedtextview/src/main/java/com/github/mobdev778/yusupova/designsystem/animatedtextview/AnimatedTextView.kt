package com.github.mobdev778.yusupova.designsystem.animatedtextview

import android.graphics.Typeface
import android.text.Layout
import android.text.TextPaint
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.math.absoluteValue

@Composable
@Suppress("LongParameterList")
fun AnimatedTextView(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle(),
    mode: AnimatedTextMode = AnimatedTextMode.DEFAULT
) {
    var job: Job? = null
    val backgroundScope: CoroutineScope by lazy {
        CoroutineScope(Executors.newFixedThreadPool(1).asCoroutineDispatcher())
    }

    val textPaint = TextPaint().apply {
        isAntiAlias = true
        textSize = with(LocalDensity.current) { style.fontSize.toPx().absoluteValue }
        color = style.color.toArgb()
        typeface = style.toTypeface()
    }

    var state: AnimatedTextViewState by remember { mutableStateOf(AnimatedTextViewState()) }
    val alignment = style.textAlign?.toAlignment() ?: Layout.Alignment.ALIGN_CENTER

    Canvas(
        modifier
    ) {

        val width = size.width.toInt()
        val height = size.height.toInt()

        if (!state.isInitialized() && job?.isActive != true && width > 0) {
            job = backgroundScope.launch {
                state = state.initialize(mode, text, width, height, textPaint, alignment)
            }
        }

        state.getBitmap()?.let { bitmap ->
            drawIntoCanvas {
                it.nativeCanvas.drawBitmap(bitmap, 0f, 0f, textPaint)
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

private fun TextAlign.toAlignment(): Layout.Alignment {
    return when(this) {
        TextAlign.Center -> Layout.Alignment.ALIGN_CENTER
        TextAlign.Start,
        TextAlign.Left -> Layout.Alignment.ALIGN_NORMAL
        TextAlign.Right,
        TextAlign.End -> Layout.Alignment.ALIGN_OPPOSITE
        else -> Layout.Alignment.ALIGN_NORMAL
    }
}

private fun TextStyle.toTypeface(): Typeface {
    val family = (fontFamily ?: FontFamily.Default).toTypeface()
    val isItalic = fontStyle == FontStyle.Italic
    val isBold = fontWeight == FontWeight.Bold
    val style = when {
        isBold && isItalic -> Typeface.BOLD_ITALIC
        isBold -> Typeface.BOLD
        isItalic -> Typeface.ITALIC
        else -> Typeface.NORMAL
    }
    return Typeface.create(family, style)
}

private fun FontFamily.toTypeface(): Typeface {
    return when (this) {
        FontFamily.Serif -> Typeface.SERIF
        FontFamily.Monospace -> Typeface.MONOSPACE
        FontFamily.SansSerif -> Typeface.SANS_SERIF
        else -> Typeface.DEFAULT
    }
}

// @Preview function is use to see preview
// for our composable function in preview section.
@Preview
@Composable
fun AnimatedTextViewPreview() {
    MaterialTheme {
        // we are passing our composable
        // function to display its preview.
        AnimatedTextView(
            text = "Liliya Yusupova",
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(16.dp),
            style = TextStyle(
                fontSize = 40.sp,
                color = Color(0xFF4D3731),
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Serif,
                shadow = Shadow(
                    color = Color.DarkGray,
                    offset = Offset(2.0f, 2.0f),
                    blurRadius = 3f
                )
            )
        )
    }
}

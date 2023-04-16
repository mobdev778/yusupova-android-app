package com.github.mobdev778.yusupova.ui.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.mobdev778.yusupova.animatedsplashview.AnimatedSplashView
import com.github.mobdev778.yusupova.animatedsplashview.R
import com.github.mobdev778.yusupova.animatedtextview.AnimatedTextView
import com.github.mobdev778.yusupova.ui.theme.YusupovaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YusupovaTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AnimatedSplashView(
                        bitmap = ImageBitmap.imageResource(id = R.drawable.splash_background),
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(1.0f)
                            .padding(16.dp)
                    )
                    Column(
                        modifier = Modifier.fillMaxSize().padding(start = 64.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedTextView(
                            text = stringResource(id = com.github.mobdev778.yusupova.R.string.liliya_yusupova),
                            modifier = Modifier
                                .fillMaxWidth(fraction = 0.8f)
                                .height(54.dp)
                                .padding(top = 16.dp),
                            style = TextStyle(
                                fontSize = 22.sp,
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
                        AnimatedTextView(
                            text = stringResource(id = com.github.mobdev778.yusupova.R.string.verses),
                            modifier = Modifier
                                .fillMaxWidth(fraction = 0.8f)
                                .height(54.dp)
                                .padding(top = 16.dp),
                            style = TextStyle(
                                fontSize = 28.sp,
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
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    YusupovaTheme {
        AnimatedTextView(
            text = "Liliya Yusupova",
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(16.dp),
            style = TextStyle(
                fontSize = 32.sp,
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

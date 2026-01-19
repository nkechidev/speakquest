package com.nkechinnaji.speakquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nkechinnaji.speakquest.ui.SplashScreen
import com.nkechinnaji.speakquest.ui.TranslationScreen
import com.nkechinnaji.speakquest.ui.theme.SpeakQuestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeakQuestTheme {
                SpeakQuestApp()
            }
        }
    }
}

@Composable
fun SpeakQuestApp() {
    var showSplash by remember { mutableStateOf(true) }

    Surface(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = showSplash,
            exit = fadeOut(tween(500))
        ) {
            SplashScreen(
                onSplashComplete = { showSplash = false }
            )
        }

        AnimatedVisibility(
            visible = !showSplash,
            enter = fadeIn(tween(500))
        ) {
            TranslationScreen()
        }
    }
}

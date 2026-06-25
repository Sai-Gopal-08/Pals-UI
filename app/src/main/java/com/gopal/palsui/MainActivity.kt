package com.gopal.palsui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.gopal.adaptive_styled_text.AdaptiveStyledText
import com.gopal.adaptive_styled_text.AdaptiveStyledTextSegment
import com.gopal.adaptive_styled_text.AdaptiveStyledTextSpec
import com.gopal.palsui.ui.theme.PalsUITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PalsUITheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        AdaptiveStyledText(
            spec = AdaptiveStyledTextSpec(
                segments = listOf(
                    AdaptiveStyledTextSegment(
                        text = "Hello",
                        textDecoration = TextDecoration.Underline
                    ),
                    AdaptiveStyledTextSegment(
                        text = " $name!",
                        textDecoration = TextDecoration.LineThrough,
                        fontWeight = FontWeight.Bold
                    )
                )
            )
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PalsUITheme {
        Greeting("Android")
    }
}
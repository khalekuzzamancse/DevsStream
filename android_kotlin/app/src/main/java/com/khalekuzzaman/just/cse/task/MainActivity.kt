package com.khalekuzzaman.just.cse.task
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import com.khalekuzzaman.just.cse.task.ui.theme.DevsStreamTaskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DevsStreamTaskTheme {
                RootNavHost()

            }
        }
    }
}



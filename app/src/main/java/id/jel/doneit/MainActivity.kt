package id.jel.doneit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import id.jel.doneit.ui.AppViewModelProvider
import id.jel.doneit.ui.TaskViewModel
import id.jel.doneit.ui.theme.DoneItTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoneItTheme {
                val viewModel: TaskViewModel = viewModel(factory = AppViewModelProvider.Factory)
            }
        }
    }
}
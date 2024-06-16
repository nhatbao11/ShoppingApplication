package com.project.ecommerceapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.project.ecommerceapplication.ui.theme.EcommerceApplicationTheme
import com.project.ecommerceapplication.view.navigation.NavigationGraph
import com.project.ecommerceapplication.viewmodel.AppViewModel


@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommerceApplicationTheme {
                val context = LocalContext.current
                val appViewModel = viewModel<AppViewModel>(
                    viewModelStoreOwner = context as ComponentActivity
                )
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NavigationGraph(navController = rememberNavController())
                }
            }

        }
    }
}
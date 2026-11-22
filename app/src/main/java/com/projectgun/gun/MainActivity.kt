package com.projectgun.gun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.projectgun.gun.ui.navigation.GunNavigation
import com.projectgun.gun.ui.theme.GunTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as GunApplication

        setContent {
            GunTheme {
                GunNavigation(app = app)
            }
        }
    }
}

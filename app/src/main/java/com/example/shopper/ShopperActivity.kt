package com.example.shopper

import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.shopper.helpers.NotificationHelper


/**
 * Shopper Activity
 *
 * The Main activity that hosts all other fragment in this project
 */
class ShopperActivity : AppCompatActivity() {

    // NavController for handling the back navigation in the app
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopper)

        // Initialize the nav controller
        navController = findNavController(R.id.nav_container)

        // Initialize the notification manager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationHelper.createNotificationChannel(
                this,
                NotificationManager.IMPORTANCE_DEFAULT,
                false,
                "App notification channel."
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

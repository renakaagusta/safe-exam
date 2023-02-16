package com.example.myapplication

import android.app.ActivityManager
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import java.security.AccessController.getContext
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        window.addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY); //(dont forget to add flag before `setContentView`)
        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        val lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE)
        lock.disableKeyguard()

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Do nothing or catch the keys you want to block
        return true
    }

    override fun onPause() {
        super.onPause()
        val activityManager = applicationContext
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.moveTaskToFront(taskId, 0)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideNavigationBar()
    }

    override fun onDestroy() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        super.onDestroy()
    }

    private fun hideNavigationBar() {
        val decorView: View = this.window.decorView
        val uiOptions: Int = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        val timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread { decorView.systemUiVisibility = uiOptions }
            }
        }
        timer.scheduleAtFixedRate(task, 0, 2)
    }
}
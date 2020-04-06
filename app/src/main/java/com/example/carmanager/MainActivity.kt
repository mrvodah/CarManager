package com.example.carmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.example.carmanager.databinding.ActivityMainBinding
import com.example.carmanager.view.ui.home.HomeViewModel
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private val navController: NavController by lazy {
        this.findNavController(R.id.nav_host_fragment)
    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val topDestination = setOf(R.id.homeFragment, R.id.categoryFragment, R.id.historyFragment)

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main);

        drawerLayout = binding.drawerLayout
        navigationView = binding.navView

        textView = navigationView.getHeaderView(0).findViewById(R.id.tv_email)

        appBarConfiguration = AppBarConfiguration.Builder(topDestination)
            .setDrawerLayout(drawerLayout)
            .build()

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        NavigationUI.setupWithNavController(navigationView, navController)

        val viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.email?.observe(this) { value ->
            textView.text = value
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

}

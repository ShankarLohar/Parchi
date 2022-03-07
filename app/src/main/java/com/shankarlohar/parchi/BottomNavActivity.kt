package com.shankarlohar.parchi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.shankarlohar.parchi.databinding.ActivityBottomNavBinding

class BottomNavActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var alert: AlertDialog
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityBottomNavBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.app_title)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_bottom_nav)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_search, R.id.navigation_book, R.id.navigation_my_tickets))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.open, R.string.close)
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.drawerView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.visit_history -> Toast.makeText(
                    this,
                    "Visit history coming soon...",
                    Toast.LENGTH_LONG
                ).show()
                R.id.logout -> logoutFirebase()
                R.id.githubrepo -> redirectToGithubRepo()
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    private fun redirectToGithubRepo() {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse("https://github.com/ShankarLohar/Parchi")
        startActivity(openURL)
    }

    private fun logoutFirebase() {
        alert = AlertDialog.Builder(this).create()
        alert.setTitle("Logout")
        alert.setMessage("Please wait while we log you out from Parchi...")
        alert.setCanceledOnTouchOutside(false)
        auth = FirebaseAuth.getInstance()
        alert.show()
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
    }

}
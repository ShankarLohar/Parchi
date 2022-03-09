package com.shankarlohar.parchi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit

import com.google.firebase.auth.FirebaseAuth
import com.shankarlohar.parchi.databinding.ActivityBottomNavBinding
import com.shankarlohar.parchi.fragments.BookFragment
import com.shankarlohar.parchi.fragments.MyTicketsFragment
import com.shankarlohar.parchi.fragments.SearchFragment

class BottomNavActivity : AppCompatActivity(){

    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var alert: AlertDialog
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityBottomNavBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.app_title)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        binding = ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.navView.setOnItemSelectedListener{
            when (it.itemId) {
                R.id.search_museums -> onSearch()
                R.id.book_ticket -> onBook()
                R.id.booked_ticket -> onBooked()
                else -> false
            }
        }

        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.open, R.string.close)
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        binding.drawerView.setNavigationItemSelectedListener{
            when(it.itemId){
                R.id.visit_history -> {
                    Toast.makeText(this, "Visit history coming soon...", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.logout -> logoutFirebase()
                R.id.githubrepo -> redirectToGithubRepo()
                else -> false
            }
        }
}
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    private fun redirectToGithubRepo(): Boolean {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse("https://github.com/ShankarLohar/Parchi")
        startActivity(openURL)
        return true
    }

    private fun logoutFirebase(): Boolean {
        alert = AlertDialog.Builder(this).create()
        alert.setTitle("Logout")
        alert.setMessage("Please wait while we log you out from Parchi...")
        alert.setCanceledOnTouchOutside(false)
        auth = FirebaseAuth.getInstance()
        alert.show()
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        return true
    }

    private fun onBooked(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.nav_host_fragment_activity_bottom_nav, MyTicketsFragment())
        }
        return true
    }

    private fun onBook(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.nav_host_fragment_activity_bottom_nav, BookFragment())
        }
        return true
    }

    private fun onSearch(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.nav_host_fragment_activity_bottom_nav, SearchFragment())
        }
        return true
    }

}




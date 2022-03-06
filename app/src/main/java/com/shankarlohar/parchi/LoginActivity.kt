package com.shankarlohar.parchi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shankarlohar.parchi.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textViewForgotLoginPassword.setOnClickListener { onForgotPassword() }
        binding.buttonLogin.setOnClickListener { onLogin() }

    }

    private fun onLogin() {
        val loginActivityIntent = Intent(this, BottomNavActivity::class.java)
        startActivity(loginActivityIntent)
    }

    private fun onForgotPassword(){
        val signupActivityIntent = Intent(this, SignupActivity::class.java)
        startActivity(signupActivityIntent)
    }

}
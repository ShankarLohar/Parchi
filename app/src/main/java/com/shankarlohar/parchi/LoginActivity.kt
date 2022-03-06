package com.shankarlohar.parchi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.shankarlohar.parchi.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var alert: AlertDialog
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        alert = AlertDialog.Builder(this).create()
        alert.setTitle("Login")
        alert.setMessage("Please wait while we login you to Parchi...")
        alert.setCanceledOnTouchOutside(false)

        auth = FirebaseAuth.getInstance()
        checkUser()

        binding.textViewCreateAccount.setOnClickListener { onForgotPassword() }
        binding.buttonLogin.setOnClickListener { onLogin() }

    }

    private fun checkUser() {
        val firebaseUser = auth.currentUser
        if (firebaseUser!=null){
            startActivity(Intent(this, BottomNavActivity::class.java))
            finish()
        }
    }

    private fun onLogin() {
        if (validateUser()){
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        alert.show()
        auth.signInWithEmailAndPassword(binding.editTextUserEmail.text.toString().trim(),binding.editTextPassword.text.toString().trim())
            .addOnSuccessListener {
                alert.dismiss()
                val firebaseUser = auth.currentUser
                val userEmail = firebaseUser!!.email
                Toast.makeText(this,"Welcome user $userEmail!",Toast.LENGTH_LONG).show()
                startActivity(Intent(this,BottomNavActivity::class.java))
                finish()
            }.addOnFailureListener{
                alert.dismiss()
                Toast.makeText(this,"Login Failed due to ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun validateUser(): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.editTextUserEmail.text.toString().trim()).matches()){
            binding.editTextUserEmail.error = "Invalid Email Format."
        } else if (TextUtils.isEmpty(binding.editTextPassword.text.toString().trim()) || binding.editTextPassword.text.toString().trim().trim().length < 6){
            binding.editTextPassword.error = "Enter a valid 8 character password."
        }else {
            return true
        }
        return false
    }

    private fun onForgotPassword(){
        startActivity(Intent(this, SignupActivity::class.java))
    }

}
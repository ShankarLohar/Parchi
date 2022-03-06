package com.shankarlohar.parchi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.shankarlohar.parchi.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var alert: AlertDialog
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")
        binding.buttonCreateAccount.setOnClickListener { createUserAccount() }
    }

    private fun createUserAccount() {
        alert = AlertDialog.Builder(this).create()
        if (validate()){
            alert.setTitle("Registration")
            alert.setMessage("Please wait while we register you to Parchi...")
            alert.setCanceledOnTouchOutside(false)
            alert.show()
            saveUserData()
            firebaseSignup()
        }
    }

    private fun saveUserData() {
        val user = Users(binding.editTextName.text.toString().trim(),binding.editTextEmail.text.toString().trim(),binding.editTextPhone.text.toString().trim(),binding.editTextSetConfirmPassword.text.toString().trim())
        database.child(binding.editTextUserName.text.toString().trim()).setValue(user).addOnSuccessListener {
            Toast.makeText(this,"Database Ready for user ${binding.editTextUserName.text.toString().trim()}!",Toast.LENGTH_LONG).show()
        }.addOnFailureListener{
            Toast.makeText(this,"Database Creation Failed due to ${it.message}",Toast.LENGTH_LONG).show()
        }
    }

    private fun firebaseSignup() {
        auth.createUserWithEmailAndPassword(binding.editTextEmail.text.toString().trim(),binding.editTextSetConfirmPassword.text.toString().trim())
            .addOnSuccessListener {
                alert.dismiss()
                val firebaseUser = auth.currentUser
                val userEmail = firebaseUser!!.email
                Toast.makeText(this,"Account Created for user $userEmail!",Toast.LENGTH_LONG).show()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()

        }.addOnFailureListener{
                alert.dismiss()
                Toast.makeText(this,"Sign up Failed due to ${it.message}",Toast.LENGTH_LONG).show()

            }
    }

    private fun validate(): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.editTextEmail.text.toString().trim()).matches()){
            binding.editTextEmail.error = "Invalid Email Format."
        } else if (TextUtils.isEmpty(binding.editTextSetPassword.text.toString().trim()) || binding.editTextSetPassword.text.toString().trim().length < 6){
            binding.editTextSetPassword.error = "Enter a valid 8 character password."
        }else if (binding.editTextSetPassword.text.toString().trim() != binding.editTextSetConfirmPassword.text.toString().trim()){
            binding.editTextSetConfirmPassword.error = "Passwords didn't match."
        }else {
            return true
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
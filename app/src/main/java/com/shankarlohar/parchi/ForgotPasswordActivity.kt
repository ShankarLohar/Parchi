package com.shankarlohar.parchi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.shankarlohar.parchi.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var alert: AlertDialog
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        alert = AlertDialog.Builder(this).create()
        alert.setTitle("Forgot Password")
        alert.setMessage("Please wait while we send you the reset link...")
        alert.setCanceledOnTouchOutside(false)

        auth = FirebaseAuth.getInstance()

        binding.buttonResetSubmit.setOnClickListener {
            if (!Patterns.EMAIL_ADDRESS.matcher(binding.editTextUserEmail.text.toString().trim())
                    .matches()
            ) {
                binding.editTextUserEmail.error = "Invalid Email Format."
            }else{
                alert.show()
                auth.sendPasswordResetEmail(binding.editTextUserEmail.text.toString().trim())
                    .addOnSuccessListener {
                    alert.dismiss()
                    Toast.makeText(this,"Password reset link sent!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                }.addOnFailureListener{
                    alert.dismiss()
                    Toast.makeText(this,"Password reset failed due to ${it.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
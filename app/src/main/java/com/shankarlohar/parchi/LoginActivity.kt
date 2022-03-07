package com.shankarlohar.parchi

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.shankarlohar.parchi.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    companion object{
        private const val RC_SIGN_IN = 120
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var alert: AlertDialog
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        alert = AlertDialog.Builder(this).create()
        alert.setTitle("Login")
        alert.setMessage("Please wait while we login you to Parchi...")
        alert.setCanceledOnTouchOutside(false)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()
        checkUser()

        binding.textViewCreateAccount.setOnClickListener { onCreateNewAccount() }
        binding.buttonLogin.setOnClickListener { onLogin() }
        binding.textViewForgotPassword.setOnClickListener { onForgotPassword() }
        binding.signInWithGoogleButton.setOnClickListener { signIn() }

    }

    private fun onCreateNewAccount() {
        startActivity(Intent(this, SignupActivity::class.java))
    }

    private fun checkUser() {
        val firebaseUser = auth.currentUser
        if (firebaseUser!=null){
            startActivity(Intent(this, BottomNavActivity::class.java))
            finish()
        }else {
            onLogin()
        }
    }

    private fun onLogin() {
        if (validateUser()){
            firebaseLogin()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    alert.dismiss()
                    val firebaseUser = auth.currentUser
                    val name = firebaseUser!!.displayName
                    Toast.makeText(this,"Welcome $name!",Toast.LENGTH_LONG).show()
                    startActivity(Intent(this,BottomNavActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    alert.dismiss()
                    Toast.makeText(this,"Login Failed due to ${task.exception}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun firebaseLogin() {
        alert.show()
        auth.signInWithEmailAndPassword(binding.editTextUserEmail.text.toString().trim(),binding.editTextPassword.text.toString().trim())
            .addOnSuccessListener {
                alert.dismiss()
                val firebaseUser = auth.currentUser
                val name = firebaseUser!!.displayName
                Toast.makeText(this,"Welcome $name!",Toast.LENGTH_LONG).show()
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
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }

}
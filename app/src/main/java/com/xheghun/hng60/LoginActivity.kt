package com.xheghun.hng60

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : FirebaseAppCompactActivity() {
    private var error: String?  = null
    private lateinit var googleSignInClient: GoogleSignInClient
    private var RC_SIGN_IN: Int = 1220
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Glide.with(this).load(R.drawable.female_phone).into(bg_image)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        sign_in_btn.setOnClickListener { firebaseSignIn() }
        google_sign_in_btn.setOnClickListener { signWithGoogle() }
    }

    private fun signWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)
    }

    private fun firebaseSignIn() {
         error = "This field is required"
        when {
            email.text.isNullOrEmpty() -> {
                email_layout.isErrorEnabled = true
                email_layout.error = error
            }
            password.text.isNullOrEmpty() -> {
                password_layout.isErrorEnabled = true
                password_layout.error = error
            }
            else -> {
                val uEmail = email.text.toString().trim()
                val uPass = password.text.toString().trim()

                mAuth.signInWithEmailAndPassword(uEmail,uPass)
                    .addOnCompleteListener(this) {task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this,MainActivity::class.java))
                        } else {
                            error = "unable to firebaseSignIn"
                            Snackbar.make(findViewById(R.id.root_view), error!!,Snackbar.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val gAcctAPI = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            val accountFirstName = gAcctAPI.signInAccount!!.givenName
            val accountLastName = gAcctAPI.signInAccount!!.familyName
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!,accountFirstName,accountLastName)
            } catch (e: ApiException) {
                Log.w("Google Sign In Error",e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount, accountFirstName: String?,accountLastName: String?) {
        Log.d("FirebaseAuthWithGoogle", "firebaseAuthWithGoogle:" + account.id!!)

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("FirebaseAuthWithGoogle", "signInWithCredential:success")
                    sendToDB(accountFirstName!!,accountLastName!!)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("FirebaseAuthWithGoogle", "signInWithCredential:failure", task.exception)
                    Snackbar.make(findViewById(R.id.root_view), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }

                // ...
            }
    }

    fun facebookSignIn(view: View) {
        //enough coding for now check my github later
        Toast.makeText(this,"This feature is currently not available",Toast.LENGTH_SHORT).show()
    }
    fun toSignUp(view: View) {
        startActivity(Intent(this,SignUpActivity::class.java))
    }

    private fun sendToDB(firstname: String,lastname: String) {
        val database = FirebaseDatabase.getInstance()
        val profile = Profile(firstname, lastname)
        val ref = database.reference
            ref.child("users")
                .child("user_${mAuth.uid}").setValue(profile)
                .addOnSuccessListener { startActivity(Intent(this,MainActivity::class.java)) }
    }
}
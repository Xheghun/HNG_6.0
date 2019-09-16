package com.xheghun.hng60

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : FirebaseAppCompactActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        Glide.with(this).load(R.drawable.female_phone).into(bg_image)

    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    fun createAccount(view: View) {
        var error = "this field is required"
        when {
            first_name.text.isNullOrEmpty() -> {
                first_name_layout.isErrorEnabled = true
                first_name_layout.error = error
            }
            last_name.text.isNullOrEmpty() -> {
                last_name_layout.isErrorEnabled = true
                last_name_layout.error = error
            }
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
                val firstName = first_name.text.toString().trim()
                val lastname = last_name.text.toString().trim()
                sign_up_message.visibility = View.VISIBLE
                mAuth.createUserWithEmailAndPassword(uEmail, uPass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            sendToDB(firstName, lastname)
                        } else {
                            error = "unable to firebaseSignIn"
                            Snackbar.make(
                                findViewById(R.id.root_view),
                                error,
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    fun toSignIn(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    //send extra details to db
    private fun sendToDB(firstname: String, lastname: String) {
        val database = FirebaseDatabase.getInstance()
        val profile = Profile(firstname, lastname)
        val ref = database.reference
        ref.child("users")
            .child("user_${mAuth.currentUser!!.uid}").setValue(profile)
            .addOnSuccessListener {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("name", "$firstname $lastname")
                intent.putExtra("email", mAuth.currentUser!!.email)
                sign_up_message.visibility = View.GONE
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                sign_up_message.visibility = View.GONE
            }
    }
}
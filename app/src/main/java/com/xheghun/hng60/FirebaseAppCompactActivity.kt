package com.xheghun.hng60

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

open class FirebaseAppCompactActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    val PREFERENCE_NAME = "USER DETAILS"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }
}

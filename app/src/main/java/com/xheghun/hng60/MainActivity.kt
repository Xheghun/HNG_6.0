package com.xheghun.hng60

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FirebaseAppCompactActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Glide.with(this).load(R.drawable.female_phone).into(bg_image)

        logout_fab.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Chernobyl")
                .setMessage("you will be logged out")
                .setNegativeButton("no") { inter: DialogInterface, _: Int -> inter.dismiss() }
                .setPositiveButton("yes") { _: DialogInterface, _: Int ->
                    mAuth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                .show()
        }

        val intent = intent
        val fullname = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")

        val pref = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("prefFullname", fullname)
        editor.putString("prefEmail", email)
        editor.apply()

        val prefFullname = pref.getString("prefFullname", "user")
        val prefEmail = pref.getString("prefEmail", "user email")

        user_name.text = prefFullname
        user_email.text = prefEmail
    }
}

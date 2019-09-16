package com.xheghun.hng60

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        Glide.with(this).load(R.drawable.female_phone).into(bg_image)

    }

     fun toSignIn(view: View) {
        startActivity(Intent(this,LoginActivity::class.java))
    }
}

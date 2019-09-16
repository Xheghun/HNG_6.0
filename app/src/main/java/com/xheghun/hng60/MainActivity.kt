package com.xheghun.hng60

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : FirebaseAppCompactActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = intent

        Toast.makeText(this,"name is ${intent.getStringExtra("name")}",Toast.LENGTH_SHORT).show()
    }
}

package com.example.newmaidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.sign

class MainActivity : AppCompatActivity() {

    private lateinit var signOutbtn: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        signOutbtn = findViewById(R.id.signOutButton)
        signOutbtn.setOnClickListener{
            auth.signOut()

            startActivity(Intent(this,sendOtp::class.java))
        }

    }
}
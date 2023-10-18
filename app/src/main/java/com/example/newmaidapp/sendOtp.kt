package com.example.newmaidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class sendOtp : AppCompatActivity() {

    private lateinit var sendOtpbtn: Button
    private  lateinit var phoneNumberEdittext: EditText
    private  lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_otp)
        init()
    }
    private  fun init() {
        sendOtpbtn = findViewById(R.id.sendOtpButton)
        phoneNumberEdittext = findViewById(R.id.phoneNumberEdittext)
    }
}
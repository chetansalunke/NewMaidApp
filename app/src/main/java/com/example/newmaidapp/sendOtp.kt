package com.example.newmaidapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class sendOtp : AppCompatActivity() {

    private lateinit var sendOtpbtn: Button
    private  lateinit var phoneNumberEdittext: EditText
    private  lateinit var auth:FirebaseAuth
    private  lateinit var number: String
    private lateinit var mProgressbar : ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_otp)
        init()
        sendOtpbtn.setOnClickListener{
            number = phoneNumberEdittext.text.trim().toString()
            if(number.isNotEmpty()){
                if(number.isNotEmpty()){
                    if(number.length == 10)
                    {
                            number = "+91$number"
                            mProgressbar.visibility = View.VISIBLE
                        val options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(number) // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this) // Activity (for callback binding)
                            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                            .build()
                        PhoneAuthProvider.verifyPhoneNumber(options)
                    }
                    else{
                        Toast.makeText(this,"Enter Valid Number",Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this,"Enter Valid Number",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private  fun init() {
        sendOtpbtn = findViewById(R.id.sendOtpButton)
        phoneNumberEdittext = findViewById(R.id.phoneNumberEdittext)
        auth = FirebaseAuth.getInstance()
        mProgressbar = findViewById(R.id.progressBar)
        mProgressbar.visibility = View.INVISIBLE
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this,"Authintication Successfull",Toast.LENGTH_SHORT).show()
                    sendToMain()

                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Log.d("TAG","signWithPhoneAuthCredential: ${task.exception.toString()}")
                    }
                    // Update UI
                }
            }
    }
    private fun sendToMain(){
        startActivity(Intent(this,MainActivity::class.java))
    }

   private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request

                Log.d("TAG","onVerificationFailed: ${e.toString()}")
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d("TAG","onVerificationFailed: ${e.toString()}")
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
                val intent = Intent(this@sendOtp,verifyOtp::class.java)
                intent.putExtra("OTP",verificationId)
                intent.putExtra("resendToken",token)
                intent.putExtra("phoneNumber",number)
                startActivity(intent);
            mProgressbar.visibility= View.INVISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser !=null)
            {
                startActivity(Intent(this,MainActivity::class.java))
            }
    }
}
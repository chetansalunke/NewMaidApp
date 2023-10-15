package com.example.newmaidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class verifyOtp : AppCompatActivity() {


    private lateinit var auth : FirebaseAuth
    private lateinit var verifyBtn:Button
    private  lateinit var resendTv:TextView
    private lateinit var inputOTP1:EditText
    private lateinit var inputOTP2:EditText
    private lateinit var inputOTP3:EditText
    private lateinit var inputOTP4:EditText
    private lateinit var inputOTP5:EditText
    private lateinit var inputOTP6:EditText
    private lateinit var progressbar:ProgressBar
    private lateinit var OTP:String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneNumber:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)

        OTP = intent.getStringExtra("OTP").toString()
        resendToken = intent.getParcelableExtra("resendToken")!!
        phoneNumber = intent.getStringExtra("phoneNumber")!!
        init()
        progressbar.visibility = View.VISIBLE
        addTextChangeListener()
        resendOYPVisiblity()
        verifyBtn.setOnClickListener{
            // collect otp from the  all edittext
            val typedOTP = (inputOTP1.text.toString()+inputOTP2.text.toString()+inputOTP3.text.toString()+inputOTP4.text.toString()+inputOTP5.text.toString()+inputOTP6.text.toString())

            if(typedOTP.isEmpty()){
                if(typedOTP.length==6){
                    val credentiall: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        OTP,typedOTP
                    )
                    progressbar.visibility = View.VISIBLE
                    signInWithPhoneAuthCredential(credentiall)
                }else
                {
                    Toast.makeText(this,"Please Enter Correct Otp",Toast.LENGTH_SHORT).show()
                }
            }else
            {
                Toast.makeText(this,"Please Enter Otp",Toast.LENGTH_SHORT).show()
            }
        }


        // resend Token

        resendTv.setOnClickListener{
            resendVerificationCode()
            resendOYPVisiblity()
        }



    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    progressbar.visibility = View.INVISIBLE
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
    private fun addTextChangeListener(){
        inputOTP1.addTextChangedListener(EditTextWatcher(inputOTP1))
        inputOTP2.addTextChangedListener(EditTextWatcher(inputOTP2))
        inputOTP3.addTextChangedListener(EditTextWatcher(inputOTP3))
        inputOTP4.addTextChangedListener(EditTextWatcher(inputOTP4))
        inputOTP5.addTextChangedListener(EditTextWatcher(inputOTP5))
        inputOTP6.addTextChangedListener(EditTextWatcher(inputOTP5))

    }
    private fun init()
    {
        auth = FirebaseAuth.getInstance()
        verifyBtn = findViewById(R.id.verifyButton)
        resendTv = findViewById(R.id.resendOTP)
        inputOTP1 = findViewById(R.id.inputotp1)
        inputOTP2 = findViewById(R.id.inputotp2)
        inputOTP3 = findViewById(R.id.inputotp3)
        inputOTP4 = findViewById(R.id.inputotp4)
        inputOTP5 = findViewById(R.id.inputotp5)
        inputOTP6 = findViewById(R.id.inputotp6)
        progressbar = findViewById(R.id.progressBar)

    }

    private fun resendVerificationCode()
    {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(resendToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
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

            OTP = verificationId
            resendToken = token
        }
    }

    private fun resendOYPVisiblity(){
        inputOTP1.setText("")
        inputOTP2.setText("")
        inputOTP3.setText("")
        inputOTP4.setText("")
        inputOTP5.setText("")
        inputOTP6.setText("")

        resendTv.visibility = View.INVISIBLE
        resendTv.isEnabled= false

        Handler(Looper.myLooper()!!).postDelayed(Runnable {
        resendTv.visibility=View.VISIBLE
            resendTv.isEnabled=true
        },60000)

    }

    inner class EditTextWatcher(private val view: View): TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }


        override fun afterTextChanged(p0: Editable?) {
            val text = p0.toString()
            when(view.id){
                R.id.inputotp1->if(text.length ==1) inputOTP2.requestFocus()
                R.id.inputotp2->if(text.length ==1) inputOTP3.requestFocus()else if(text.isEmpty()) inputOTP1.requestFocus()
                R.id.inputotp3->if(text.length ==1) inputOTP4.requestFocus()else if(text.isEmpty()) inputOTP2.requestFocus()
                R.id.inputotp4->if(text.length ==1) inputOTP5.requestFocus()else if(text.isEmpty()) inputOTP3.requestFocus()
                R.id.inputotp5->if(text.length ==1) inputOTP6.requestFocus()else if(text.isEmpty()) inputOTP4.requestFocus()
                R.id.inputotp6->if(text.isEmpty()) inputOTP5.requestFocus()
            }
        }

    }
}
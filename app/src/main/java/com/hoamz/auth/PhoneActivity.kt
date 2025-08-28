package com.hoamz.auth

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.TimeUnit
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import com.google.firebase.auth.FirebaseAuth
import com.hoamz.auth.databinding.ActivityPhoneBinding
import androidx.core.graphics.drawable.toDrawable
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

class PhoneActivity : BaseActivity() {
    private lateinit var binding : ActivityPhoneBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var number : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.setBackgroundColor(Color.WHITE)
        init();
        //onClick
        onClick();
    }
    private fun onClick(){
        //on click
        binding.sendOTPBtn.setOnClickListener {
            number = binding.phoneEditTextNumber.text.toString()
            if(number.isNotEmpty()){
                //ok ->
                number = "+84$number"
                //hien thi progress
                binding.phoneProgressBar.visibility = View.VISIBLE
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(number)
                    .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS)
                    .setActivity(this)
                    .setCallbacks(callbacks)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
            else{
                //neu chua nhap gi -> toast thong bao
                showDialogNotify("Pleas enter phone number")
            }
        }
    }
    private fun init(){
        auth = FirebaseAuth.getInstance()
        binding.phoneProgressBar.visibility = View.INVISIBLE
    }

    private fun signInWithPhoneAuthCredential(credential : PhoneAuthCredential){
        auth.signInWithCredential(credential)
            .addOnCompleteListener (this){task ->
                if(task.isSuccessful){
                    //sign successfully
                    onToast("Authenticate Successfully")
                    startActivity(Intent(this, MainActivity::class.java))
                }
                else{
                    //failed
                    onToast("Authenticate Failed")
                }
                binding.phoneProgressBar.visibility = View.INVISIBLE
            }
    }
    //callback
    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            //ok ->
            signInWithPhoneAuthCredential(credential = p0)
        }
        override fun onVerificationFailed(e: FirebaseException) {
            if(e is FirebaseAuthInvalidCredentialsException){
                onToast("loi o day")
            }
            else if(e is FirebaseTooManyRequestsException ){
                onToast("loi o day")
            }
            else{
                onToast(e.message.toString())
            }
        }

        override fun onCodeSent(
            verificationId : String,
            token: PhoneAuthProvider.ForceResendingToken)
        {
            val intent = Intent(this@PhoneActivity, OTPActivity::class.java)
            intent.putExtra("OTP",verificationId)
            intent.putExtra("resendToken",token)
            intent.putExtra("phoneNumber",number)
            startActivity(intent)
            binding.phoneProgressBar.visibility = View.INVISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        auth.currentUser?.let{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onToast("destroy")
        Log.e("TAG","destroy");
    }

    override fun onResume() {
        super.onResume()
        onToast("Resume")
        Log.e("TAG","Resum");
    }

    override fun onStop() {
        super.onStop()
        onToast("Stop")
        Log.e("TAG","Stop");
    }

}


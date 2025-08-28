package com.hoamz.auth

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hoamz.auth.databinding.ActivityOtpactivityBinding
import kotlin.math.sign

/*
* xong auth
* doc them ve firebase firestore
* */
class OTPActivity : BaseActivity() {
    private lateinit var binding: ActivityOtpactivityBinding
//    private lateinit var auth : FirebaseAuth
//    private lateinit var otp : String
//    private lateinit var resendToken : PhoneAuthProvider.ForceResendingToken
//    private lateinit var phoneNumber : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.setBackgroundColor(Color.WHITE)


        binding.otpProgressBar.visibility = View.INVISIBLE
        //onGetData()
        addTextChangeListener()
        //resendOtp()

//        binding.verifyOTPBtn.setOnClickListener {
//            val typeOtp : String = (binding.otpEditText1.text.toString()
//                     + binding.otpEditText2.text.toString()
//                    + binding.otpEditText3.text.toString()
//                    + binding.otpEditText4.text.toString()
//                    + binding.otpEditText5.text.toString()
//                    + binding.otpEditText6.text.toString())
//
//            if(typeOtp.isNotEmpty()){
//                if(typeOtp.length == 6){
//                    val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
//                        otp,typeOtp
//                    )
//                    binding.otpProgressBar.visibility = View.VISIBLE
//                    signInWithPhoneAuthCredential(credential)
//                }
//                else{
//                    onToast("Please enter correct otp")
//                }
//            }
//            else{
//                onToast("Please enter otp")
//            }
//        }
//        binding.resendTextView.setOnClickListener {
//            resendCode()
//        }
    }

//    private fun resendCode(){
//        val options = PhoneAuthOptions.newBuilder(auth)
//            .setPhoneNumber(phoneNumber)
//            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS)
//            .setActivity(this)
//            .setForceResendingToken(resendToken)
//            .setCallbacks(callbacks)
//            .build()
//        PhoneAuthProvider.verifyPhoneNumber(options)
//    }
//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    private fun onGetData() {
//        otp = intent.getStringExtra("OTP").toString()
//        resendToken = intent.getParcelableExtra(
//            "resendToken",
//            PhoneAuthProvider.ForceResendingToken::class.java
//        )!!
//        phoneNumber = intent.getStringExtra("phoneNumber").toString()
//    }

//    private fun resendOtp(){
//        binding.otpEditText1.setText("")
//        binding.otpEditText2.setText("")
//        binding.otpEditText3.setText("")
//        binding.otpEditText4.setText("")
//        binding.otpEditText5.setText("")
//        binding.otpEditText6.setText("")
//        binding.resendTextView.visibility = View.INVISIBLE
//        binding.resendTextView.isEnabled = false
//
//        //doi 1 luc roi hien lai resendTextView
//        val handler = Handler(Looper.getMainLooper())
//        handler.postDelayed({
//            binding.resendTextView.visibility = View.VISIBLE
//            binding.resendTextView.isEnabled = true
//        },100000)
//    }

//    private fun signInWithPhoneAuthCredential(credential : PhoneAuthCredential){
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener (this){task ->
//                if(task.isSuccessful){
//                    //sign successfully
//                    binding.otpProgressBar.visibility = View.INVISIBLE
//                    onToast("Authenticate Successfully")
//                    startActivity(Intent(this, MainActivity::class.java))
//                }
//                else{
//                    //failed
//                    onToast(task.exception.toString())
//                }
//                binding.otpProgressBar.visibility = View.INVISIBLE
//            }
//    }

    private fun addTextChangeListener(){
        binding.otpEditText1.addTextChangedListener(MyEditTextWatcher(binding.otpEditText1))
        binding.otpEditText2.addTextChangedListener(MyEditTextWatcher(binding.otpEditText2))
        binding.otpEditText3.addTextChangedListener(MyEditTextWatcher(binding.otpEditText3))
        binding.otpEditText4.addTextChangedListener(MyEditTextWatcher(binding.otpEditText4))
        binding.otpEditText5.addTextChangedListener(MyEditTextWatcher(binding.otpEditText5))
        binding.otpEditText6.addTextChangedListener(MyEditTextWatcher(binding.otpEditText6))
    }

//    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
//        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
//            //ok ->
//            signInWithPhoneAuthCredential(credential = p0)
//        }
//        override fun onVerificationFailed(e: FirebaseException) {
//            if(e is FirebaseAuthInvalidCredentialsException){
//                onToast("Dinh danh khong hop le")
//            }
//            else if(e is FirebaseTooManyRequestsException ){
//                onToast("Co nhieu yeu cau")
//            }
//        }
//
//        override fun onCodeSent(
//            verificationId : String,
//            token: PhoneAuthProvider.ForceResendingToken)
//        {
//            otp = verificationId
//            resendToken = token
//        }
//    }
    inner class MyEditTextWatcher(private val view: View) : TextWatcher{
        override fun afterTextChanged(edit: Editable?) {
            val text = edit.toString()
            when(view.id){
                //neu dai = 1 thi snag text tiep theo
                R.id.otpEditText1 -> if(text.length == 1) binding.otpEditText2.requestFocus()//yeu cau focus
                R.id.otpEditText2 -> if(text.length == 1) binding.otpEditText3.requestFocus()
                                    else if(text.isEmpty()){
                                        binding.otpEditText2.onBackspaceListener = {
                                            binding.otpEditText1.requestFocus()
                                        }
                                    }
                R.id.otpEditText3 -> if(text.length == 1) binding.otpEditText4.requestFocus()//yeu cau focus
                                    else if(text.isEmpty()){
                                        binding.otpEditText3.onBackspaceListener = {
                                            binding.otpEditText2.requestFocus()
                                        }
                                    }
                R.id.otpEditText4 -> if(text.length == 1) binding.otpEditText5.requestFocus()//yeu cau focus
                                    else if(text.isEmpty()){
                                        binding.otpEditText4.onBackspaceListener = {
                                            binding.otpEditText3.requestFocus()
                                        }
                                    }
                R.id.otpEditText5 -> if(text.length == 1) binding.otpEditText6.requestFocus()//yeu cau focus
                                    else if(text.isEmpty()){
                                        binding.otpEditText5.onBackspaceListener = {
                                            binding.otpEditText4.requestFocus()
                                        }
                                    }
                R.id.otpEditText6 -> if(text.isEmpty()) binding.otpEditText5.requestFocus()
            }
        }

        override fun beforeTextChanged(
            p0: CharSequence?, p1: Int, p2: Int, p3: Int
        ) {

        }

        override fun onTextChanged(
            p0: CharSequence?, p1: Int, p2: Int, p3: Int
        ) {

        }
    }

    override fun onToast(msg: String) {
        super.onToast(msg)
    }

}
package com.hoamz.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.hoamz.auth.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var credentialManager: CredentialManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.signOutBtn.setOnClickListener {
            onToast(binding.otp.getOtp())
        }

//        auth = FirebaseAuth.getInstance()
//        credentialManager = CredentialManager.create(this)
//        binding.signOutBtn.setOnClickListener {
//            val ggOptions = GetGoogleIdOption.Builder()
//                .setFilterByAuthorizedAccounts(true)
//                .setServerClientId(getString(R.string.default_web_client_id))
//                .build()
//
//            val rq = GetCredentialRequest.Builder()
//                .addCredentialOption(ggOptions)
//                .build()
//
//            lifecycleScope.launch {
//                try{
//                    val result = credentialManager.getCredential(
//                        request = rq,
//                        context = this@MainActivity
//                    )
//                    val credential = result.credential
//                    if(credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
//                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
//                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
//                    }
//                }catch (e : GetCredentialException){
//                    onToast(e.message.toString())
//                }
//            }
//
//        }
    }
    /*
    * setFilterByAuthorizedAccounts(...) trong Google Identity Services (GIS) là một flag để quyết định xem khi hiện account picker (hộp chọn tài khoản Google), nó có lọc chỉ những tài khoản đã được authorized (đăng nhập trước, có refresh token) hay không.

true → chỉ hiện những account đã được app của bạn cấp phép trước đó.
👉 Thường dùng nếu bạn muốn bắt buộc người dùng chọn lại trong số các tài khoản đã login với app, không cho login mới.

false → hiện tất cả tài khoản Google trong thiết bị (kể cả chưa từng login với app).
👉 Thường dùng cho đăng nhập mới, hoặc khi muốn cho user tự chọn bất kỳ tài khoản nào.

Ví dụ:

Ứng dụng ngân hàng, yêu cầu user chỉ được chọn tài khoản đã xác thực trước → để true.

Ứng dụng bình thường (ví dụ social, game, học tập), muốn user có thể login với bất kỳ tài khoản nào → để false.

👉 Trong thực tế, hầu hết app để false cho login ban đầu, sau đó nếu bạn có luồng "re-authenticate" thì có thể để true để chỉ hiện tài khoản đã cấp quyền.
    * */

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful) {
                    val userUid = auth.currentUser?.uid
                    onToast(userUid.toString())
                } else {
                    onToast("Authentication Failed")
                }
            }
    }
}

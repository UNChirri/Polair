package com.polairapp.polair

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()
        signOut()

        initFacebookCallback()
        initButtonsListeners()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun initFacebookCallback() {
        facebookLogin.setReadPermissions("email", "public_profile")
        facebookLogin.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                startActivity(Intent(this@LoginActivity, LoadingActivity::class.java))
                finish()
            }

            override fun onCancel() {}

            override fun onError(error: FacebookException) {}

        })

    }
    private fun initButtonsListeners() {
        consLayFacebook.setOnClickListener {
                        facebookLogin.performClick()
        }
    }


    fun signOut() {
        auth.signOut()
        LoginManager.getInstance().logOut()
    }
}
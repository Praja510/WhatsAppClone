package com.mprajadinata.whatsappclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btn_signup.setOnClickListener {
            onSignUp()
        }

        txt_login.setOnClickListener {
            onLogin()

        }
    }

    private fun onSignUp() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun onLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
package com.mprajadinata.whatsappclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener {
            onLogin()
        }

        txt_signup.setOnClickListener {
            onSignUp()
        }
    }

    private fun onSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }

    private fun onLogin() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }
}
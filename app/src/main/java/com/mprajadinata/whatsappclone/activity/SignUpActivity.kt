package com.mprajadinata.whatsappclone.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mprajadinata.whatsappclone.MainActivity
import com.mprajadinata.whatsappclone.R
import com.mprajadinata.whatsappclone.util.DATA_USERS
import com.mprajadinata.whatsappclone.util.User
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private val firebaseDb = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser?.uid
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_sign_up)

        setTextChangedListener(edt_email, til_email)
        setTextChangedListener(edt_password, til_password)
        setTextChangedListener(edt_name, til_name)
        setTextChangedListener(edt_phone, til_phone)
        progress_layout.setOnTouchListener { v, event -> true }

        btn_signup.setOnClickListener {
            onSignUp()
        }

        txt_login.setOnClickListener {
            onLogin()

        }
    }

    private fun setTextChangedListener(edt: EditText, til: TextInputLayout) {

        edt.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                til.isErrorEnabled = false

            }
        })
    }

    private fun onSignUp() {

        var proceed = true
        if (edt_name.text.isNullOrEmpty()) {
            til_name.error = "Required name"
            til_name.isErrorEnabled = true
            proceed = false

        }

        if (edt_phone.text.isNullOrEmpty()) {
            til_phone.error = "Required phone"
            til_phone.isErrorEnabled = true
            proceed = false

        }

        if (edt_email.text.isNullOrEmpty()) {
            til_email.error = "Required email"
            til_email.isErrorEnabled = true
            proceed = false

        }

        if (edt_password.text.isNullOrEmpty()) {
            til_password.error = "Required password"
            til_password.isErrorEnabled = true
            proceed = false

        }

        if (proceed) {
            progress_layout.visibility = View.VISIBLE
            firebaseAuth.createUserWithEmailAndPassword(
                edt_email.text.toString(),
                edt_password.text.toString()
            )

                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        progress_layout.visibility = View.GONE
                        Toast.makeText(
                            this@SignUpActivity,
                            "SignUp error:${task.exception?.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (firebaseAuth.uid == null) {
                        val email = edt_email.text.toString()
                        val phone = edt_phone.text.toString()
                        val name = edt_name.text.toString()
                        val user = User(email, phone, name, "", "Hello I'm New", "", "")
                        firebaseDb.collection(DATA_USERS).document(firebaseAuth.uid!!).set(user)
                    }

                    progress_layout.visibility = View.GONE
                }

                .addOnFailureListener {
                    progress_layout.visibility = View.GONE
                    it.printStackTrace()
                }
        }
    }

    private fun onLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }
}
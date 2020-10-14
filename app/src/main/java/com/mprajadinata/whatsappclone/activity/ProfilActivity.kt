package com.mprajadinata.whatsappclone.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mprajadinata.whatsappclone.MainActivity
import com.mprajadinata.whatsappclone.R
import com.mprajadinata.whatsappclone.util.*
import kotlinx.android.synthetic.main.activity_profil.*

class ProfilActivity : AppCompatActivity() {

    private val firebaseDb = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        if (userId.isNullOrEmpty()) {
            finish()
        }

        btn_back_profil.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        progress_layout.setOnTouchListener { view, event -> true }

        btn_apply.setOnClickListener {
            onApply()
        }

        btn_delete_account.setOnClickListener {
            onDelete()
        }

        populateInfo()
    }

    private fun populateInfo() {

        progress_layout.visibility = View.VISIBLE
        firebaseDb.collection(DATA_USERS).document(userId!!).get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                edt_name_profile.setText(user?.name, TextView.BufferType.EDITABLE)
                edt_email_profile.setText(user?.email, TextView.BufferType.EDITABLE)
                edt_phone_profile.setText(user?.phone, TextView.BufferType.EDITABLE)
                progress_layout.visibility = View.GONE

            }

            .addOnFailureListener { e ->
                e.printStackTrace()
                finish()
            }
    }

    private fun onApply() {

        progress_layout.visibility = View.VISIBLE

        val name = edt_name_profile.text.toString()
        val email = edt_email_profile.text.toString()
        val phone = edt_phone_profile.text.toString()

        val map = HashMap<String, Any>()
        map[DATA_USER_NAME] = name
        map[DATA_USER_EMAIL] = email
        map[DATA_USER_PHONE] = phone

        firebaseDb.collection(DATA_USERS).document(userId!!).update(map)
            .addOnSuccessListener {
                Toast.makeText(this, "Update successful", Toast.LENGTH_LONG).show()
                finish()
            }

            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(this, "Update failed", Toast.LENGTH_LONG).show()
                progress_layout.visibility = View.GONE
            }
    }

    private fun onDelete() {

        progress_layout.visibility = View.VISIBLE
        AlertDialog.Builder(this)
            .setTitle("Delete Acount")
            .setMessage("Are you sure about this?")
            .setPositiveButton("Yes") { dialog, which ->
                firebaseDb.collection(DATA_USERS).document(userId!!).delete()
                progress_layout.visibility = View.GONE
                Toast.makeText(this, "Profil deleted", Toast.LENGTH_LONG).show()
                finish()

            }

            .setNegativeButton("No") { dialog, which ->
                progress_layout.visibility = View.GONE

            }

            .setCancelable(false)
            .show()

    }
}
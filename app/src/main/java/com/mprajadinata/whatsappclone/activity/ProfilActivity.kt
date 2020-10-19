package com.mprajadinata.whatsappclone.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mprajadinata.whatsappclone.MainActivity
import com.mprajadinata.whatsappclone.R
import com.mprajadinata.whatsappclone.util.*
import kotlinx.android.synthetic.main.activity_profil.*

class ProfilActivity : AppCompatActivity() {

    private val firebaseDb = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance().reference
    private var imageUrl: String? = null

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

        imbtn_profile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_PHOTO)

        }

        populateInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PHOTO) {
            storeImage(data?.data)
        }
    }

    private fun populateInfo() {

        progress_layout.visibility = View.VISIBLE
        firebaseDb.collection(DATA_USERS).document(userId!!).get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                imageUrl = user?.imageUrl
                edt_name_profile.setText(user?.name, TextView.BufferType.EDITABLE)
                edt_email_profile.setText(user?.email, TextView.BufferType.EDITABLE)
                edt_phone_profile.setText(user?.phone, TextView.BufferType.EDITABLE)
                if (imageUrl != null) {
                    populateImage(this, user?.imageUrl, img_profile, R.drawable.ic_user)
                }

                progress_layout.visibility = View.GONE

            }

            .addOnFailureListener { e ->
                e.printStackTrace()
                finish()
            }
    }

    private fun storeImage(uri: Uri?) {

        if (uri != null) {
            Toast.makeText(this, "Uploading..", Toast.LENGTH_LONG).show()
            progress_layout.visibility = View.VISIBLE
            val filePath = firebaseStorage.child(DATA_IMAGES).child(userId!!)

            filePath.putFile(uri)
                .addOnSuccessListener {
                    filePath.downloadUrl
                        .addOnSuccessListener {
                            val url = it.toString()
                            firebaseDb.collection(DATA_USERS)
                                .document(userId).update(DATA_USER_IMAGE_URL, url)
                                .addOnSuccessListener {
                                    imageUrl = url
                                    populateImage(this, imageUrl, img_profile, R.drawable.ic_user)

                                }

                            progress_layout.visibility = View.GONE

                        }

                        .addOnFailureListener {
                            onUploadFailured()

                        }
                }

                .addOnFailureListener {
                    onUploadFailured()

                }
        }
    }

    private fun onUploadFailured() {

        Toast.makeText(this, "Failed, Pls try again", Toast.LENGTH_LONG).show()
        progress_layout.visibility = View.GONE
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
                firebaseStorage.child(DATA_IMAGES).child(userId).delete()
                firebaseAuth.currentUser?.delete()
                    ?.addOnSuccessListener {
                        finish()
                    }

                    ?.addOnFailureListener {
                        finish()
                    }

                progress_layout.visibility = View.GONE
                Toast.makeText(this, "Profil deleted", Toast.LENGTH_LONG).show()
                finish()

                startActivity(Intent(this, LoginActivity::class.java))

            }

            .setNegativeButton("No") { dialog, which ->
                progress_layout.visibility = View.GONE

            }

            .setCancelable(false)
            .show()

    }
}
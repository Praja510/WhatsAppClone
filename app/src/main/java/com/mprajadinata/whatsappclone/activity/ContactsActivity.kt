package com.mprajadinata.whatsappclone.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mprajadinata.whatsappclone.MainActivity
import com.mprajadinata.whatsappclone.R
import com.mprajadinata.whatsappclone.adapter.ContactAdapter
import com.mprajadinata.whatsappclone.listener.ContactsClickListener
import com.mprajadinata.whatsappclone.util.Contact
import kotlinx.android.synthetic.main.activity_contacts.*

class ContactsActivity : AppCompatActivity(), ContactsClickListener {

    private val contactsList = ArrayList<Contact>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        getContacts()
        setupList()
    }

    private fun getContacts() {

        progress_layout.visibility = View.VISIBLE
        contactsList.clear()

        val newList = ArrayList<Contact>()
        val phone = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
        )

        while (phone!!.moveToNext()) {
            val name =
                phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))

            val phoneNumber =
                phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            newList.add(Contact(name, phoneNumber))
        }

        contactsList.addAll(newList)
        phone.close()

    }

    private fun setupList() {
        progress_layout.visibility = View.GONE
        val contactsAdapter = ContactAdapter(contactsList)
        contactsAdapter.setOnItemClickedListener(this)
        rv_contacts.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = contactsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

    }

    override fun onContactClicked(name: String?, phone: String?) {

        val intent = Intent()
        intent.putExtra(MainActivity.PARAM_NAME, name)
        intent.putExtra(MainActivity.PARAM_PHONE, phone)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
package com.mprajadinata.whatsappclone

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.mprajadinata.whatsappclone.activity.LoginActivity
import com.mprajadinata.whatsappclone.activity.ProfilActivity
import com.mprajadinata.whatsappclone.adapter.SectionPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainActivity : AppCompatActivity() {

    private var mSectionPagerAdapter: SectionPagerAdapter? = null
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        mSectionPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        container.adapter = mSectionPagerAdapter
        fab.setOnClickListener {
            Snackbar.make(it, "Replace with action", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.action_profile -> onProfil()
            R.id.action_logout -> onLogout()

        }

        return super.onOptionsItemSelected(item)
    }

    private fun onProfil() {
        startActivity(Intent(this, ProfilActivity::class.java))
        finish()

    }

    private fun onLogout() {
        firebaseAuth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    class PlaceHolderFragment : Fragment() {
        companion object {
            private val ARG_SECTION_NUMBER = "Section Number"
            fun newIntent(sectionNumber: Int): PlaceHolderFragment {
                val fragment =
                    PlaceHolderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_main, container, false)
            rootView.section_label.text = "Hello world, from section${arguments?.getInt(
                ARG_SECTION_NUMBER
            )}"
            return rootView
        }
    }


}

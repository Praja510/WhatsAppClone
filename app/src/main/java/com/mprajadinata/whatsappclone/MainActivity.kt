package com.mprajadinata.whatsappclone

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
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

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        resizeTabs()
        tabs.getTabAt(1)?.select()

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {

                when (tab?.position) {
                    0 -> fab.hide()
                    1 -> fab.show()
                    2 -> fab.hide()
                }
            }
        })

        fab.setOnClickListener {
            onNewChat()
            Snackbar.make(it, "Replace with action", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show()

        }
    }

    private fun onNewChat() {
        Toast.makeText(this, "Add action for result", Toast.LENGTH_LONG).show()
    }

    private fun resizeTabs() {

        val layout = (tabs.getChildAt(0) as LinearLayout).getChildAt(0) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 0.4f
        layout.layoutParams = layoutParams
    }

    override fun onResume() {
        super.onResume()

        if (firebaseAuth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
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

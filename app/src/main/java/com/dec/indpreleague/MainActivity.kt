package com.dec.indpreleague

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.dec.indpreleague.ui.main.fragments.MainFragment
import com.dec.indpreleague.ui.main.fragments.WinnerFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            replaceFragment(MainFragment.newInstance())
        }
    }

    /**
     * API to handle fragment transition
     */
    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.replace(android.R.id.content, fragment, fragment.javaClass.simpleName)
        transaction.addToBackStack("tag")
        transaction.commit()
    }

    /**
     * Toolbar back press handler
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Handle back press events
     */
    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(android.R.id.content)) {
            is MainFragment -> {
                finish()
            }
            is WinnerFragment -> supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, MainFragment.newInstance())
                .addToBackStack("tag").commit()
            else -> super.onBackPressed()
        }

    }
}


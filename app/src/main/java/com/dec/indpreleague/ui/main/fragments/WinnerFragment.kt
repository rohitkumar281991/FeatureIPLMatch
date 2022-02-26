package com.dec.indpreleague.ui.main.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dec.indpreleague.MainActivity
import com.dec.indpreleague.R
import com.dec.indpreleague.databinding.WinnerFragmentBinding
import com.dec.indpreleague.ui.main.viewmodel.MainViewModel
import java.util.*

/**
 * Fragment to handle Winner teams display
 */
class WinnerFragment : Fragment() {
    companion object {
        fun newInstance() = WinnerFragment()
    }

    private lateinit var binding: WinnerFragmentBinding
    private lateinit var activity: Activity
    private val viewModel: MainViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) activity = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        binding = DataBindingUtil.inflate(inflater, R.layout.winner_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val appCompatActivity = activity
        if (appCompatActivity is AppCompatActivity) {
            appCompatActivity.setSupportActionBar(binding.toolbarHomepage)
            appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        registerObserver()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRestartMatch.setOnClickListener {
            restartMatch()
        }
    }

    /**
     * API to restart the match
     */
    private fun restartMatch() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity.finish()
    }

    /**
     * Handle back press
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Observer to get list of top winner teams from shared viewmodel
     */
    private fun registerObserver() {
        viewModel.winnerTeams.observe(viewLifecycleOwner) {
            populateUI(it)
        }
    }

    /**
     * Show winner teams on UI
     */
    private fun populateUI(it: Stack<String>?) {
        if (it?.isEmpty() == false) {
            binding.tvFirstPosTeam.text = it.pop()
        }
        if (it?.isEmpty() == false) {
            binding.tvSecondPosTeam.text = it.pop()
        }
        if (it?.isEmpty() == false) {
            binding.tvThirdPosTeam.text = it.pop()
        }
    }
}
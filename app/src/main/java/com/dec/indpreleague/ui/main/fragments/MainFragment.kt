package com.dec.indpreleague.ui.main.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dec.indpreleague.MainActivity
import com.dec.indpreleague.R
import com.dec.indpreleague.databinding.MainFragmentBinding
import com.dec.indpreleague.getTeamsList
import com.dec.indpreleague.ui.main.adapter.RVAdapter
import com.dec.indpreleague.ui.main.data.Team
import com.dec.indpreleague.ui.main.data.TeamEntity
import com.dec.indpreleague.ui.main.data.TeamRepository
import com.dec.indpreleague.ui.main.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * Main Screen UI
 */
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: MainFragmentBinding
    private lateinit var activity: Activity
    private val viewModel: MainViewModel by activityViewModels()
    private var adapter = RVAdapter(ArrayList<Team>())
    private var WAS_HARD_CODED_BTN_CLICKED: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) activity = context as AppCompatActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val appCompatActivity = activity
        if (appCompatActivity is AppCompatActivity) {
            appCompatActivity.setSupportActionBar(binding.toolbarHomepage)
            appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddCustomData.setOnClickListener {
            if (binding.etTeamName.text.toString().isBlank()) {
                Snackbar.make(
                    activity.window.decorView,
                    "Enter team name in edit text!!",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                if (WAS_HARD_CODED_BTN_CLICKED) {
                    showAlertDialog()
                } else {
                    addData()
                }
            }
        }
        binding.btnAddHardcodedTeam.setOnClickListener {
            WAS_HARD_CODED_BTN_CLICKED = true
            addHardCodedTeam()
        }
        binding.btnStartIpl.setOnClickListener {
            if (adapter.itemCount == 0) {
                Snackbar.make(activity.window.decorView, "Team data empty!!", Snackbar.LENGTH_LONG)
                    .show()
            } else {
                startIPL()
            }
        }
        initializeAdapter()
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Warning")
        builder.setMessage("Do you want to enter new team data!!")

        builder.setPositiveButton("Yes") { _, _ ->
            viewModel.clearTeamsData()
            invalidateRecyclerView()
            WAS_HARD_CODED_BTN_CLICKED = false
            addData()
        }

        builder.setNegativeButton("No") { _, _ ->
            WAS_HARD_CODED_BTN_CLICKED = true
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    /**
     * Initialize adapter to show team list
     */
    private fun initializeAdapter() {
        binding.recyclerview.layoutManager = LinearLayoutManager(
            activity, RecyclerView.VERTICAL, false
        )

        binding.recyclerview.addItemDecoration(
            DividerItemDecoration(
                activity,
                (binding.recyclerview.layoutManager as LinearLayoutManager).orientation
            )
        )
        observeData()
    }

    /**
     * Observe liveData to update recycler view
     */
    private fun observeData() {
        viewModel.lst.observe(viewLifecycleOwner) {
            adapter = RVAdapter(it)
            binding.recyclerview.adapter = adapter
        }
    }

    /**
     * Invalidate recycler view
     */
    private fun invalidateRecyclerView() {
        binding.recyclerview.adapter?.notifyDataSetChanged()
    }

    /**
     * Add hardcoded team to liveData
     */
    private fun addHardCodedTeam() {
        viewModel.clearTeamsData()
        invalidateRecyclerView()
        val hardCodeData = getTeamsList()
        for (i in hardCodeData.indices) {
            viewModel.add(hardCodeData[i])
        }
    }

    /**
     * Add team data taken from user into liveData
     */
    private fun addData() {
        val teamName = binding.etTeamName.text.toString()
        if (teamName.isBlank()) {
            Snackbar.make(activity.window.decorView, "Team data empty!!", Snackbar.LENGTH_LONG)
                .show()
        } else {
            val team = Team(teamName)
            val result = viewModel.add(team)
            if (result) {
                binding.etTeamName.text.clear()
                invalidateRecyclerView()
            } else {
                Toast.makeText(activity, "Team name already present", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Start match simulation by shuffling teams and creating matches among them
     */
    private fun startIPL() {
        if (!viewModel.isEvenTeamData()) {
            Toast.makeText(activity, "Add one more team to balance team count", Toast.LENGTH_LONG)
                .show()
        } else {
            insertIntoDb(viewModel.getTeamsList())
            val teamList = viewModel.getTeamsList().shuffled().toMutableList()

            val pairs = teamList.withIndex()
                .groupBy { it.index / 2 }
                .map { it -> it.value.map { it.value } }
            viewModel.addToFeaturedList(pairs)

            (activity as MainActivity).replaceFragment(FeatureFragment.newInstance())
        }
    }

    /**
     * Room DB implementation for reference, saves and retrieves
     */
    private fun insertIntoDb(teamsList: java.util.ArrayList<Team>) {
        for (i in 0 until teamsList.size) {
            TeamRepository(activity).insert(TeamEntity(i, teamName = teamsList[i].team))
        }
        val data = TeamRepository(activity).getAllTeams()
        for (element in data) {
            Log.d("rohit ", "Retrieved data is " + element.teamName)
        }
    }

}
package com.dec.indpreleague.ui.main.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dec.indpreleague.MainActivity
import com.dec.indpreleague.R
import com.dec.indpreleague.ui.main.data.Team
import com.dec.indpreleague.databinding.FeatureFragmentBinding
import com.dec.indpreleague.getWinnerAmongPair
import com.dec.indpreleague.ui.main.viewmodel.MainViewModel
import java.util.*

/**
 * Fragment to show list of matches between different teams
 */
class FeatureFragment : Fragment() {
    companion object {
        fun newInstance() = FeatureFragment()
    }

    private lateinit var mLinearLayout: LinearLayout
    private lateinit var binding: FeatureFragmentBinding
    private lateinit var activity: Activity
    private val viewModel: MainViewModel by activityViewModels()
    private var finalTeamsList = Stack<String>()

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

        binding = DataBindingUtil.inflate(inflater, R.layout.feature_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val appCompatActivity = activity
        if (appCompatActivity is AppCompatActivity) {
            appCompatActivity.setSupportActionBar(binding.toolbarHomepage)
            appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        registerObserver()
        return binding.root
    }

    /**
     * Observe liveData for updated list after winning matches
     */
    private fun registerObserver() {
        viewModel.featuredList.observe(viewLifecycleOwner) {
            populateUI(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSimulate.setOnClickListener {
            mLinearLayout.removeAllViews()
            val data = viewModel.featuredList.value
            when (data?.size) {
                1 -> {
                    showFinalWinnerUI(data)
                }
                3 -> {
                    showAlert(data)
                }
                else -> {
                    calculateWinner(data)
                }
            }
        }
    }

    /**
     * API to calculate winners
     */
    private fun calculateWinner(teamPairs: List<List<Team>>?) {
        val winner = arrayListOf<Team>()
        val listForThirdPos = arrayListOf<Team>()
        if (teamPairs != null) {
            for (teamItem in teamPairs) {
                winner.add(getWinnerAmongPair(teamItem))
            }
            if (teamPairs.size == 2) {
                val list = arrayListOf<Team>()
                for (item in teamPairs) {
                    list.add(item[0])
                    list.add(item[1])
                }
                val differences: MutableList<Team> = ArrayList(list)
                differences.removeAll(winner)

                for (item in differences) {
                    listForThirdPos.add(item)
                }
                finalTeamsList.push(getWinnerAmongPair(listForThirdPos).team)
            }
            if (teamPairs.size == 3) {
                /**
                 * assuming one team scores low so keeping it 3rd position
                 */
                finalTeamsList.push(winner[0].team)
                winner.removeAt(0)

//                Toast.makeText(
//                    activity,
//                    "Assuming one team scores lowest amount top 3, so giving it 3rd",
//                    Toast.LENGTH_LONG
//                ).show()
            }
            val teamList = winner.shuffled().toMutableList()
            val pairs = teamList.withIndex()
                .groupBy { it.index / 2 }
                .map { it -> it.value.map { it.value } }
            viewModel.addToFeaturedList(pairs)
        }
    }

    private fun showAlert(data: List<List<Team>>) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Information")
        builder.setMessage(
            "Among top 3 teams, one with lowest score rate is given 3rd place by default. And other two" +
                    " teams are sent to finals"
        )

        builder.setPositiveButton("Thanks for Info") { _, _ ->
            calculateWinner(data)
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    /**
     * Show Teams as group on UI
     */
    private fun populateUI(teamPairs: List<List<Team>>) {
        mLinearLayout = binding.llFeatureFragment

        if (teamPairs.size == 1) {//there are two teams
            binding.btnSimulate.text = getString(R.string.simulateAndEnd)
            binding.tvFinalMatch.visibility = View.VISIBLE
            addDataToView(teamPairs[0][0].team, teamPairs[0][1].team)
        } else {
            for (items in teamPairs) {
                addDataToView(items[0].team, items[1].team)
            }
        }
    }

    /**
     * Helper method to show teams as group on UI
     */
    private fun addDataToView(team1: String, team2: String) {
        val child = LinearLayout(activity)
        child.layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also { it.setMargins(0, 0, 0, 10) }
        child.orientation = LinearLayout.VERTICAL
        child.gravity = Gravity.CENTER_HORIZONTAL
        child.setPadding(0, 20, 0, 20)

        val textView1 = TextView(activity)
        textView1.text = team1
        textView1.textSize =
            activity.resources.getDimensionPixelOffset(R.dimen.text_size).toFloat()
        textView1.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val textView2 = TextView(activity)
        textView2.text = getString(R.string.stringForVs)
        textView2.textSize =
            activity.resources.getDimensionPixelOffset(R.dimen.text_size).toFloat()
        textView2.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val textView3 = TextView(activity)
        textView3.text = team2
        textView3.textSize =
            activity.resources.getDimensionPixelOffset(R.dimen.text_size).toFloat()
        textView3.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        try {
            child.addView(textView1)
            child.addView(textView2)
            child.addView(textView3)
            child.background = ResourcesCompat.getDrawable(resources, R.drawable.border, null)
            mLinearLayout.addView(child)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * API to show final winner
     */
    private fun showFinalWinnerUI(teamItem: List<List<Team>>?) {
        val winnerTeam: String
        val runnerUpTeam: String
        val winnerIndex = (0..1).random()
        if (winnerIndex == 0) {
            winnerTeam = teamItem!![0][0].team
            runnerUpTeam = teamItem[0][1].team
        } else {
            winnerTeam = teamItem!![0][1].team
            runnerUpTeam = teamItem[0][0].team
        }

        finalTeamsList.push(runnerUpTeam)
        finalTeamsList.push(winnerTeam)

        viewModel.setWinnerTeams(finalTeamsList)

        (activity as MainActivity).replaceFragment(WinnerFragment.newInstance())
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

}
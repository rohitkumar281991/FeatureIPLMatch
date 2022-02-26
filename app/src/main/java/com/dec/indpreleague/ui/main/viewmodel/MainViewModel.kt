package com.dec.indpreleague.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dec.indpreleague.ui.main.data.Team
import java.util.*

/**
 * ViewModel to handle live data
 */
class MainViewModel : ViewModel() {
    var lst = MutableLiveData<ArrayList<Team>>()
    var newlist = arrayListOf<Team>()
    var featuredList = MutableLiveData<List<List<Team>>>()
    var newFeaturedList = arrayListOf<List<Team>>()
    var winnerTeams = MutableLiveData<Stack<String>>()

    /**
     * LiveData to keep top 3 winners
     */
    fun setWinnerTeams(team: Stack<String>) {
        winnerTeams.value = team
    }

    /**
     * LiveData to keep list of Team features like random selected team matches
     */
    fun addToFeaturedList(teamPairList: List<List<Team>>) {
        newFeaturedList = teamPairList as ArrayList<List<Team>>
        featuredList.value = newFeaturedList
    }

    /**
     * Add teams to LiveData from hardcoded list or data taken from user
     */
    fun add(team: Team): Boolean {
        for (item in newlist) {
            if (item.team == team.team) {
                return false
            }
        }
        newlist.add(team)
        lst.value = newlist
        return true
    }

    /**
     * Returns team count
     */
    fun getTeamsCount(): Int {
        return newlist.size
    }

    /**
     * Make team list livedata empty
     */
    fun clearTeamsData() {
        newlist.clear()
        lst.value = newlist
    }

    /**
     * Checks if even number of teams are available to conduct matches
     */
    fun isEvenTeamData(): Boolean {
        return newlist.size % 2 == 0
    }

    /**
     * Returns team list
     */
    fun getTeamsList(): ArrayList<Team> {
        return newlist
    }

}
package com.dec.indpreleague

import com.dec.indpreleague.ui.main.data.Team

/**
 * API to get random Team among two teams as winner
 */
fun getWinnerAmongPair(pair: List<Team>): Team {
    return pair.random()
}

/**
 * API to get list of teams
 */
fun getTeamsList(): List<Team> {
    return listOf(
        Team("Mumbai Indians"),
        Team("SunRisers Hyderabad"),
        Team("Royal Challengers Bangalore"),
        Team("Rajasthan Royals"),
        Team("Chennai Super Kings"),
        Team("Kolkata Knight Riders"),
        Team("Delhi Capitals"),
        Team("Kings XI Punjab")
    )
}




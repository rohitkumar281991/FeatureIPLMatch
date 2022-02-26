package com.dec.indpreleague.ui.main.data

import android.content.Context
import android.os.AsyncTask

class TeamRepository(context: Context) {

    var db: TeamDao = AppDatabase.getInstance(context)?.teamDao()!!

    //Fetch All Teams
    fun getAllTeams(): List<TeamEntity> {
        return db.getAll()
    }

    // Insert new team
    fun insert(team: TeamEntity) {
        InsertAsyncTask(db).execute(team)
    }

    private class InsertAsyncTask internal constructor(private val teamDao: TeamDao) :
        AsyncTask<TeamEntity, Void, Void>() {

        override fun doInBackground(vararg params: TeamEntity): Void? {
            teamDao.insert(params[0])
            return null
        }
    }
}
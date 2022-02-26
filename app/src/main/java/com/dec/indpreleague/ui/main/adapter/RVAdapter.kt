package com.dec.indpreleague.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dec.indpreleague.R
import com.dec.indpreleague.ui.main.data.Team
import com.dec.indpreleague.databinding.RecyclerviewItemLayoutBinding

/**
 * Adapter class to show teams on recycler view
 */
class RVAdapter(private val arrayList: ArrayList<Team>) :
    RecyclerView.Adapter<RVAdapter.TeamsViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TeamsViewHolder {
        val binding: RecyclerviewItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_item_layout,
            parent,
            false
        )
        return TeamsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeamsViewHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class TeamsViewHolder(private val binding: RecyclerviewItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(team: Team) {
            binding.tvRecyclerviewItem.text = team.team
        }
    }

}
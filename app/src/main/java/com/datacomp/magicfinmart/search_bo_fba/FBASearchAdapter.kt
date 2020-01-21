package com.datacomp.magicfinmart.search_bo_fba

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.datacomp.magicfinmart.R
import kotlinx.android.synthetic.main.layout_bo_fba_search_item.view.*
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.BOFbaEntity

class FBASearchAdapter(val fbaList: List<BOFbaEntity>, val iboFbaCallback: IBOFbaCallback, val context: Context) :
        androidx.recyclerview.widget.RecyclerView.Adapter<FBASearchAdapter.FBAItem>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FBAItem {
        return FBAItem(LayoutInflater.from(context).inflate(R.layout.layout_bo_fba_search_item, parent, false))
    }

    override fun onBindViewHolder(holder: FBAItem, position: Int) {

        if (position == 0)
            holder.txtFBAName.text = fbaList.get(position).fullName
        else
            holder.txtFBAName.text = fbaList.get(position).fullName + " (FBA -" + fbaList.get(position).fbaid + ")"

        holder.llSearchFBA.setOnClickListener {
            if (position == 0)
                iboFbaCallback.getBOFBA(null)
            else
                iboFbaCallback.getBOFBA(fbaList.get(position))
        }
    }


    override fun getItemCount(): Int {

        return fbaList.size

    }


    class FBAItem(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {
        var txtFBAName = v.txtFBAName
        var llSearchFBA = v.llSearchFBA
    }
}
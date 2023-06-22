package com.policyboss.policybossproelite.attendance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.policyboss.policybossproelite.databinding.AttendanceListitemsLayoutBinding


import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.PbAttendHistoryEntity

class PolicyBossAttendanceAdapter( private var attendanceLst : List<PbAttendHistoryEntity>): RecyclerView.Adapter<PolicyBossAttendanceAdapter.AttendanceDataHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceDataHolder {

        val binding = AttendanceListitemsLayoutBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)

        return AttendanceDataHolder(binding)
    }

    override fun onBindViewHolder(holder: AttendanceDataHolder, position: Int) {

        holder.bind(attendanceLst[position])
    }

    override fun getItemCount() = attendanceLst.size

    inner class AttendanceDataHolder(val binding : AttendanceListitemsLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(entity: PbAttendHistoryEntity) = binding.apply {
      //"${entity.Log_Date} : ${entity.Log_Time}"
            txtDate.text = entity.Log_Date
            txtTime.text = entity.Log_Time
        }
    }

    fun  setData(attendanceLst: List<PbAttendHistoryEntity>){

        this.attendanceLst = attendanceLst
        notifyDataSetChanged()
    }

}
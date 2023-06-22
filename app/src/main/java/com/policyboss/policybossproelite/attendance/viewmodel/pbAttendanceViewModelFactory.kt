package com.policyboss.policybossproelite.attendance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.policyboss.policybossproelite.attendance.pbAttendanceRepository

class pbAttendanceViewModelFactory(private val repository: pbAttendanceRepository) :
    ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(pbAttendanceViewModel::class.java)){

            return pbAttendanceViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel class Not Found")

    }
}
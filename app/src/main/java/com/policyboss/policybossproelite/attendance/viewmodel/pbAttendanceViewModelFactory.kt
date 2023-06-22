package com.policyboss.policybossproelite.attendance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.policyboss.policybossproelite.attendance.pbAttendanceRepository
import com.policyboss.policybossproelite.oauthtoken.model.repository.OauthTokenRepository
import com.policyboss.policybossproelite.oauthtoken.model.viewmodel.OauthTokenViewModel

class pbAttendanceViewModelFactory(private val repository: pbAttendanceRepository) :
    ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(pbAttendanceViewModel::class.java)){

            return pbAttendanceViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel class Not Found")

    }
}
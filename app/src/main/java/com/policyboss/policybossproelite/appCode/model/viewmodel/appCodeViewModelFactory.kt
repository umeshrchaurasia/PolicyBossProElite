package com.policyboss.policybossproelite.appCode.model.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.policyboss.policybossproelite.appCode.model.repository.appcodeRepository


class appCodeViewModelFactory(private val repository: appcodeRepository) :ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(appCodeViewModel::class.java)){

            return appCodeViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel class Not Found")

    }
}
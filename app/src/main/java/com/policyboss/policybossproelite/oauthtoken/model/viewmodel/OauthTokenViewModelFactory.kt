package com.policyboss.policybossproelite.oauthtoken.model.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.policyboss.policybossproelite.oauthtoken.model.repository.OauthTokenRepository


class OauthTokenViewModelFactory(private val repository: OauthTokenRepository) :ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(OauthTokenViewModel::class.java)){

            return OauthTokenViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel class Not Found")

    }
}
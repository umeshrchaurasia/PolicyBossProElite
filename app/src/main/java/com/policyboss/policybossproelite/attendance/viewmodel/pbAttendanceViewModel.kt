package com.policyboss.policybossproelite.attendance.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.policyboss.policybossproelite.APIState
import com.policyboss.policybossproelite.attendance.pbAttendanceRepository
import com.policyboss.policybossproelite.utility.Constant
import com.policyboss.policybossproelite.utility.UTILITY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.pbAttendance.pbAttendRequestEntity

import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.PbAttendance.pbAttendResponse

class pbAttendanceViewModel (val pbRepository: pbAttendanceRepository) : ViewModel(){

    private var attendMutuableStateFlow : MutableStateFlow<APIState<pbAttendResponse>> = MutableStateFlow(
        APIState.Empty())

    // data is collected in OauthStateFlow variable, we have to get from here
    val attendStateFlow : StateFlow<APIState<pbAttendResponse>>
        get() = attendMutuableStateFlow


    fun getAttendance(url: String, pbAttendRequestEntity: pbAttendRequestEntity) = viewModelScope.launch {


        attendMutuableStateFlow.value = APIState.Loading()
        // delay(8000)
        try {

            pbRepository.getAttendance(url,pbAttendRequestEntity)
                .catch { it ->

                   // Log.d(Constant.TAG,"Error1"+it.message.toString())
                    attendMutuableStateFlow.value = APIState.Failure(errorMessage = UTILITY.ErrorMessage)
                    Log.d(Constant.TAG,"Error1"+it.message.toString())

                }.collect{ data ->

                    if(data.isSuccessful){


                        if(data.body()?.Status?.uppercase().equals("SUCCESS")){
                            attendMutuableStateFlow.value = APIState.Success(data = data.body())

                        }else{
                            attendMutuableStateFlow.value = APIState.Failure(errorMessage = data.body()?.message ?: UTILITY.ErrorMessage)
                        }

                    }else{
                        attendMutuableStateFlow.value = APIState.Failure(errorMessage = UTILITY.ErrorMessage)
                    }

                }
        }catch (ex : Exception){

            attendMutuableStateFlow.value = APIState.Failure(errorMessage = UTILITY.ErrorMessage)
            Log.d(Constant.TAG, "Error2"+ ex.message.toString())
        }
    }


}
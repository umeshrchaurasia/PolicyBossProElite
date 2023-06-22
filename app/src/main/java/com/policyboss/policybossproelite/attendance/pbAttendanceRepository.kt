package  com.policyboss.policybossproelite.attendance


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestbuilder.Syncontact.SyncContactInterface
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.pbAttendance.pbAttendRequestEntity


class pbAttendanceRepository (private val apiService : SyncContactInterface) {


    suspend fun getAttendance(strURL : String ,pbAttendRequestEntity : pbAttendRequestEntity) = flow {


        val response = apiService.getPBAttendance(strUrl = strURL, body = pbAttendRequestEntity)
        emit(response)

    }.flowOn(Dispatchers.IO)


}
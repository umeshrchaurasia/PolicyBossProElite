package magicfinmart.datacomp.com.finmartserviceapi.finmart.response.PbAttendance

import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.PbAttendHistoryEntity

data class pbAttendResponse(
    val Status : String?,
    val message : String?,
    val Details: List<PbAttendHistoryEntity>,

    )
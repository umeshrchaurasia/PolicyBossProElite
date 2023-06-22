package com.utility.finmartcontact.core.requestentity

import com.utility.finmartcontact.core.model.CallLogEntity

data class CallLogRequestEntity(
    val call_history: List<CallLogEntity>,
    val fba_id: Int,
    val sub_fba_id: Int,
    var device_id : String,
    val ss_id: Int
)
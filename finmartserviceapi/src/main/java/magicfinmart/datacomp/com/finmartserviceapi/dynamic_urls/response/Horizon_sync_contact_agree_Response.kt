package magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.response

import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.model.sync_contact_agree

data class Horizon_sync_contact_agree_Response(
    val Msg: List<sync_contact_agree>,
    val Status: String
)
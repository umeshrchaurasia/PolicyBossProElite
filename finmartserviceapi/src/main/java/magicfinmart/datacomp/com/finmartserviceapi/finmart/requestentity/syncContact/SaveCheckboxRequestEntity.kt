package magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.syncContact

data class SaveCheckboxRequestEntity(
    val fba_id: Int,
    val is_sms: String,
    val is_call: String,
    val online_agreement: String,
    val ss_id: Int
)
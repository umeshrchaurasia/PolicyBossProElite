package magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.response

data class RM(
    val message: Any,
    val rm_details: RmDetails,
    val rm_reporting_details: RmReportingDetails,
    val status: String
)
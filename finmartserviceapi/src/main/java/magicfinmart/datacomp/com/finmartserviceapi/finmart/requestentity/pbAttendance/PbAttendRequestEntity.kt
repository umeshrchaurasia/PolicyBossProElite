package magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.pbAttendance

data class pbAttendRequestEntity(
    val DeviceId: String,
    val UID: String,
    val key: String,
    val lat: String,
    val lng: String,
    val name: String
)
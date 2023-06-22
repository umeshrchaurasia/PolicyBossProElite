package com.policyboss.policybossproelite.attendance


import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.policybosscaller.Utility.showSnackbar
import com.google.android.gms.location.LocationRequest
import com.google.android.material.snackbar.Snackbar
import com.policyboss.policybossproelite.APIState

import com.policyboss.policybossproelite.attendance.viewmodel.pbAttendanceViewModelFactory
import com.policyboss.policybossproelite.attendance.viewmodel.pbAttendanceViewModel
import com.policyboss.policybossproelite.databinding.ActivityPolicyBossAttendanceBinding
import com.policyboss.policybossproelite.utility.Constant
import com.policyboss.policybossproelite.utility.LocationService
import com.policyboss.policybossproelite.utility.UTILITY
import com.policyboss.policybossproelite.utility.UtilityNew
import magicfinmart.datacomp.com.finmartserviceapi.PrefManager
import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LoginResponseEntity
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.UserConstantEntity
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.pbAttendance.pbAttendRequestEntity
import magicfinmart.datacomp.com.finmartserviceapi.finmart.retrobuilder.RetroHelper
import com.policyboss.policybossproelite.R
import com.policyboss.policybossproelite.databinding.ProgressdialogLoadingBinding
import kotlinx.coroutines.*


class PolicyBossAttendanceActivity : AppCompatActivity(), LocationService.ILocation {

    private lateinit var mainBinding: ActivityPolicyBossAttendanceBinding
    private lateinit var layout : View
    lateinit var viewModel: pbAttendanceViewModel
    lateinit var loginResponseEntity: LoginResponseEntity
    lateinit var userConstantEntity: UserConstantEntity
    lateinit var prefManager: PrefManager

    lateinit var mAdapter: PolicyBossAttendanceAdapter
    lateinit var btn_submit_attendance: Button
    private val REQUEST_CODE_ASK_PERMISSIONS = 1111

    private lateinit  var locationService: LocationService
    lateinit var showDialog: Dialog
    var isSubmitLocationCliked = false
    var perms = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private var Lat = ""
    private var Lon = ""

    lateinit var locationRequest: LocationRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityPolicyBossAttendanceBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        layout = mainBinding.root


        setSupportActionBar(mainBinding.toolbar)
        supportActionBar?.apply {

            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
           // setTitle("PolicyBoss Attendance")
        }

        showDialog = Dialog(this@PolicyBossAttendanceActivity, R.style.Dialog)
        // Initialize Sevice

        locationService = LocationService(this@PolicyBossAttendanceActivity,this@PolicyBossAttendanceActivity)

        prefManager = PrefManager(this)

        btn_submit_attendance = mainBinding.btnSubmitAttendance

        btn_submit_attendance.setVisibility(View.VISIBLE);


        requestFineLocationPermission()

        init()

        setListener ()

        observePunchAttendance()

//        layout.showSnackbar(
//            "Required Device Location",
//            Snackbar.LENGTH_INDEFINITE,
//            "ALLOW"){
//
//            UtilityNew.settingDialog(context = this, ::settingDialogAction)
//
//        }
    }

    private fun init(){


        mainBinding.lySubmit.visibility = View.GONE
        mainBinding.lyheader.visibility = View.GONE
        var repository = pbAttendanceRepository( RetroHelper.api)
        var viewModelFactory = pbAttendanceViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(pbAttendanceViewModel::class.java)

        var db =  DBPersistanceController(this)

        loginResponseEntity = db.getUserData()
        userConstantEntity = db.userConstantsData
        bindRecyclerView()
        cancelAnimDialog()

    }

    // region Method
    private fun bindRecyclerView(){

        mAdapter = PolicyBossAttendanceAdapter(ArrayList())
        mainBinding.rvAttendace.apply {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@PolicyBossAttendanceActivity)
            adapter = mAdapter
        }
    }

    fun setListener (){

        mainBinding.btnLocation.setOnClickListener {


           // isCurrentLocationCliked = true
           // showAnimDialog()
            mainBinding.lyheader.visibility = View.GONE
            clearlatlon()
            isSubmitLocationCliked = false
            requestFineLocationPermission()

           // cancelAnimDialog()

        }

        mainBinding.btnSubmitAttendance.setOnClickListener {


            submitAttend()

        }





    }

    fun submitAttend() {


        mainBinding.txtMessage.text =""
        mainBinding.lyheader.visibility = View.GONE

        if(checkPermission()){

            if(isLocationEnabled(this@PolicyBossAttendanceActivity)){

                showAnimDialog()

                handlingSaveLatLonButton(isblnEnable = false)

                locationService.createLocationRequest()

                isSubmitLocationCliked = true
            }else{

               clearlatlon()
                Snackbar.make(layout, "Please Enable Device Location..",
                        Snackbar.LENGTH_LONG).show()

            }


        }else{

            requestFineLocationPermission()
        }



    }

    fun saveAttendData(){

        if (!Lat.isEmpty()) {

            var key = "K!R:|A*J$" + "P*"

            val attendRequestEntity = pbAttendRequestEntity(

                DeviceId = UTILITY.getDeviceID(this@PolicyBossAttendanceActivity),
                UID = userConstantEntity.userid,
                key = key,
                lat = Lat,
                lng = Lon,
                name = loginResponseEntity.fullName
            )

            viewModel.getAttendance(Constant.pbAttendanceURL, attendRequestEntity)

        } else {

            handlingSaveLatLonButton(isblnEnable = true)
            layout.showSnackbar(
                "Location are not found",
                Snackbar.LENGTH_SHORT
            )


        }
    }

    fun clearlatlon(){
        mainBinding.tvLatitude.text = ""
        mainBinding.tvLongitude.text = ""
        mainBinding.txtMessage.text = ""
        Lat = ""
        Lon = ""
    }

    fun handlingSaveLatLonButton(isblnEnable: Boolean){

        if(isblnEnable){

           // btn_submit_attendance.setBackgroundColor(ContextCompat.getColor(this@PolicyBossAttendanceActivity, R.color.green_descent))
           // btn_submit_attendance.setBackgroundResource(R.drawable.bg_primary_rect)
          //  btn_submit_attendance.background?.alpha = 100
            btn_submit_attendance.alpha = 1F
           // btn_submit_attendance.isEnabled = true
        }else{
          //  btn_submit_attendance.background?.alpha = 80
            btn_submit_attendance.alpha = 0.6F
           // btn_submit_attendance.isEnabled = false
        }

    }



    //endregion


    //region Observe the response
    private fun observePunchAttendance(){


        lifecycleScope.launch{

            repeatOnLifecycle(Lifecycle.State.STARTED){
                //collect date from flow  Variable
                viewModel.attendStateFlow.collect{

                    when(it){

                        is APIState.Loading ->{

                            showDialog()
                        }
                        is APIState.Success ->{

                            cancelDialog()
                            cancelAnimDialog()
                            handlingSaveLatLonButton(isblnEnable = true)
                            if(it != null){
                                it.data?.let{

                                    mainBinding.txtMessage.setTextColor( ContextCompat.getColor(
                                        this@PolicyBossAttendanceActivity,
                                        R.color.header_dark_text))
                                    mainBinding.txtMessage.setText( it.message ?: "Attendance marked successfully..")

                                    mainBinding.lyheader.visibility = View.VISIBLE
                                    mAdapter.setData(it.Details)

                                }
                            }


                        }

                        is APIState.Failure -> {

                            cancelDialog()
                            cancelAnimDialog()
                            clearlatlon()
                            handlingSaveLatLonButton(isblnEnable = true)
                         //   mainBinding.txtMessage.setTextColor()
                            mainBinding.txtMessage.setTextColor( ContextCompat.getColor(
                                this@PolicyBossAttendanceActivity,
                                R.color.red_custom))

                            mainBinding.txtMessage.setText( it.errorMessage ?: "No Data Found")


                            layout.showSnackbar(
                                ""+ it.errorMessage.toString(),
                                Snackbar.LENGTH_SHORT
                                )
                        }

                        is APIState.Empty ->{

                            cancelDialog()

                        }
                    }


                }
            }
        }
    }

    //endregion

    //region Get permission and Location
    private fun checkPermission(): Boolean {
        val fine_location = ActivityCompat.checkSelfPermission(this, perms.get(0))

        return fine_location == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, perms, REQUEST_CODE_ASK_PERMISSIONS)
    }

    override fun getLocation(location: Location?) {


        if (location != null) {


         //  cancelAnimDialog()
            locationService.stopLocationUpdates()

            Lat = location.latitude.toString()
            Lon = location.longitude.toString()

            mainBinding.tvLatitude.text = location.latitude.toString()
            mainBinding.tvLongitude.text = location.longitude.toString()
            mainBinding.lySubmit.visibility = View.VISIBLE


            if(isSubmitLocationCliked ){

                saveAttendData()
            }


        }
    }

    fun requestFineLocationPermission() = fineLocationPermissionLauncher.launch(  Manifest.permission.ACCESS_FINE_LOCATION)




    private  val fineLocationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){

            isGranted ->
        // Do something if permission granted
        if (isGranted) {
            Log.i("DEBUG", "permission granted")

            //***********************************************************************
            //Note: If location permission granted than we go for Location gps
            locationService.createLocationRequest()


        } else {
            Log.i("DEBUG", "permission denied")
            //binding.tvCallLogInfo.setImageDrawable(ContextCompat.getDrawable(this@SettingActivity, R.drawable.circular_exclaimlayer))
            handlingSaveLatLonButton(isblnEnable = true)

            layout.showSnackbar(
                "Required for PolicyBoss Pro to get Location",
                Snackbar.LENGTH_INDEFINITE,
                "ALLOW"){

                UtilityNew.settingDialog(context = this, ::settingDialogAction)

            }
        }
    }

    //  endregion


    //region Dialog
    fun settingDialogAction( strType : String, dialog : DialogInterface){


        when(strType){

            "Y" -> {

                dialog.dismiss()
                openSetting(context =this)

            }

            "N" -> {


                dialog.dismiss()

            }

        }

    }

    fun openSetting(context: Context){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    private fun showDialog() {
        try {
            if (!this@PolicyBossAttendanceActivity.isFinishing()) {
                if (!showDialog.isShowing()) {
                    val dialogLoadingBinding: ProgressdialogLoadingBinding =
                        ProgressdialogLoadingBinding.inflate(
                            layoutInflater
                        )
                    showDialog.setContentView(dialogLoadingBinding.getRoot())
                    showDialog.setCancelable(false)
                    showDialog.show()
                }
            }
        } catch (e: Exception) {
            showDialog.dismiss()
        }
    }

    private fun cancelDialog() {
        try {
            if (showDialog != null) {
                showDialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showDialog.dismiss()
        }
    }
    ///

    fun showAnimDialog(){
          mainBinding.txtLoader.visibility = View.VISIBLE

    }

    fun cancelAnimDialog(){


       mainBinding.txtLoader.visibility = View.GONE
    }

    @SuppressLint("ServiceCast")
    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    //endregion


    //region  Event
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationService.stopLocationUpdates()
    }



   //endregion

}
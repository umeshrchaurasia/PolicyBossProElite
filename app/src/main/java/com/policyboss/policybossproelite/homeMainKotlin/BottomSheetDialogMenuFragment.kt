package com.policyboss.policybossproelite.homeMainKotlin

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.policyboss.policybossproelite.R
import com.policyboss.policybossproelite.appCode.appcodeActivity
import com.policyboss.policybossproelite.attendance.PolicyBossAttendanceActivity
import com.policyboss.policybossproelite.certificate.POSP_certicate_appointment
import com.policyboss.policybossproelite.change_password.ChangePasswordActivity
import com.policyboss.policybossproelite.databinding.FragmentBottomSheetMenuDialogBinding
import com.policyboss.policybossproelite.generatelead.GenerateLeadActivity
import com.policyboss.policybossproelite.helpfeedback.raiseticket.RaiseTicketActivity
import com.policyboss.policybossproelite.homeMainKotlin.Adapter.MenuAdapter
import com.policyboss.policybossproelite.homeMainKotlin.menuRepository.DBMenuRepository
import com.policyboss.policybossproelite.messagecenter.messagecenteractivity
import com.policyboss.policybossproelite.myaccount.MyAccountActivity
import com.policyboss.policybossproelite.mybusiness.MyBusinessActivity
import com.policyboss.policybossproelite.posp.PospEnrollment
import com.policyboss.policybossproelite.posp.PospListActivity
import com.policyboss.policybossproelite.syncContact.Worker.WelcomeSyncContactActivityKotlin
import com.policyboss.policybossproelite.transactionhistory.nav_transactionhistoryActivity
import com.policyboss.policybossproelite.utility.Constants
import com.policyboss.policybossproelite.utility.NetworkUtils
import com.policyboss.policybossproelite.webviews.CommonWebViewActivity
import com.policyboss.policybossproelite.webviews.PrivacyWebViewActivity
import magicfinmart.datacomp.com.finmartserviceapi.PrefManager
import magicfinmart.datacomp.com.finmartserviceapi.Utility
import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LoginResponseEntity
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.UserConstantEntity
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.UserConstatntResponse
import magicfinmart.datacomp.com.finmartserviceapi.model.MenuChild
import org.json.JSONObject


open class BottomSheetDialogMenuFragment : BottomSheetDialogFragment() , IResponseSubcriber ,View.OnClickListener {
    // TODO: Rename and change types of parameters

    lateinit var db: DBPersistanceController
    lateinit var prefManager: PrefManager

    lateinit var userConstantEntity: UserConstantEntity
    lateinit var loginResponseEntity: LoginResponseEntity

    lateinit var binding : FragmentBottomSheetMenuDialogBinding
    lateinit var adapterMenu : MenuAdapter
    lateinit var MyUtilitiesDialog :AlertDialog

    lateinit var mCallback: IBottomMenuCallback

    val mapKey = "map_switchuser"

    // for registeration use onAttached method
    open fun registerCallBack(mCallback: IBottomMenuCallback) {
        if (mCallback != null)
            this.mCallback = mCallback
    }

    open interface IBottomMenuCallback {

        fun onClickLogout()

        fun onSwitchUser()

        fun ConfirmnMyUtilitiesAlert()
    }
    companion object {

        @JvmStatic
        fun newInstance() = BottomSheetDialogMenuFragment()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)

//        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.setOnShowListener(object : DialogInterface.OnShowListener {
//            override fun onShow(dialogInterface: DialogInterface?) {
//                val bottomSheetDialog = dialogInterface as BottomSheetDialog?
//                setupRatio(bottomSheetDialog!!)
//            }
//        })
//        return dialog


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mCallback = try {
            context as IBottomMenuCallback
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener")
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        binding = FragmentBottomSheetMenuDialogBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        db = DBPersistanceController(requireContext())
        userConstantEntity = db.userConstantsData
        loginResponseEntity = db.userData

        prefManager = PrefManager(requireContext())



        init()

        switchUserBinding()

        setListener()

        bindMenuData()


    }

    private fun init(){

        if (loginResponseEntity != null) {

            binding.apply {
                txtFbaID.text = "Fba Id - " + loginResponseEntity.fbaId
                txtReferalCode.text = "Referral Code - " + loginResponseEntity.referer_code

            } //switchUserBinding()   05 temp
        } else {
            binding.apply {

                txtFbaID.text = "Fba Id - "
                txtReferalCode.text = "Referral Code - "
            }


        }


        if (this::userConstantEntity.isInitialized)
         {
            try {
                binding.apply {
                    txtPospNo.text = "Posp No - " + userConstantEntity.pospselfid
                    txtErpID.text = "Erp Id - " + userConstantEntity.erpid
                }

            } catch (e: Exception) { }
        } else {
            try {
                binding.apply {
                    txtPospNo.text = ""
                    txtErpID.text = ""
                }

            } catch (e: Exception) { }
        }

    }

    private fun setListener(){

        binding.lstswitchuser.setOnClickListener(this)

    }

    private fun bindMenuData(){


        adapterMenu = MenuAdapter(this, DBMenuRepository.getMenuMainList(loginResponseEntity,userConstantEntity, prefManager))

        binding.apply {

            rvBottomMenuDialog.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvBottomMenuDialog.setHasFixedSize(true)

            rvBottomMenuDialog.adapter = adapterMenu

        }

        // rvBottomMenuDialog.isNestedScrollingEnabled = false


        binding.BtnDismiss.setOnClickListener {
            // on below line we are calling a dismiss
            // method to close our dialog.
             dismiss()

        }


    }


     open fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        //id = com.google.android.material.R.id.design_bottom_sheet for Material Components
        //id = android.support.design.R.id.design_bottom_sheet for support librares
        val bottomSheet: RelativeLayout? = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as RelativeLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from<View?>(bottomSheet)
        val layoutParams: ViewGroup.LayoutParams = bottomSheet!!.getLayoutParams()


        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.setLayoutParams(layoutParams)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

     open fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 90 / 100
    }

     open fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)?.getWindowManager()?.getDefaultDisplay()?.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun getMenuLogOut()  {


        this.dismiss()
        mCallback.onClickLogout()



    }

    fun getNavigationMenu(menuChild: MenuChild)  {


        if (!NetworkUtils.isNetworkAvailable(this.requireContext())) {


            Toast.makeText(this.requireContext(),getString(R.string.noInternet), Toast.LENGTH_SHORT ).show()

            return
        }
        this.dismiss()



            when (menuChild.getmId()) {

                "nav_myaccount" -> {

                    startActivity(Intent(requireContext(), MyAccountActivity::class.java))

                }

                "nav_My_Attendance" -> {

                    startActivity(Intent(requireContext(), PolicyBossAttendanceActivity::class.java))

                }
                "nav_ouathEnabled" -> {

                    startActivity(Intent(requireContext(),appcodeActivity::class.java))

                }

                "nav_pospenrollment" -> {

                    startActivity(Intent(requireContext(), PospEnrollment::class.java))

                }

                "nav_addposp" -> {

                    startActivity(Intent(requireContext(), PospListActivity::class.java))

                }


                "nav_leaddetail" -> {

                    //                if (new DBPersistanceController(this).getUserConstantsData().getInsurancerepositorylink() == null) {
//                    startActivity(new Intent(this, KnowledgeGuruWebviewActivity.class)
//                            .putExtra("URL", "http://www.policyboss.com/repository/page.html")
//                            .putExtra("NAME", "INSURANCE REPOSITORY")
//                            .putExtra("TITLE", "INSURANCE REPOSITORY"));
//                } else {
                    var leaddetail = ""
                    val append_lead =
                        "&ip_address=&mac_address=&app_version=" + prefManager.appVersion + "&device_id="+prefManager.deviceID+ "&login_ssid="
                    leaddetail = userConstantEntity.leadDashUrl + append_lead



                    startActivity(Intent(requireContext(), CommonWebViewActivity::class.java) // .putExtra("URL", "http://bo.magicfinmart.com/motor-lead-details/" + String.valueOf(loginResponseEntity.getFBAId()))
                            .putExtra("URL", "" + leaddetail )
                            .putExtra("NAME", "" + "Sync Contact DashBoard")
                            .putExtra("TITLE", "" + "Sync Contact DashBoard"))

                }

                "nav_raiseTicket" -> {

                    if (userConstantEntity.raiseTickitEnabled == "0") {
                        startActivity(Intent(requireContext(), RaiseTicketActivity::class.java))
                    } else {
                        startActivity(Intent(requireContext(), CommonWebViewActivity::class.java)
                                .putExtra("URL", userConstantEntity.raiseTickitUrl + "&mobile_no=" + userConstantEntity.mangMobile
                                        + "&UDID=" + userConstantEntity.userid+"&app_version="+prefManager.getAppVersion()
                                        +"&device_code="+prefManager.getDeviceID()+"&ssid="+userConstantEntity.pospNo+"&fbaid="+loginResponseEntity.fbaId)
                                .putExtra("NAME", "RAISE_TICKET")
                                .putExtra("TITLE", "RAISE TICKET"))
                    }
                }
                "nav_changepassword" -> {

                    startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))

                }


                "nav_transactionhistory" -> {

                    startActivity(Intent(requireContext(), nav_transactionhistoryActivity::class.java))

                }


                "nav_contact" -> {

                    // 05 temp
                  //  startActivity(Intent(requireContext(), ContactLeadActivity::class.java))
                  //  startActivity(Intent(requireContext(), SyncContactActivity::class.java))
                   // startActivity(Intent(requireContext(), WelcomeSyncContactActivity::class.java))
                    startActivity(Intent(requireContext(), WelcomeSyncContactActivityKotlin::class.java))

                }

                // Shifted in Send Sms Template
//                "nav_sendSmsTemplate" -> {
//
//                    startActivity(Intent(requireContext(), SendTemplateSmsActivity::class.java))
//
//                }


                "nav_sendSmsTemplate" -> {

                    MessageCenter()
                }

                "nav_mybusiness_insurance" -> {

                    startActivity(Intent(requireContext(), MyBusinessActivity::class.java))

                }

                "nav_crnpolicy" -> {

                    if ((userConstantEntity?.pbByCrnSearch ?: "").length > 0) {

                        startActivity(Intent(requireContext(), CommonWebViewActivity::class.java)
                                .putExtra("URL", userConstantEntity.pbByCrnSearch)
                                .putExtra("NAME", "" + "Search CRN")
                                .putExtra("TITLE", "" + "Search CRN"))

                    } else {
                        Toast.makeText(requireContext(), "Please contact to your RM", Toast.LENGTH_SHORT).show()
                    }
                }
                "nav_AppointmentLetter" -> {

                    startActivity(Intent(requireContext(), POSP_certicate_appointment::class.java)
                            .putExtra("TYPE", "1"))

                }

                "nav_Certificate" -> {

                    startActivity(Intent(requireContext(), POSP_certicate_appointment::class.java)
                            .putExtra("TYPE", "0"))

                }

                "nav_generateLead" -> {

                    startActivity(Intent(requireContext(), GenerateLeadActivity::class.java))

                }

                "nav_MYUtilities" -> {

                    mCallback.ConfirmnMyUtilitiesAlert()
                }
                "nav_disclosure" -> {

                    startActivity(Intent(requireContext(), CommonWebViewActivity::class.java)
                            .putExtra("URL", "file:///android_asset/Disclosure.html")
                            .putExtra("NAME", "DISCLOSURE")
                            .putExtra("TITLE", "DISCLOSURE"))
                }

                "nav_policy" -> {

                    startActivity(Intent(requireContext(), CommonWebViewActivity::class.java)
                            .putExtra("URL", "https://www.policyboss.com/privacy-policy-policyboss-pro-elite?ss_id="+userConstantEntity.getPOSPNo()+"&app_version="+prefManager.getAppVersion()+"&device_code="+prefManager.getDeviceID()+"&fbaid="+userConstantEntity.fbaId)
                            .putExtra("NAME", "PRIVACY POLICY")
                            .putExtra("TITLE", "PRIVACY POLICY"))

                }
                "nav_delete" -> {

                    startActivity(Intent(requireContext(), PrivacyWebViewActivity::class.java)
                        .putExtra("URL", "https://www.policyboss.com/initiate-account-deletion-elite?ss_id="+userConstantEntity.getPOSPNo()+"&app_version="+prefManager.getAppVersion()+"&device_code="+prefManager.getDeviceID()+"&fbaid="+userConstantEntity.fbaId)
                        .putExtra("NAME", "ACCOUNT-DELETE")
                        .putExtra("TITLE", "ACCOUNT-DELETE"))



                }



//                "nav_logout" -> {
//
//
//                    mCallback.onClickLogout()
//                }

            }



    }

    open fun loadMap(): Map<String, String>? {
        val outputMap: MutableMap<String, String> = HashMap()
        val pSharedPref: SharedPreferences = requireContext().getSharedPreferences(Constants.SWITCh_ParentDeatils_FINMART,
                Context.MODE_PRIVATE)
        try {
            if (pSharedPref != null) {
                val jsonString = pSharedPref.getString(mapKey, JSONObject().toString())
                val jsonObject = JSONObject(jsonString)
                val keysItr = jsonObject.keys()
                while (keysItr.hasNext()) {
                    val key = keysItr.next()
                    outputMap[key] = jsonObject[key].toString()
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return outputMap
    }

    private fun switchUserBinding() {


        //region Switch user Binding
        val outputMap = loadMap()
        if (outputMap != null && outputMap.size > 0) {
            binding.lstswitchuser.setVisibility(View.GONE)
            binding.lstswitchChildUser.setVisibility(View.VISIBLE)
            val mystring: String = "Parent :- " + outputMap["Parent_name"]
            val content = SpannableString(mystring)
            content.setSpan(UnderlineSpan(), 0, mystring.length, 0)
            binding.txtparentuser.setText(content)
            val currentChild = outputMap["Child_name"]
            binding.txtchilduser.setText(currentChild)




        } else {
            if (loginResponseEntity.isUidLogin == "Y") {
                binding.lstswitchuser.setVisibility(View.VISIBLE)
                binding.lstswitchChildUser.setVisibility(View.GONE)
            } else {
                binding.lstswitchuser.setVisibility(View.GONE)
                binding.lstswitchChildUser.setVisibility(View.GONE)
            }
        }



        //endregion
    }



    private fun MessageCenter() {
        val POSPNO = "" + userConstantEntity.pospsendid
        val msgurl = "" + userConstantEntity.messagesender
        //   empCode="232";
        if (POSPNO == "5") {
            startActivity(Intent(this.requireContext(), messagecenteractivity::class.java))
        } else {
            var ipaddress = "0.0.0.0"
            ipaddress = try {
                Utility.getinfoaddress(requireContext())
            } catch (io: java.lang.Exception) {
                "0.0.0.0"
            }
            val append = ("&ip_address=" + ipaddress
                    + "&app_version=policyboss-" + Utility.getVersionName(requireContext())
                    + "&device_id=" + Utility.getDeviceId(requireContext()))
            val fullmsgurl = msgurl + append
            startActivity(Intent(requireContext(), CommonWebViewActivity::class.java)
                    .putExtra("URL", fullmsgurl)
                    .putExtra("NAME", "Message Center")
                    .putExtra("TITLE", "Message Center"))

            //   incl_nav.setVisibility(View.VISIBLE);
            //  new PendingController(this).gettransactionhistory(empCode, "1", this);
        }
    }





    override fun OnSuccess(response: APIResponse?, message: String?) {


        if (response is UserConstatntResponse) {

        }

    }




    override fun OnFailure(t: Throwable?) {


    }

    override fun onClick(view: View?) {

        when(requireView().id) {

            R.id.lstswitchuser -> {

                this.dismiss()

                mCallback.onSwitchUser()   // zCallback to HomeMain


            }



        }

    }


}



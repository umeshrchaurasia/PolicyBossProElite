package com.policyboss.policybossproelite.syncContact.Worker

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.policyboss.policybossproelite.BaseActivity
import com.policyboss.policybossproelite.R
import com.policyboss.policybossproelite.databinding.ActivityWelcomeSyncContactNewBinding
import com.policyboss.policybossproelite.databinding.DialogLoadingBinding
import com.policyboss.policybossproelite.syncContact.Worker.WelcomeSyncContactActivity.MyViewPagerAdapter
import com.policyboss.policybossproelite.utility.NetworkUtils.Companion.isNetworkAvailable
import com.policyboss.policybossproelite.webviews.CommonWebViewActivity
import kotlinx.coroutines.*
import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.UserConstantEntity
import magicfinmart.datacomp.com.finmartserviceapi.finmart.retrobuilder.RetroHelper

class WelcomeSyncContactActivityNew : BaseActivity() , OnClickListener {

    lateinit var binding: ActivityWelcomeSyncContactNewBinding
    private lateinit var dialogAnim : Dialog
    val TAG = "HORIZONEMP"

    lateinit var viewPager: ViewPager
    lateinit var myViewPagerAdapter: MyViewPagerAdapter
    lateinit var dotsLayout: LinearLayout
    lateinit var  layouts: IntArray
    lateinit var btnNext: Button

    lateinit var txtprivacy:TextView
    lateinit  var txtterm:TextView
    var current = 0
    lateinit var btnchkagree: CheckBox
    lateinit  var ll_term: LinearLayout
    var isContactSync = "0"
    lateinit var userConstantEntity: UserConstantEntity

    var POSPNO = ""

//userConstantEntity!!.pospNo
    var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            //addBottomDots(position);
            current = position
            setSelectedDot(position + 1)
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.size - 1) {
                // last page. make button text to GOT IT
                btnNext.text = "GET STARTED"
                //  btnNext.setVisibility(View.GONE);
                btnNext.isEnabled = false
                btnNext.alpha = 0.4f
                btnNext.tag = 0
                ll_term!!.visibility = View.VISIBLE


                // btnSkip.setVisibility(View.VISIBLE);
            } else {
                // still pages are left
                ll_term!!.visibility = View.GONE
                //btnNext.setVisibility(View.VISIBLE);
                btnNext.tag = 1
                btnNext.alpha = 1f
                btnNext.text = "NEXT"
                btnchkagree!!.isChecked = false
                btnNext.isEnabled = true
                //  btnNext.setVisibility(View.GONE);
                // btnSkip.setVisibility(View.VISIBLE);
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_welcome_sync_contact_new)

        binding = ActivityWelcomeSyncContactNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialogAnim = Dialog(this)
        userConstantEntity = DBPersistanceController(this).userConstantsData

        POSPNO = userConstantEntity.pospNo

        init_widgets()
        setListener()

//        if (!isNetworkAvailable(this)) {
//            Snackbar.make(binding.root, getString(R.string.noInternet), Snackbar.LENGTH_SHORT).show()
//            return
//        }
        showAnimDialog("")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                //showDialog()
                getHorizonDetails()


            }catch (e: Exception){

                withContext(Dispatchers.Main) {
                    viewPager.visibility = View.VISIBLE
                    cancelAnimDialog()
                }
            }
        }

//        if (intent.getStringExtra("Is_Contact_Sync") != null) {
//            isContactSync = intent.getStringExtra("Is_Contact_Sync")!!
//        }







    }

    private fun init_widgets() {

        layouts = intArrayOf(
            R.layout.sync_welcome_slide1,
            R.layout.sync_welcome_slide2,
            R.layout.sync_welcome_slide3
        )
        viewPager = binding.viewPager
        viewPager.visibility = GONE
        dotsLayout = binding.layoutDots

        btnNext = binding.btnNext
        btnchkagree = binding.chkagree

        txtterm = binding.txtterm
        txtprivacy = binding.txtprivacy

        ll_term = binding.llTerm
        btnchkagree!!.tag = 0
    }

    private fun setListener() {
        myViewPagerAdapter = MyViewPagerAdapter()
        viewPager!!.adapter = myViewPagerAdapter

        viewPager!!.addOnPageChangeListener(viewPagerPageChangeListener)
        btnNext.setOnClickListener(this)
        btnchkagree.setOnClickListener(this)

        txtprivacy.setOnClickListener(this)
        txtterm.setOnClickListener(this)

        ll_term.setOnClickListener(this)
        ll_term.visibility = View.GONE
        btnchkagree!!.isChecked = false
    }

    private fun setSelectedDot(current: Int) {
        binding.dot1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unselected_dot))
        binding.dot2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unselected_dot))
        binding.dot3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unselected_dot))
        when (current) {
            1 -> binding.dot1.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.indicator_active
                )
            )
            2 -> binding.dot2.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.indicator_active
                )
            )
            3 -> binding.dot3.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.indicator_active
                )
            )
        }
    }

    private suspend fun getHorizonDetails(){

        withContext(Dispatchers.IO){

           // var url =  "https://horizon.policyboss.com:5443/sync_contacts" + "/contact_entry"
            var url = "https://horizon.policyboss.com:5443/posps/dsas/view/" + POSPNO
            val resultRespAsync = async { RetroHelper.api.getHorizonDetails(url) }
             val resultResp = resultRespAsync.await()
            if (resultResp.isSuccessful) {
               // cancelDialog()
                Log.d(TAG, resultResp.toString())
                if(resultResp.body()?.status.equals("SUCCESS")){

                    var HorizonEmpDetailResponse = resultResp.body()
                    isContactSync = HorizonEmpDetailResponse?.POSP?.Is_Contact_Sync.toString()

                    withContext(Dispatchers.Main){

                        if (isContactSync.equals("1")) {
                            delay(200)

                            viewPager!!.currentItem = 2
                            viewPager.beginFakeDrag()
                            viewPager.visibility = View.VISIBLE
                            cancelAnimDialog()

                        }else{
                            viewPager.visibility = View.VISIBLE
                            cancelAnimDialog()
                        }

                    }
                }else{
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, resultResp.toString())
                        viewPager.visibility = View.VISIBLE
                        cancelAnimDialog()
                    }
                }


                // delay(8000)



            }else{

                withContext(Dispatchers.Main) {
                    Log.d(TAG, resultResp.toString())
                    viewPager.visibility = View.VISIBLE
                    cancelAnimDialog()
                }
               // cancelDialog()
            }

        }

    }


        //  viewpager change listener


    override fun onClick(view: View?) {

        when (view!!.getId()) {
            R.id.btn_next -> {
                current = current + 1
                if (current < layouts.size) {
                    // move to next screen
                    viewPager.currentItem = current
                } else {
                    startActivity(Intent(this, SyncContactActivity::class.java))
                }
            }

            R.id.btn_skip -> startActivity(Intent(this, SyncContactActivity::class.java))

            R.id.txtprivacy -> startActivity(
                Intent(this, CommonWebViewActivity::class.java)
                    .putExtra(
                        "URL",
                        "https://www.policyboss.com/privacy-policy-policyboss-pro-elite"
                    )
                    .putExtra("NAME", "" + "privacy-policy")
                    .putExtra("TITLE", "" + "privacy-policy")
            )
            R.id.txtterm -> startActivity(
                Intent(this, CommonWebViewActivity::class.java)
                    .putExtra("URL", "https://www.policyboss.com/terms-condition")
                    .putExtra("NAME", "" + "Terms & Conditions")
                    .putExtra("TITLE", "" + "Terms & Conditions")
            )
            R.id.chkagree -> if (btnchkagree!!.tag != "1") {
                if (btnchkagree!!.isChecked) {
                    btnNext.isEnabled = true
                    btnNext.alpha = 1f
                } else {
                    btnNext.isEnabled = false
                    btnNext.alpha = 0.4f
                }
            }
        }
    }

    fun showAnimDialog(msg: String){

        if(!dialogAnim.isShowing) {
            val dialogLoadingBinding = DialogLoadingBinding.inflate(layoutInflater)
            dialogAnim.setContentView(dialogLoadingBinding.root)
            Glide.with(this).load<Any>(R.drawable.loading_gif)
                .asGif()
                .crossFade()
                .into(dialogLoadingBinding.imgLoader)
            if (dialogAnim.window != null) {

                dialogAnim!!.window!!.setBackgroundDrawable(ColorDrawable(0))

            }

            dialogAnim.setCancelable(false)
            dialogAnim.show()
        }
    }

    fun cancelAnimDialog(){

        if(dialogAnim.isShowing){

            dialogAnim.dismiss()
        }
    }


    inner class MyViewPagerAdapter : PagerAdapter() {
            private var layoutInflater: LayoutInflater? = null
            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                layoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater?
                val view: View = layoutInflater!!.inflate(layouts.get(position), container, false)
                container.addView(view)
                return view
            }

            override fun getCount(): Int {
                return layouts.size
            }

            override fun isViewFromObject(view: View, obj: Any): Boolean {
                return view === obj
            }

            override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
                // super.destroyItem(container, position, obj)
                val view = obj as View
                container.removeView(view)
            }
        }


}
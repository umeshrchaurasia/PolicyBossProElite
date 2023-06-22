package com.utility.finmartcontact.home.Worker

import android.app.NotificationManager
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters

import com.google.gson.Gson
import com.policyboss.policybossproelite.utility.Constant

import com.utility.finmartcontact.core.model.ContactlistEntity
import com.utility.finmartcontact.core.requestentity.ContactLeadRequestEntity

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import magicfinmart.datacomp.com.finmartserviceapi.finmart.retrobuilder.RetroHelper

import com.policyboss.policybossproelite.syncContact.Worker.ContactHelper



/**
 * Created by Rahul on 10/06/2022.
 */
class ContactLogWorkManager(
   val context: Context, workerParameters: WorkerParameters,

    ) : CoroutineWorker(context, workerParameters) {

    private val TAG = "CALL_LOG_CONTACT"
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    override suspend fun doWork(): Result {
        return try {
            Log.d("CallLogWorker", "Run work manager")
            //Do Your task here

           var ContactCount = callContactTask()


            // Log.d("CallLogWorker", callLogList.toString())
            val outPutData: Data = Data.Builder()
                .putString(Constant.KEY_result, "${ContactCount}")
                .build()
            Result.success(outPutData)
        }
        catch (e: Exception) {
            Log.d(TAG, "exception in doWork1 ${e.message}")
            val errorData: Data = Data.Builder()
                .putString(Constant.KEY_error_result, "Data Not Uploaded.Please Try Again.")
                .build()
            Result.failure(errorData)

        }

    }

    private suspend fun callContactTask() : Int {

        var ContactCount = 0

        //region Comment : Not In Used
     //   var strbody = "Contact is Uploading Please wait.."
     //   var strResultbody = "Successfully Uploaded.."
      //  setForeground(createForegroundInfo(0, 0, strbody))

        //endregion


        // region getting Input Data
        val fbaid = inputData.getInt(Constant.KEY_fbaid, 0)
        val ssid = inputData.getString(Constant.KEY_ssid)
        val parentid = inputData.getString(Constant.KEY_parentid)
        val deviceID = inputData.getString(Constant.KEY_deviceid) ?: ""

        var tfbaid = ""
        var tsub_fba_id = ""
        var getAllContactDetails :  MutableList<ContactHelper.ModelContact> = mutableListOf()

        if (parentid.isNullOrEmpty() || parentid.equals("0")) {

            tfbaid = fbaid.toString()
            tsub_fba_id = parentid.toString()

        } else {
            tfbaid = parentid.toString()
            tsub_fba_id = fbaid.toString()
        }

        //endregion
      //  delay(2000)

        withContext(Dispatchers.IO) {

            var url =  "https://horizon.policyboss.com:5443/sync_contacts" + "/contact_entry"

            var contactlist = getContactList()

            ContactCount = contactlist.size


            var subcontactlist: List<ContactlistEntity>

            if (contactlist != null && contactlist!!.size > 0) {

                try{
                    getAllContactDetails =  ContactHelper.getContact(context.applicationContext)
                }catch (ex :Exception ){

                }



                for (i in 0..contactlist!!.size - 1 step 1000) {

                    Log.d(TAG, "CallLog Number of data jumped ${i}")

                    subcontactlist = contactlist!!.filter { it.id > i && it.id <= (1000 + i) }


                      // region calling to server


                    val contactRequestEntity = ContactLeadRequestEntity(
                        fbaid = tfbaid,
                        ssid = ssid!!,
                        sub_fba_id = tsub_fba_id,
                        contactlist = subcontactlist,
                        raw_data = Gson().toJson(getAllContactDetails),
                        device_id = deviceID
                    )


                    Log.d(Constant.TAG_SAVING_CONTACT_LOG,Gson().toJson(getAllContactDetails) )

                        val resultResp = RetroHelper.api.saveContactLead(url, contactRequestEntity)


                        if (resultResp.isSuccessful) {



                           // delay(8000)

                        }else{

                            Log.d(TAG, resultResp.toString())
                        }


                        //endregion




                }

            }

        }

        return ContactCount
    }
    private  fun getContactList(): MutableList<ContactlistEntity> {

        var contactlist: MutableList<ContactlistEntity> = ArrayList<ContactlistEntity>()
        var templist: MutableList<String> = ArrayList<String>()
        var phones: Cursor? = null


        val PROJECTION = arrayOf(
            ContactsContract.RawContacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Photo.CONTACT_ID,
             ContactsContract.Data.MIMETYPE,
            ContactsContract.Data.DATA1
        )

        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val filter =
            "" + ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0 and " + ContactsContract.CommonDataKinds.Phone.TYPE + "=" + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
        val order =
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"// LIMIT " + limit + " offset " + lastId + "";

        phones = applicationContext.contentResolver.query(uri, PROJECTION, filter, null, order)




        ///////////////////////////


        val regex = Regex("[^.0-9]")

        phones.let {

            if (phones != null && phones!!.getCount() > 0) {
                try {
                    var i = 1
                    while (phones.moveToNext()) {


                        var name =
                            "" + phones.getString(
                                phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME) ?: 0
                            )
                        var phoneNumber =
                            "" + phones.getString(
                                phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                    ?: 0
                            )

                        //  phoneNumber = phoneNumber.trim().replace("[^0-9\\s+]", "");   // remove Special character and Space

                        phoneNumber = regex.replace(phoneNumber, "") // works
                        //.replace("\\s".toRegex(), "")

                        if (phoneNumber.length >= 10) {

                            phoneNumber = phoneNumber.takeLast(10)
                            // check whether the number alreday added to list or not

                            if (!templist!!.contains(phoneNumber)) {
                                templist?.add(phoneNumber)

                                val selectUser = ContactlistEntity(
                                    name = name,
                                    mobileno = phoneNumber,
                                    id = i
                                )
                               // Log.i(TAG, "Key ID: " + i + " Name: " + name + " Mobile: " + phoneNumber + "\n");
                                contactlist.add(selectUser)

                            }


                        }


                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }



        return contactlist


    }




}
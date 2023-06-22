package com.policyboss.policybossproelite.syncContact.Worker

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract

object ContactHelper {

         val maxSize = 150
         val maxStandardSize = 100
        private val TAG = "CALL_LOG_CONTACT"

        @JvmStatic
        fun getContact(context: Context)  : MutableList<ModelContact> {

            var myData : MutableList<ModelContact> = mutableListOf()

            val regex = Regex("[^.0-9]")

            val contatcPhoneMapList: MutableMap<String, MutableList<String>> = mutableMapOf()

            val contatcEmailMapList: MutableMap<String, MutableList<String>> = mutableMapOf()

            val contatcAddressMapList: MutableMap<String, MutableList<String>> = mutableMapOf()

            val contatcOrganizationMapList : MutableMap<String, MutableList<String>> = mutableMapOf()

           // val DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME

            val ORDER =
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"// LIMIT " + limit + " offset " + lastId + "";
            val cursor = context.contentResolver.query(
                ContactsContract.Data.CONTENT_URI, arrayOf(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.Data.DISPLAY_NAME,
                    ContactsContract.Data.RAW_CONTACT_ID,
                    ContactsContract.Data.LOOKUP_KEY,
                    ContactsContract.Data.DATA1,
                    ContactsContract.Data.DATA2,
                    ContactsContract.Data.DATA3,
                    ContactsContract.Data.DATA4,
                    ContactsContract.Data.DATA5,
                    ContactsContract.Data.DATA6,
                    ContactsContract.Data.DATA7,
                    ContactsContract.Data.DATA8,
                    ContactsContract.Data.DATA10
                ),
//                (ContactsContract.Data.HAS_PHONE_NUMBER + ">0" + " AND "
//                        + "(" + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "')"),
//                null, ORDER

                (ContactsContract.Data.HAS_PHONE_NUMBER + ">0" + " AND "

                + "(" + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' OR "
                        + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE + "' OR "
                        + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE + "' OR "
                        + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE + "' OR "
                        + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "')"),
                null, ORDER

            )

            // val contactMaps  =  HashMap<String,ArrayList<Model?>>()


            if (cursor != null && cursor.count > 0) {

                while (cursor.moveToNext()) {
                    val displayName =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME))


                    val mimeType =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Data.MIMETYPE))
                    if ((mimeType == ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                        //arrayPhoneList.clear()
                        val phoneNo =
                            cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))


                        /********************** displayName work as KEY ************/
                        var mutableList  = contatcPhoneMapList.get(displayName) // using the key ie Display Name and get All Element of Phone

                        if(mutableList == null){


                            mutableList = ArrayList<String>()

                         }

                        mutableList.add(phoneNo.replace("\\s".toRegex(), ""))

                        contatcPhoneMapList.put(displayName,mutableList.toSet().toMutableList())

                    }
                    else if (mimeType == ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE) {
                       setEmailList(cursor = cursor, displayName = displayName , contatcEmailMapList = contatcEmailMapList)
                    }else if(mimeType == ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE){

                        setAddress(cursor = cursor, displayName = displayName , contatcAddressMapList = contatcAddressMapList)
                    }else if ((mimeType == ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)) {
                        setOrganization(cursor = cursor, displayName = displayName , contatcOrganizationMapList = contatcOrganizationMapList)
                    }

                    //contatcOrganizationMapList

                }
                var count = 0

//                val entriesEmail: List<String> = contatcEmailMapList.entries.map { "(${it.key}, ${it.value})" }
//                entriesEmail.forEach {
//                    // println(it)
 //                  Log.d(Constant.TAG_SAVING_CONTACT_LOG,it)
//                }



                // Add Phone Number in Main List
                var modeContact : ModelContact


                contatcPhoneMapList.forEach{

                      modeContact =  ModelContact(displayName = it.key.toString() )

                      modeContact.phoneNo = it.value

                    myData.add(modeContact)
                }


                myData.forEach{ myData ->

                    // Add Email
                    contatcEmailMapList.forEach{ email ->

                        if(myData.displayName.equals(email.key)){

                            myData.emiailId = email.value
                        }

                    }

                    // Add Address
                    contatcAddressMapList.forEach{ address ->

                        if(myData.displayName.equals(address.key)){

                            myData.address = address.value
                        }
                    }

                    // Add Organization
                    contatcOrganizationMapList.forEach{ organization ->

                        if(myData.displayName.equals(organization.key)){

                            myData.address = organization.value
                        }
                    }
                }

                // region commented below line for testing Purpose

               // val entries: List<String> = myData.entries.map { "(${it.key}, ${it.value})" }
//                myData.forEach {
//                    // println(it)
//                    Log.d("CALL_LOG_CONTACT",it.displayName +" " + it.phoneNo +" " + it.emiailId +" " + it.address  )
//                }


//                val entries: List<String> = contatcAddressMapList.entries.map { "(${it.key}, ${it.value})" }
//
//                entries.forEach {
//                    // println(it)
//                    Log.d(Constant.TAG_SAVING_CONTACT_LOG,it.toString()  )
//                }
                //endregion
                cursor.close()
            }

          return  myData
        }


        private fun setEmailList(cursor: Cursor, displayName : String, contatcEmailMapList: MutableMap<String, MutableList<String>> ) {

            //var contatcEmailMapList: MutableMap<String, MutableList<String>> = mutableMapOf()
            /********************** displayName work as KEY ************/
            var emailList  = contatcEmailMapList.get(displayName) // using the key ie Display Name and get All Element of Phone

            if(emailList == null){


                emailList = ArrayList<String>()

            }

            val emailAddress =
                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.ADDRESS))

            emailList.add(emailAddress.toString().replace("\\s".toRegex(), ""))


            contatcEmailMapList.put(displayName,emailList.toSet().toMutableList())




        }


        private fun setAddress(cursor: Cursor, displayName : String, contatcAddressMapList: MutableMap<String, MutableList<String>> ) {


            /********************** displayName work as KEY ************/
            var addressList  = contatcAddressMapList.get(displayName) // using the key ie Display Name and get All Element of Phone

            if(addressList == null){


                addressList = ArrayList<String>()

            }

            val address = Address()     // For Address  05 Added Below for test large data


            var street = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.STREET))

            street ?.let {

                if(it.count() > maxSize){

                    street = it.take(maxSize)
                }

                address.street = street
            }


             var city =
                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.CITY))

            city ?.let {

                if(it.count() > maxStandardSize){

                    city = it.take(maxStandardSize)
                }

                address.city = city
            }


            var region =
                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.CITY))

            region ?.let {

                if(it.count() > maxStandardSize){

                    region = it.take(maxStandardSize)
                }

                address.region = region
            }

            //region old Data 05 Commnted For Testing
//            address.street =
//                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.STREET))

//            address.city =
//                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.CITY))
//            address.region =
//                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.REGION))
//            address.country =
//                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY))
//            val addressType =
//                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.TYPE))
//            if (addressType != null && (addressType == ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM.toString())) {
//                address.label =
//                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.LABEL))
//            }

          //  addressList.add( "${address.city?: ""}${address.region ?: ""}${address.street ?: ""}  ")
       //endregion

            addressList.add( "${address.city ?: ""}${address.region ?: ""}${address.street ?: ""}  ")

            contatcAddressMapList.put(displayName,addressList)


        }


        private fun setOrganization(cursor: Cursor, displayName : String, contatcOrganizationMapList: MutableMap<String, MutableList<String>> ) {

            /********************** displayName work as KEY ************/
            var organizationList  = contatcOrganizationMapList.get(displayName) // using the key ie Display Name and get All Element of Phone

            if(organizationList == null){


                organizationList = ArrayList<String>()

            }

            var company =
                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Organization.COMPANY))
            var  jobTitle =
                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Organization.TITLE))
            var department =
                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Organization.DEPARTMENT))

            organizationList.add( "${jobTitle?: ""} ${company ?: ""} ${department ?: ""}  ")


            contatcOrganizationMapList.put(displayName,organizationList)

        }

        //*********************************************************//


        data class ModelContact(
            var displayName: String? = "",
        ){

            var phoneNo: MutableList<String> = ArrayList()
            var emiailId: MutableList<String> = ArrayList()
            var address: MutableList<String> = ArrayList()

        }

        class Address() {
            var street : String ? = ""
            var city : String ? = ""
            var region : String ? = ""
            var country  : String ? = ""
            var label: String? = ""

        }





}
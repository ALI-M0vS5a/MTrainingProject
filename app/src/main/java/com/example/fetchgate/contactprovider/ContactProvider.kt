package com.example.fetchgate.contactprovider

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import com.example.fetchgate.fragments.ContactsViewModel
import com.example.fetchgate.network.ContactDataTransfer


class ContactProvider(private val contentResolver: ContentResolver) {
    companion object {
        val fieldList = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        )
        private var CONTENT_URI: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        private var ORDER = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
    }

    @SuppressLint("Range")
    fun getAllContactsFromProvider(contactsViewModel: ContactsViewModel) {
        val cr: ContentResolver = contentResolver

        val c: Cursor? = cr.query(
            CONTENT_URI, fieldList,
            null, null, ORDER
        )
        var name: String
        var phone: String
        var contactId: String

        val normalizedNumbers: HashSet<String> = HashSet()
        if (c != null) {
            while (c.moveToNext()) {
                try {
                    phone =
                        c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER))

                    if (normalizedNumbers.add(phone)) {
                        name =
                            c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                        contactId =
                            c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                        val m = ContactDataTransfer(contactId.toInt(), name, phone)

                        contactsViewModel.addContacts(m)
                    }
                } catch (e: Exception) {
                    Log.d("NONuLL", e.message.toString())
                }
            }
            c.close()
        }
    }
}





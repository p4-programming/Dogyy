package com.bnb.chat

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.bnb.doggydoo.firebaseChat.ChatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class FirebaseService(chatActivity: ChatActivity) {
    private var context: Context? = null

    fun FirebaseService(context: ChatActivity) {
        this.context = context
    }

    fun uploadImageToFireBaseStorage(uri: Uri, onCallBack: OnCallBack) {
        val riversRef: StorageReference = FirebaseStorage.getInstance().getReference()
            .child("ImagesChats/" + System.currentTimeMillis() + "." + getFileExtention(uri))

        riversRef.putFile(uri).addOnSuccessListener {
            val result = it.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {

                val imageLink = it.toString()
                onCallBack.onUploadSuccess(imageLink)

            }
            result.addOnFailureListener(OnFailureListener { e -> onCallBack.onUploadFailed(e) })
        }
       /* riversRef.putFile(uri).addOnSuccessListener(OnSuccessListener<Any> { taskSnapshot ->
            val urlTask: Task<Uri> = taskSnapshot.getMetadata().getDownloadUrl();
            while (!urlTask.isSuccessful);
            val downloadUrl = urlTask.result
            val sdownload_url = downloadUrl.toString()
            onCallBack.onUploadSuccess(sdownload_url)
        }).addOnFailureListener(OnFailureListener { e -> onCallBack.onUploadFailed(e) })*/
    }

    private fun getFileExtention(uri: Uri): String? = when (uri.scheme) {
        // get file extension
        ContentResolver.SCHEME_FILE -> File(uri.path!!).extension
        // get actual name of file
        //ContentResolver.SCHEME_FILE -> File(uri.path!!).name
        ContentResolver.SCHEME_CONTENT -> getCursorContent(uri)
        else -> null
    }

    private fun getCursorContent(uri: Uri): String? = try {
        context!!.contentResolver.query(uri, null, null, null, null)?.let { cursor ->
            cursor.run {
                val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
                if (moveToFirst()) mimeTypeMap.getExtensionFromMimeType(context!!.contentResolver.getType(uri))
                // case for get actual name of file
                //if (moveToFirst()) getString(getColumnIndex(OpenableColumns.DISPLAY_NAME))
                else null
            }.also { cursor.close() }
        }
    } catch (e: Exception) {
        null
    }


    interface OnCallBack {
        fun onUploadSuccess(imageUrl: String?)
        fun onUploadFailed(e: Exception?)
    }
}
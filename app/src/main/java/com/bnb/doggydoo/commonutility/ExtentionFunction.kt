package com.bnb.doggydoo.commonutility

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RawRes
import androidx.annotation.RequiresApi
import com.bnb.doggydoo.R
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun ImageView.loadImageAsGif(context: Context, @RawRes resourceId: Int) {
    Glide.with(context).asGif().load(resourceId).centerCrop()
        .into(this)
}

fun ImageView.loadImageFromString(context: Context, imagePng: String) {
    Glide.with(context).load(imagePng).placeholder(R.drawable.default_image).centerCrop()
        .into(this)
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.convertTimeIn12HrFormat(): String =
    LocalTime.parse(this).format(DateTimeFormatter.ofPattern("h:mma"))

@SuppressLint("SimpleDateFormat")
fun String.convertTimeIn24HrFormat(): String {
    val date = SimpleDateFormat("hh:mm a").parse(this)
    val displayFormat = SimpleDateFormat("HH:mm")
    return displayFormat.format(date)
}

/**
 * Add th,rd,st in date
 */
fun Int.ordinalNumber(): String {
    if (this in 11..13) {
        return "${this}th"
    }
    return when (this % 10) {
        1 -> "${this}st"
        2 -> "${this}nd"
        3 -> "${this}rd"
        else -> "${this}th"
    }
}

fun String.snack(color: Int, view: View) {
    Snackbar.make(view, this, Snackbar.LENGTH_LONG).apply {
        setBackgroundTint(color)
        setTextColor(Color.WHITE)
        show()
    }
}

fun String.showToast(context: Context) {
    Toast.makeText(context,this,Toast.LENGTH_LONG).show()
}

fun Bitmap.convertBitmapToBase64(): String {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    val byteArray = stream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.inVisible() {
    this.visibility = View.INVISIBLE
}
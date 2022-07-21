package com.bnb.doggydoo.commonutility

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar


class CommonFunctions {
    companion object {
        fun showSnackBar(msg: String, view: View) {
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
        }

        fun hideKeyBoard(view: View, context: Context) {
            val inputMethodManager: InputMethodManager =
                context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

        }
    }
}
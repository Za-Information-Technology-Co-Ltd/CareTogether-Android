package com.za.caretogether.utils

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.provider.Settings
import android.view.View
import android.widget.EditText
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.za.caretogether.R
import org.jetbrains.anko.*

fun String.toMMNum() = this.fold("") { acc, c ->
    if (c in '0'..'9') {
        // acc + ((c.toInt() - '0'.toInt()) + '၀'.toInt())
        acc + (c.toInt() + 4112).toChar()
    } else {
        acc + c
    }
}

fun String.toEString() = this.fold("") { acc, c ->
    if (c in '0'..'9') {
        // acc + ((c.toInt() - '0'.toInt()) + '၀'.toInt())
        acc + (c.toInt() + 64).toChar()
    } else {
        acc + c
    }
}

fun Int.toMMNum() = this.toString().fold("") { acc, c ->
    if (c in '0'..'9') {
        // acc + ((c.toInt() - '0'.toInt()) + '၀'.toInt())
        acc + (c.toInt() + 4112).toChar()
    } else {
        acc + c
    }
}

fun EditText.isValid(): Boolean {
    return !this.text.toString().isNullOrEmpty()
}

fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun Context.availableConnection(view: View? = null, yes: () -> Unit) {
    if (!this.isInternetAvailable()) {
        if (view == null) {
            this.toast("No Internet Connection")
        } else {
            if (view is SwipeRefreshLayout) {
                view.isRefreshing = false
            }
            Snackbar.make(view,"No Internet Connection", Snackbar.LENGTH_SHORT).show()
        }
    } else {
        yes()
    }
}

fun Context.progressdialog(title: String): AlertBuilder<DialogInterface> {

    return alert {
        customView {
            linearLayout {
                padding = dip(30)
                progressBar { }

                textView {
                    padding = dip(10)
                    textSize = 20f
                    text = title
                }

            }
        }
    }
}

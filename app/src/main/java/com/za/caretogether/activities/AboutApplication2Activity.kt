package com.za.caretogether.activities

import android.annotation.SuppressLint
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.za.caretogether.BuildConfig
import com.za.caretogether.R
import com.za.caretogether.utils.Storage
import com.za.caretogether.utils.ViewUtil
import com.za.caretogether.utils.isInternetAvailable
import com.za.caretogether.utils.progressdialog
import kotlinx.android.synthetic.main.activity_about_application2.*
import java.util.*

class AboutApplication2Activity : AppCompatActivity() {
    val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_application2)

        back.setOnClickListener {
            finish()
        }

        if(isInternetAvailable()){

            require_about_internet.visibility = View.GONE
            webview_about_situation.visibility = View.VISIBLE
            initWebView()
            setWebClient()
            handlePullToRefresh()
            var locale = Storage.make(this).locale
            loadUrl("https://ct.zacompany.dev/webview/information/about"+"?language="+locale+"&v="+ BuildConfig.VERSION_NAME)


        }else {

            require_about_internet.visibility = View.VISIBLE
            webview_about_situation.visibility = View.GONE

        }

        webview_about_situation.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && webview_about_situation.canGoBack()) {
                    webview_about_situation.goBack()
                    return@OnKeyListener true
                }
            }
            false
        })
    }

    private fun handlePullToRefresh() {
    }



    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webview_about_situation.settings.javaScriptEnabled = true
        webview_about_situation.settings.loadWithOverviewMode = true
        webview_about_situation.settings.useWideViewPort = true
        webview_about_situation.settings.domStorageEnabled = true
        webview_about_situation.webViewClient = object : WebViewClient() {
            override
            fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }
        }
    }

    private fun setWebClient() {
        val progressDialog = progressdialog(ViewUtil.getString(this,resources.getString(R.string.waiting))).show()

        webview_about_situation.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(
                view: WebView,
                newProgress: Int
            ) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    progressDialog.dismiss()
                }
            }
        }
    }

    private fun loadUrl(pageUrl: String) {
        webview_about_situation.loadUrl(pageUrl)
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
    }
}

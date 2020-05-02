package com.za.caretogether.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.za.caretogether.BuildConfig
import com.za.caretogether.MainActivity
import com.za.caretogether.R
import com.za.caretogether.response.DataByHospital
import com.za.caretogether.response.DataByRegion
import com.za.caretogether.response.GlobalData
import com.za.caretogether.utils.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_emergency2.*
import kotlinx.android.synthetic.main.fragment_situation2.*
import kotlinx.android.synthetic.main.fragment_situation2.loading_indicator
import kotlinx.android.synthetic.main.fragment_situation2.require_internet
import java.util.*


class Situation2Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var disposable = CompositeDisposable()
    var hospitallist = mutableListOf<DataByHospital>()
    var regionList = mutableListOf<DataByRegion>()
    var globallist = mutableListOf<GlobalData>()
    var asianlist = mutableListOf<GlobalData>()
    val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_situation2, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var local = Storage.make(context).locale
        var lat = Storage.make(context).lat
        var long = Storage.make(context).long

        val config =
            context!!.resources.configuration
        val locale = Locale(local)
        Locale.setDefault(locale)
        config.locale = locale
        context!!.resources.updateConfiguration(
            config,
            context!!.resources.displayMetrics
        )

        if(context!!.isInternetAvailable()){

            require_internet.visibility = View.GONE
            webview_situation.visibility = View.VISIBLE
            initWebView()
            setWebClient()
            handlePullToRefresh()
            var locale = Storage.make(context).locale
            loadUrl("https://ct.zacompany.dev/webview/myanmar"+"?language="+locale+"&v="+ BuildConfig.VERSION_NAME+"&p="+lat.toMMNum()+":"+long.toMMNum())
            Log.i("Url: ", "https://ct.zacompany.dev/webview/myanmar"+"?language="+locale+"&v="+ BuildConfig.VERSION_NAME+"&p="+lat.toMMNum()+":"+long.toMMNum())

        }else {

            require_internet.visibility = View.VISIBLE
            webview_situation.visibility = View.GONE

        }



        webview_situation.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && webview_situation.canGoBack()) {
                    webview_situation.goBack()
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
        webview_situation.settings.javaScriptEnabled = true
        webview_situation.settings.loadWithOverviewMode = true
        webview_situation.settings.useWideViewPort = true
        webview_situation.settings.domStorageEnabled = true
        webview_situation.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                url: String?
            ): Boolean {
                if (url.toString().startsWith("tel:")) {

                    if (ContextCompat.checkSelfPermission(
                            context!!,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            context!! as Activity,
                            arrayOf(
                                Manifest.permission.CALL_PHONE
                            ),
                            100
                        )
                    } else {

                        var intent = Intent(
                            Intent.ACTION_CALL,
                            Uri.parse(url.toString())
                        )

                        startActivity(intent)
                    }

                    return true

                }
                else  if (!url.toString().startsWith("https:ct.zacompany.dev")) {

                    var intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(url)
                    )

                    startActivity(intent)

                    return true

                }
                else return false
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (request!!.url.toString().startsWith("tel:")) {

                    if (ContextCompat.checkSelfPermission(
                            context!!,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            context!! as Activity,
                            arrayOf(
                                Manifest.permission.CALL_PHONE
                            ),
                            100
                        )
                    } else {

                        var intent = Intent(
                            Intent.ACTION_CALL,
                            Uri.parse(request!!.url.toString())
                        )

                        startActivity(intent)
                    }

                    return true

                }
                else  if (!request!!.url.toString().startsWith("https://ct.zacompany.dev")) {

                    var intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(request!!.url.toString())
                    )

                    startActivity(intent)

                    return true

                }
                else return false
            }
            override
            fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }
        }
    }
    private fun setWebClient() {

        activity!!.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        webview_situation.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(
                view: WebView,
                newProgress: Int
            ) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    loading_indicator.visibility = View.GONE
                    activity!!.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            }
        }
    }

    private fun loadUrl(pageUrl: String) {
        webview_situation.loadUrl(pageUrl)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Situation2Fragment().apply {

            }
    }

}

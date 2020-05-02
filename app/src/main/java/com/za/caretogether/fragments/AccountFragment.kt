package com.za.caretogether.fragments

import android.app.Activity
import android.app.DownloadManager
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.za.caretogether.BuildConfig
import com.za.caretogether.MainActivity
import com.za.caretogether.R
import com.za.caretogether.activities.*
import com.za.caretogether.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.app_update_dialog_layout.view.*
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.usage_example.view.*
import org.jetbrains.anko.support.v4.runOnUiThread
import java.io.File
import java.util.*

class AccountFragment : Fragment() {

    var disposable = CompositeDisposable()
    private val USER_PREF = "user_pref_file"
    var REQUEST_PHONE_CALL = 1
    lateinit var mProgressDialog: ProgressDialog
    var download_link : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    external fun getSomething(): String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var local = Storage.make(context).locale
        var prefixName = String.format("ic_%s", local)
        var resourceId = context!!.resources.getIdentifier(prefixName, "drawable", context!!.getPackageName())
        img_locale.setImageResource(resourceId)

        val config =
            context!!.resources.configuration
        val locale = Locale(local)
        Locale.setDefault(locale)
        config.locale = locale
        context!!.resources.updateConfiguration(
            config,
            context!!.resources.displayMetrics
        )

        val preferences = context!!.getSharedPreferences(
            USER_PREF,
            Context.MODE_PRIVATE
        )
        val liveSharedPreferences = LiveSharedPreferences(preferences)

        liveSharedPreferences.getInt("count", 0).observe(this, Observer<Int> { value ->
            Log.d("TAG", value.toString())
            if(value == 0){
                contact_count.text = resources.getString(R.string.no_contact_with_user)

            }else{

                if(Storage.make(context).installDate.isNullOrBlank()){
                    if(local == "en")
                        contact_count.text = "  -  " + resources.getString(R.string.touched_people) + " ( " + Storage.make(context).count.toString()+ " ) "+ resources.getString(R.string.touched_people2)
                    else contact_count.text = "  -  " + resources.getString(R.string.touched_people) + " ( " + Storage.make(context).count.toString().toMMNum()+ " ) "+ resources.getString(R.string.touched_people2)
                }else {
                    if(local == "en")
                        contact_count.text = "Starting from "+ Storage.make(context).installDate + "  "+ resources.getString(R.string.touched_people) + " ( " + Storage.make(context).count.toString()+ " ) "+ resources.getString(R.string.touched_people2)
                    else if(local == "sn")  contact_count.text = "တႄႇၵႃႈတီႈ "+ Storage.make(context).installDate + "  "+ resources.getString(R.string.touched_people) + " ( " + Storage.make(context).count.toString()+ " ) "+ resources.getString(R.string.touched_people2)
                        else contact_count.text = Storage.make(context).installDate + "  "+ resources.getString(R.string.touched_people) + " ( " + Storage.make(context).count.toString().toMMNum()+ " ) "+ resources.getString(R.string.touched_people2)
                }

            }
        })

        ll_update.visibility = View.GONE
        ll_permission.visibility = View.GONE
        checkUpdate()



        var phone = Storage.make(context).phone
        if(phone.startsWith("09")){
            var replace_phone = phone.replaceRange(4,8,"XXXX")
            Storage.make(context).phone = ChCrypto.tryMeHack(replace_phone,getSomething())
            user_phone.text = replace_phone
        }else {
            phone = ChCrypto.tryMeHackMore(phone, getSomething())
            user_phone.text = phone
        }


        if(context!!.isInternetAvailable()){
            img_connection.setImageResource(R.drawable.ic_wifi_connect)

        }else {
            img_connection.setImageResource(R.drawable.ic_wifi_not_connect)
        }

        var bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val permissionUtils = MarshMallowPermissionUtils(activity)

        if (bluetoothAdapter?.isEnabled == false||!isLocationEnabled()) {

            ll_permission.visibility = View.VISIBLE
            turn_permission_on.text = resources.getString(R.string.permission_on)
            ll_permission.setOnClickListener {

                if (!isLocationEnabled()) {
                    Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(intent, 102)
                } else {

                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBtIntent, 101)
                }

            }

        }

        getSetting()

        about_app.setOnClickListener {

            startActivity(Intent(context, AboutApplication2Activity::class.java))
        }

        img_locale.setOnClickListener {
            startActivity(Intent(context, LocaleActivity::class.java))
            activity!!.finish()
        }

        img_unlink.setOnClickListener {
            startActivity(Intent(context, ClearCacheActivity::class.java))
            activity!!.finish()
        }
        info_policy.setOnClickListener {
            startActivity(Intent(context, InfoPolicy2Activity::class.java))
        }


        ll_caretogether_suspect.setOnClickListener {
         //   bottom_navigation_view.selectedItemId = R.id.nav_call
            var intent = Intent(activity,MainActivity::class.java)
            intent.putExtra("type",2)
            startActivity(intent)
            activity!!.finish()
        }
        count_caretogether.setOnClickListener {

            UsageExample()
        }

        user_phone.setOnClickListener {
            startActivity(Intent(context,QRActivity::class.java))
        }

    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    companion object {

        init {
            System.loadLibrary("native-lib")
        }
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountFragment().apply {

            }
    }

    private fun checkUpdate() {

        context!!.availableConnection {

            val versionCode = BuildConfig.VERSION_CODE
            disposable.add(
                NetworkService.getInstance(context!!).apiService.checkUpdate(versionCode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {

                        // progressDialog.dismiss()
                    }
                    .doOnComplete {

                        //  progressDialog.dismiss()

                    }
                    .subscribe({

                        if (it.code() == 200) {

                            if (it.body()!!.need_update) {

                                download_link = it.body()!!.url
                                var play_store = it.body()!!.playstore
                                mProgressDialog = ProgressDialog(context)

                                ll_update.visibility = View.VISIBLE
                                update_available.text = resources.getString(R.string.update_available)+ " " + it.body()!!.version_code+ " "+resources.getString(R.string.update_available_2)
                                ll_update.setOnClickListener {

                                    val mDialogView =
                                        LayoutInflater.from(context!!)
                                            .inflate(R.layout.app_update_dialog_layout, null)
                                    val mBuilder = AlertDialog.Builder(context!!)
                                        .setView(mDialogView)
                                        .setCancelable(true)
                                    val mAlertDialog = mBuilder.show()
                                    if (play_store) {
                                        mDialogView.app_update.visibility = View.VISIBLE
                                    } else {
                                        mDialogView.app_update.visibility = View.GONE
                                    }
                                    mDialogView.app_download.setOnClickListener {
                                        //dismiss dialog
                                        // downloadFile()
                                        var fileName = "ct" + System.currentTimeMillis() + ".apk"

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                            if (!context!!.packageManager.canRequestPackageInstalls()) {

                                                try{
                                                    startActivityForResult(
                                                        Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).setData(
                                                            Uri.parse(String.format("package:%s", context!!.packageName))
                                                        ), 10
                                                    )
                                                }
                                                catch (e : Exception)
                                                {
                                                    Log.i("Error", e.toString())
                                                }
                                            } else {
                                                downloadNewVersion(download_link!!, fileName)
                                            }

                                        } else {
                                            downloadNewVersion(download_link!!, fileName)
                                        }

                                        mProgressDialog.setMessage("Updating to New Version")
                                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                                        mProgressDialog.setCancelable(true)
                                        mProgressDialog.show()

                                        mAlertDialog.dismiss()

                                    }
                                    mDialogView.app_update.setOnClickListener {
                                        //dismiss dialog
                                        val appPackageName =
                                            context!!.packageName // getPackageName() from Context or Activity object
                                        try {
                                            startActivity(
                                                Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse("market://details?id=" + appPackageName)
                                                )
                                            )
                                        } catch (anfe: android.content.ActivityNotFoundException) {
                                            startActivity(
                                                Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)
                                                )
                                            )
                                        }

                                        mAlertDialog.dismiss()

                                    }
                                }


                            }else{
                                ll_update.visibility = View.GONE
                            }
                        }
                    }, {
                        // Log.d("Denied", it.localizedMessage)
                    })
            )
        }

    }

    private fun downloadNewVersion(url: String, fileName: String) {

        var destination =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/"
        destination += fileName
        val uri = Uri.parse("file://$destination")
        Log.e("newversion", uri.toString())

        //Delete update file if exists
        val file = File(destination)

        val request = DownloadManager.Request(Uri.parse(url))
        request.setDescription("Downloading update ..")

        //set destination
        request.setDestinationUri(uri)

        // get download service and enqueue file
        val manager =
            context!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = manager.enqueue(request)

        Thread(Runnable {
            var downloading = true
            while (downloading) {
                val q = DownloadManager.Query()
                q.setFilterById(downloadId)
                val cursor = manager.query(q)
                cursor.moveToFirst()
                val bytes_downloaded = cursor.getInt(
                    cursor
                        .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                )
                val bytes_total =
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) === DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val dl_progress = ((bytes_downloaded * 100L) / bytes_total).toInt()
                runOnUiThread { mProgressDialog.progress = dl_progress.toInt() }
                cursor.close()
            }
        }).start()

        //set BroadcastReceiver to install app when .apk is downloaded
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(ctxt: Context, intent: Intent) {

                mProgressDialog.dismiss()
                var install = Intent(Intent.ACTION_VIEW)

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    install = Intent(Intent.ACTION_INSTALL_PACKAGE)

                    val apkURI = FileProvider.getUriForFile(
                        context!!,
                        context!!
                            .packageName + ".provider", file
                    )

                    Log.d("Uri", (apkURI).toString())

                    install.setDataAndType(apkURI, "application/vnd.android.package-archive")
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } else {
                    install.setDataAndType(uri, "application/vnd.android.package-archive")
                }
                install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(install)
                context!!.unregisterReceiver(this)
                activity!!.finish()
            }
        }
        context!!.registerReceiver(
            onComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }


    private fun getSetting(){

        context!!.availableConnection {

            disposable.add(
                NetworkService.getInstance(context!!).apiService.getSetting()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {

                        // progressDialog.dismiss()
                    }
                    .doOnComplete {

                        //  progressDialog.dismiss()

                    }
                    .subscribe({

                        if (it.code() == 200){

                        //    user_phone.isEnabled = it.body()!!.ministryOn

                            if(it.body()!!.ministryOn){

                                card_warning.visibility = View.GONE
//                                user_phone.setOnClickListener {
//                                    startActivity(Intent(context,InfoPhoneActivity::class.java))
//                                }

                                if(context!!.isInternetAvailable()){
                                    img_connection.setImageResource(R.drawable.ic_wifi_connect)
                                    require_internet.visibility = View.GONE
                                    getCountForMe()

                                }else {
                                    img_connection.setImageResource(R.drawable.ic_wifi_not_connect)
                                    require_internet.visibility = View.VISIBLE
                                    count_caretogether2.visibility = View.GONE
                                    ll_caretogether_suspect.visibility = View.GONE
                                }
                            }

                        }


                    }, {

                        // Log.d("Denied", it.localizedMessage)

                    })
            )
        }

    }

    private fun getCountForMe(){

        context!!.availableConnection {

            val progressDialog = context!!.progressdialog("").show()

            disposable.add(
                NetworkService.getInstance(context!!).apiService.getCountForMe()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {

                        // progressDialog.dismiss()
                    }
                    .doOnComplete {

                        //  progressDialog.dismiss()

                    }
                    .subscribe({
                        progressDialog.dismiss()
                        if (it.code() == 200){

                            if(it.body()!!.count == 0){
                                count_caretogether2.visibility = View.VISIBLE

                                ll_caretogether_suspect.visibility = View.GONE
                            }else {
                                count_caretogether2.visibility = View.GONE
                                ll_caretogether_suspect.visibility = View.VISIBLE
                                tv_count_suspect.text = resources.getString(R.string.identified_caretogether)+ "( " +
                                        it.body()!!.count.toString().toMMNum()+ " ) " + resources.getString(R.string.identified_caretogether2)
                            }

                        }


                    }, {

                        progressDialog.dismiss()
                       // Log.d("Denied", it.localizedMessage)

                    })
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {

            var fileName = "ct" + System.currentTimeMillis() + ".apk"
            downloadNewVersion(download_link!!, fileName)

        }

        if (bluetoothAdapter?.isEnabled == false||!isLocationEnabled()) {

            ll_permission.visibility = View.VISIBLE
            turn_permission_on.text = resources.getString(R.string.permission_on)
            ll_permission.setOnClickListener {

                if (!isLocationEnabled()) {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(intent, 102)
                } else {

                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBtIntent, 101)
                }

            }

        }else {
            ll_permission.visibility = View.GONE
        }
    }


    fun UsageExample(){

        val mDialogView = LayoutInflater.from(context).inflate(R.layout.usage_example, null)
        val mBuilder = AlertDialog.Builder(context!!)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        mDialogView.close.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

}

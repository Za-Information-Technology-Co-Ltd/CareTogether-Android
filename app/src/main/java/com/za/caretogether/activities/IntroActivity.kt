package com.za.caretogether.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.za.caretogether.MainActivity
import com.za.caretogether.R
import com.za.caretogether.model.FirebaseModel
import com.za.caretogether.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_intro.*
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*


class IntroActivity : AppCompatActivity() {

    var disposable = CompositeDisposable()

    companion object {

        init {
            System.loadLibrary("native-lib")
        }
    }

    external fun getSomething(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config =
            baseContext.resources.configuration
        val locale = Locale("mm")
        Locale.setDefault(locale)
        config.locale = locale
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
        setContentView(R.layout.activity_intro)

        if (Storage.make(this).isFirstTime) {
            val intent = Intent(this, FontDetectorActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
            return
        }else if (!Storage.make(this).phone.isNullOrBlank()&& !Storage.make(this).userPhone.isNullOrBlank()) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("type", 0)
            startActivity(intent)
            finish()
            return
        }
        val permissionUtils = MarshMallowPermissionUtils(this)

        if (!permissionUtils.checkPermissionForLocation()) {
            startActivity(Intent(this, LocationPermissionActivity::class.java))
            finish()
        } else if (!permissionUtils.checkPermissionForExternalStorage()) {
            startActivity(Intent(this, FilePermissionActivity::class.java))
            finish()
        }  else {

            if (!this.isInternetAvailable()) {

                care_logo.setImageResource(R.mipmap.ic_launcher)
                care_station.text = "Care Together"
                startActivity(Intent(Settings.ACTION_DATA_ROAMING_SETTINGS))
                this.toast("အင်တာနက် ချိတ်ဆက်ပါ")

            } else {
                getSetting()
                FirebaseMessaging.getInstance().subscribeToTopic("careTogether")
                    .addOnCompleteListener { task ->
                        var msg = "Topic Success"
                        if (!task.isSuccessful) {
                            msg = "Topic Fail"
                        }
                        Log.d("Firebase", msg)
                    }
            }
        }

        btn_participate.setOnClickListener {

            Log.i("Country Code " ,ccpone.selectedCountryCode.toString())
            if (profile_edit_phone_one.text.toString().length > 5) {

//                if (ccpone.selectedCountryCode == "95" && checkPhoneNumber(profile_edit_phone_one.text.toString()).equals("Not_Valid")) {
//                    profile_edit_phone_one.error = "ဖုန်းနံပါတ် မှားယွင်းနေပါသည်"
//                } else {

                    if (!this.isInternetAvailable()) {

                        startActivity(Intent(Settings.ACTION_DATA_ROAMING_SETTINGS))
                        this.toast("အင်တာနက် ချိတ်ဆက်ပါ")

                    } else {
                        profile_edit_phone_one.error = null
                        val sdf =
                            SimpleDateFormat("dd-MM-yyyy", Locale.US)
                        val currentDateandTime = sdf.format(Date())

                        Storage.make(this).installDate = currentDateandTime

                        //   Storage.make(this).setPhone(profile_edit_phone_one.text.toString())
                        FirebaseInstanceId.getInstance().instanceId
                            .addOnCompleteListener(OnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    Log.w("TAG", "getInstanceId failed", task.exception)
                                    saveFirebaseID("not_available_token", profile_edit_phone_one.text.toString())
                                    return@OnCompleteListener
                                }
                                // Get new Instance ID token
                                val token = task.result?.token

                                saveFirebaseID(token!!, profile_edit_phone_one.text.toString())

                                // Log and toast
                                val msg = getString(R.string.msg_token_fmt, token)
                                Log.d("TAG", msg)
                                // Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                            })
                 //   }

                }
            } else {
                profile_edit_phone_one.error = "ဖုန်းနံပါတ် မှားယွင်းနေပါသည်"
            }

        }


    }

    private fun getSetting() {

        availableConnection {

            disposable.add(
                NetworkService.getInstance(this).apiService.getSetting()
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

                            if (it.body()!!.ministryOn) {

                                care_logo.setImageResource(R.mipmap.logo)
                                care_station.text = "လူမှုဝန်ထမ်း၊ကယ်ဆယ်ရေးနှင့်\n" +
                                        "ပြန်လည်နေရာချထားရေးဝန်ကြီးဌာန"
                                care_station.textSize = 16F

                            } else {
                                care_logo.setImageResource(R.mipmap.ic_launcher)
                                care_station.text = "Care Together"
                                care_station.textSize = 20F
                            }

                        }


                    }, {

                        // Log.d("Denied", it.localizedMessage)

                    })
            )
        }

    }


    private fun saveFirebaseID(token: String, phone: String) {

        val progressDialog = progressdialog(ViewUtil.getString(this, resources.getString(R.string.waiting))).show()

        var firebaseModel = FirebaseModel(phone, token)
        disposable.add(
            NetworkService.getInstance(this).apiService.saveFireBaseID(firebaseModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {

                    progressDialog.dismiss()
                }
                .doOnComplete {

                    progressDialog.dismiss()

                }
                .subscribe({
                    if (it.code() == 200) {

                        progressDialog.dismiss()
                        var replace_phone = phone.replaceRange(4, 8, "XXXX")
                        Storage.make(this).phone =
                            ChCrypto.tryMeHack(replace_phone, getSomething())
                        Storage.make(this).userPhone = it.body()!!.user_phone

                        val intent = Intent(
                            this,
                            MainActivity::class.java
                        )
                        intent.putExtra("type", 0)
                        startActivity(intent)
                        finish()
                    }

                }, {

                    progressDialog.dismiss()
                    Log.d("Denied", it.localizedMessage)

                })
        )

    }

    fun checkPhoneNumber(phone: String): String {

        var operator = ""
        val ooredoo = Regex("(09|\\+?959)9(5|6|7|6)\\d{7}")
        val telenor = Regex("(09|\\+?959)7([5-9])\\d{7}")
        val mytel = Regex("(09|\\+?959)6(6|7|8|9)\\d{7}")
        val mec = Regex("(09|\\+?959)3(0-9)\\d{6,7}")
        val mpt =
            Regex("(09|\\+?959)(5\\d{6}|4\\d{7,8}|2\\d{6,8}|3\\d{7,8}|6\\d{6}|8\\d{6,8}|7\\d{7}|9(0|1|9)\\d{5,6}|2[0-4]\\d{5}|5[0-6]\\d{5}|8[13-7]\\d{5}|3[0-369]\\d{6}|34\\d{7}|4[1379]\\d{6}|73\\d{6}|91\\d{6}|25\\d{7}|26[0-5]\\d{6}|40[0-4]\\d{6}|42\\d{7}|45\\d{7}|89[6789]\\d{6}|)")

        if (ooredoo.matches(phone)) {
            operator = "Ooredoo"
            Log.i("Data", "Ooredoo")
        } else if (telenor.matches(phone)) {
            operator = "Telenor"
            Log.i("Data", "Telenor")
        } else if (mytel.matches(phone)) {
            operator = "Mytel"
            Log.i("Data", "Mytel")
        } else if (mec.matches(phone)) {
            operator = "MEC"
            Log.i("Data", "MEC")
        } else if (mpt.matches(phone)) {
            operator = "MPT"
            Log.i("Data", "MPT")
        } else {
            operator = "Not_Valid"
            Log.i("Data", "No Matching " + phone)
        }

        return operator
    }

}

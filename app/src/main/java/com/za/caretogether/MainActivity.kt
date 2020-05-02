package com.za.caretogether

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.za.caretogether.activities.LocationPermissionActivity
import com.za.caretogether.fragments.*
import com.za.caretogether.utils.MarshMallowPermissionUtils
import com.za.caretogether.utils.Storage
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import org.rabbitconverter.rabbit.Rabbit
import java.util.*

class MainActivity : AppCompatActivity() {
    private var mCurrentItem: Int = 0
    var disposable = CompositeDisposable()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var local = Storage.make(this).locale
        if(local == "rakhine"){
            local = "mr"
        }
        if(local == "shan"){
            local = "ms"
        }
        val config =
            baseContext.resources.configuration
        val locale = Locale(local)
        Locale.setDefault(locale)
        config.locale = locale
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
        setContentView(R.layout.activity_main)

        var type = intent.getIntExtra("type", 0)

        if (type == 0) {
            mCurrentItem = R.id.nav_account
            replaceFragment(AccountFragment())
        }else if(type == 2 ){
            mCurrentItem = R.id.nav_call
            replaceFragment(Emergency2Fragment())
            bottom_navigation_view.selectedItemId = mCurrentItem

        }else {
            mCurrentItem = R.id.nav_situation
            replaceFragment(Situation2Fragment())
            bottom_navigation_view.selectedItemId = mCurrentItem
        }

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("TAG", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.result?.token
                // Log and toast
                val msg = getString(R.string.msg_token_fmt, token)
                Log.d("TAG", msg)
            })

        FirebaseMessaging.getInstance().subscribeToTopic("careTogether")
            .addOnCompleteListener { task ->
                var msg = "Topic Success"
                if (!task.isSuccessful) {
                    msg = "Topic Fail"
                }
                Log.d("Firebase", msg)
            }

        val permissionUtils = MarshMallowPermissionUtils(this)

        if (!permissionUtils.checkPermissionForLocation()) {
            startActivity(Intent(this, LocationPermissionActivity::class.java))
            finish()
        }else {
            val i = Intent(this, BluetoothService::class.java)
            i.putExtra("KEY1", "Value to be used by the service")
            ContextCompat.startForegroundService(this, i)
        }
        bottom_navigation_view.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {

                if (mCurrentItem == item.itemId) {
                    return false
                }
                when (item.itemId) {
                    R.id.nav_account -> {
                        replaceFragment(AccountFragment())
                    }
                    R.id.nav_noti -> {
                        replaceFragment(Noti2Fragment())
                    }
                    R.id.nav_avoid -> {
                        replaceFragment(ToAvoid2Fragment())
                    }
                    R.id.nav_situation -> {
                        replaceFragment(Situation2Fragment())
                    }
                    R.id.nav_call -> {
                        replaceFragment(Emergency2Fragment())
                    }
                }
                mCurrentItem = item.itemId
                return true
            }
        })

        if (Storage.make(this).isZawgyi) {
            val menu = bottom_navigation_view.menu
            val total = menu.size()

            // For group title
            for (i in 0 until total) {
                val item = menu.getItem(i)
                var title = item.title.toString()
                title = Rabbit.uni2zg(title)
                item.title = title
            }
        }

        // val ONE_DAY_INTERVAL = 24 * 60 * 60 * 1000L
        // FileService.schedule(this,ONE_DAY_INTERVAL)

//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//
//        if (bluetoothAdapter?.isEnabled == false) {
//            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            startActivityForResult(enableBtIntent, 101)
//        }

    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_container, fragment)
        fragmentTransaction.commit()
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}

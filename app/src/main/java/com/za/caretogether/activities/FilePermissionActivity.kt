package com.za.caretogether.activities

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.za.caretogether.R
import com.za.caretogether.activities.IntroActivity
import com.za.caretogether.utils.MarshMallowPermissionUtils.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
import kotlinx.android.synthetic.main.activity_file_permission.*

class FilePermissionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_permission)

        btnPermissionOn.setOnClickListener {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                Toast.makeText(
                    this,
                    "External Storage permission needed. Please allow in App Settings for additional functionality.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
                )
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        var intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }
}

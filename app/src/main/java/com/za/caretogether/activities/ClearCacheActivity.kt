package com.za.caretogether.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.za.caretogether.R
import kotlinx.android.synthetic.main.activity_clear_cache.*
import kotlinx.android.synthetic.main.clear_cache_dialog.view.*
import java.io.File

class ClearCacheActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clear_cache)

        analyseStorage(this)
        btn_delete.setOnClickListener {
            confirmDialog()
        }
    }

    fun confirmDialog(){

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.clear_cache_dialog, null)
        val mBuilder = AlertDialog.Builder(this!!)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        mDialogView.btn_no.setOnClickListener {
            mAlertDialog.dismiss()
        }

        mDialogView.btn_yes.setOnClickListener {
            this.cacheDir.deleteRecursively()
            mAlertDialog.dismiss()
            Toast.makeText(this,"Successfully Clear Cache", Toast.LENGTH_LONG).show()
        }
    }

    fun analyseStorage(context: Context) {
        val appBaseFolder: File = context.getFilesDir().getParentFile()
        val totalSize = browseFiles(appBaseFolder)

        tv_data.text = (totalSize / 1024).toString()+ " MB"
        Log.d("Stored", "App uses $totalSize total bytes")
    }

    private fun browseFiles(dir: File): Long {
        var dirSize: Long = 0
        for (f in dir.listFiles()) {
            dirSize += f.length()

            if(dir.absolutePath.contains("cache")){
                tv_cache.text = (f.length()/1024).toString()+ " MB"
            }
            Log.d(
                "Stored",
                dir.getAbsolutePath()
                    .toString() + "/" + f.getName() + " uses " + f.length() + " bytes"
            )
            if (f.isDirectory()) {
                dirSize += browseFiles(f)
            }
        }
        Log.d("Stored", dir.getAbsolutePath().toString() + " uses " + dirSize + " bytes")
        return dirSize
    }

}

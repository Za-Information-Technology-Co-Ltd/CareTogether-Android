package com.za.caretogether.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.za.caretogether.R
import kotlinx.android.synthetic.main.activity_case_info.*
import kotlinx.android.synthetic.main.limit_area_dialog.view.*
import kotlinx.android.synthetic.main.not_limit_area_dialog.view.*

class CaseInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_info)

        back.setOnClickListener {
            finish()
        }

        btn_send.setOnClickListener {

            notlimitdialog()
        }
    }

    fun notlimitdialog(){

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.not_limit_area_dialog, null)
        val mBuilder = AlertDialog.Builder(this!!)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        mDialogView.btn_not_limit_close.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    fun limitdialog(){

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.limit_area_dialog, null)
        val mBuilder = AlertDialog.Builder(this!!)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        mDialogView.btn_limit_close.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }
}

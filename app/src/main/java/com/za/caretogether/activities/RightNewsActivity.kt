package com.za.caretogether.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.za.caretogether.R
import kotlinx.android.synthetic.main.activity_right_news.*
import kotlinx.android.synthetic.main.phone_receiver.*
import java.util.jar.Manifest

class RightNewsActivity : AppCompatActivity() {
    var REQUEST_PHONE_CALL = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_right_news)

        back.setOnClickListener {
            finish()
        }

        fb_link_one.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(fb_link_one.text.toString()))
            startActivity(i)
        }

        fb_link_three.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(fb_link_three.text.toString()))
            startActivity(i)
        }

        web_link_one.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(web_link_one.text.toString()))
            startActivity(i)
        }

        web_link_two.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(web_link_two.text.toString()))
            startActivity(i)
        }

        emergency_phone_one.setOnClickListener {

            val dialog = Dialog(this)
            dialog.setContentView(R.layout.phone_receiver)

            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

            dialog.receiver0.text = "0673420268"

            if (dialog.receiver0.text != null) {

                dialog.receiver0.visibility = View.VISIBLE

                dialog.receiver0.setOnClickListener {
                    val phonenumber = String.format("tel: %s", "0673420268")

                    val callintent = Intent(Intent.ACTION_CALL)
                    callintent.data = Uri.parse(phonenumber)
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            ((this as Activity?)!!),
                            arrayOf(
                                android.Manifest.permission.CALL_PHONE

                            ),
                            REQUEST_PHONE_CALL
                        )
                    } else {

                        startActivity(callintent)

                    }
                }

            } else {
                dialog.receiver0.visibility = View.GONE
            }

            dialog.receiver1.visibility = View.GONE
            dialog.receiver2.visibility = View.GONE
            dialog.receiver3.visibility = View.GONE

            // if button is clicked, close the custom dialog
            dialog.close_cancel.setOnClickListener(View.OnClickListener {
                dialog.dismiss()

            })

            dialog.show()
        }

        emergency_phone_two.setOnClickListener {

            val dialog = Dialog(this)
            dialog.setContentView(R.layout.phone_receiver)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

            dialog.receiver0.text = "09449001261"

            if (dialog.receiver0.text != null) {

                dialog.receiver0.visibility = View.VISIBLE

                dialog.receiver0.setOnClickListener {
                    val phonenumber = String.format("tel: %s", "09449001261")

                    val callintent = Intent(Intent.ACTION_CALL)
                    callintent.data = Uri.parse(phonenumber)
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            ((this as Activity?)!!),
                            arrayOf(
                                android.Manifest.permission.CALL_PHONE

                            ),
                            REQUEST_PHONE_CALL
                        )
                    } else {

                        startActivity(callintent)

                    }
                }

            } else {
                dialog.receiver0.visibility = View.GONE
            }

            dialog.receiver1.text = "09794510057"
            if (dialog.receiver1.text != null) {

                dialog.receiver1.visibility = View.VISIBLE

                dialog.receiver1.setOnClickListener {
                    val phonenumber = String.format("tel: %s", "09794510057")

                    val callintent = Intent(Intent.ACTION_CALL)
                    callintent.data = Uri.parse(phonenumber)
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            ((this as Activity?)!!),
                            arrayOf(
                                android.Manifest.permission.CALL_PHONE

                            ),
                            REQUEST_PHONE_CALL
                        )
                    } else {

                        startActivity(callintent)

                    }
                }

            } else {
                dialog.receiver1.visibility = View.GONE
            }

            dialog.receiver2.visibility = View.GONE
            dialog.receiver3.visibility = View.GONE

            dialog.close_cancel.setOnClickListener(View.OnClickListener {
                dialog.dismiss()

            })

            dialog.show()

        }

        emergency_phone_three.setOnClickListener {

            val dialog = Dialog(this)
            dialog.setContentView(R.layout.phone_receiver)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

            dialog.receiver0.text = "092000344"

            if (dialog.receiver0.text != null) {

                dialog.receiver0.visibility = View.VISIBLE

                dialog.receiver0.setOnClickListener {
                    val phonenumber = String.format("tel: %s", "092000344")

                    val callintent = Intent(Intent.ACTION_CALL)
                    callintent.data = Uri.parse(phonenumber)
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            ((this as Activity?)!!),
                            arrayOf(
                                android.Manifest.permission.CALL_PHONE

                            ),
                            REQUEST_PHONE_CALL
                        )
                    } else {

                        startActivity(callintent)

                    }
                }

            } else {
                dialog.receiver0.visibility = View.GONE
            }

            dialog.receiver1.text = "0943099526"
            if (dialog.receiver1.text != null) {

                dialog.receiver1.visibility = View.VISIBLE

                dialog.receiver1.setOnClickListener {
                    val phonenumber = String.format("tel: %s", "0943099526")

                    val callintent = Intent(Intent.ACTION_CALL)
                    callintent.data = Uri.parse(phonenumber)
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            ((this as Activity?)!!),
                            arrayOf(
                                android.Manifest.permission.CALL_PHONE

                            ),
                            REQUEST_PHONE_CALL
                        )
                    } else {

                        startActivity(callintent)

                    }
                }

            } else {
                dialog.receiver1.visibility = View.GONE
            }

            dialog.receiver2.visibility = View.GONE
            dialog.receiver3.visibility = View.GONE

            dialog.close_cancel.setOnClickListener(View.OnClickListener {
                dialog.dismiss()

            })

            dialog.show()

        }

        emergency_phone_four.setOnClickListener {

            val dialog = Dialog(this)
            dialog.setContentView(R.layout.phone_receiver)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

            dialog.receiver0.text = "09799983833"

            if (dialog.receiver0.text != null) {

                dialog.receiver0.visibility = View.VISIBLE

                dialog.receiver0.setOnClickListener {
                    val phonenumber = String.format("tel: %s", "09799983833")

                    val callintent = Intent(Intent.ACTION_CALL)
                    callintent.data = Uri.parse(phonenumber)
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            ((this as Activity?)!!),
                            arrayOf(
                                android.Manifest.permission.CALL_PHONE

                            ),
                            REQUEST_PHONE_CALL
                        )
                    } else {

                        startActivity(callintent)

                    }
                }

            } else {
                dialog.receiver0.visibility = View.GONE
            }

            dialog.receiver1.visibility = View.GONE
            dialog.receiver2.visibility = View.GONE
            dialog.receiver3.visibility = View.GONE

            // if button is clicked, close the custom dialog
            dialog.close_cancel.setOnClickListener(View.OnClickListener {
                dialog.dismiss()

            })

            dialog.show()
        }

    }
}

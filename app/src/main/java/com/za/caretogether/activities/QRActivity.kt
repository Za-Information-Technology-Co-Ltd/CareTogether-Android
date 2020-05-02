package com.za.caretogether.activities

import android.R.attr.label
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.za.caretogether.R
import com.za.caretogether.utils.Storage
import kotlinx.android.synthetic.main.activity_qr.*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast


class QRActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)

     //   val content = data_parcel!!.trackingCode
        back.setOnClickListener {
            finish()
        }

        user_id.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copy", user_id.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this,"Copied to Clipboard",Toast.LENGTH_LONG).show()
        }

        var content = Storage.make(this).userPhone
        user_id.text = content.toString()
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        qr_image.setImageBitmap(bitmap)
    }
}

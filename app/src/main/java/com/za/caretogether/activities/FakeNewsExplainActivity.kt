package com.za.caretogether.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.za.caretogether.R
import kotlinx.android.synthetic.main.activity_fake_news_explain.*

class FakeNewsExplainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fake_news_explain)

        back.setOnClickListener {
            finish()
        }
    }
}

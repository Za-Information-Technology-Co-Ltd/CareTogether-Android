package com.za.caretogether.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.za.caretogether.R
import com.za.caretogether.utils.Storage
import com.za.caretogether.utils.availableConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_font_detector.*
import kotlinx.android.synthetic.main.content_font_dector.*
import org.jetbrains.anko.startActivity


class FontDetectorActivity : AppCompatActivity() {

    var uni = false
    var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_font_detector)

        getSetting()

        cv_uni.setOnClickListener {
            checkbox_uni.visibility = View.VISIBLE
            checkbox_zawgyi.visibility = View.GONE
            uni = false
        }

        cv_zawgyi.setOnClickListener {
            checkbox_uni.visibility = View.GONE
            checkbox_zawgyi.visibility = View.VISIBLE
            uni = true
        }

        btn_choose_font.setOnClickListener {

            Storage.make(this).saveIsZawgyi(uni)
            Storage.make(this).saveIsFirstTime(false)
            startActivity<IntroActivity>()
            finish()
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

                                font_logo.setImageResource(R.mipmap.logo)
                                font_title.text = "လူမှုဝန်ထမ်း၊ကယ်ဆယ်ရေးနှင့်\n" +
                                        "ပြန်လည်နေရာချထားရေးဝန်ကြီးဌာန"
                                font_title.textSize = 16F


                            } else {
                                font_logo.setImageResource(R.mipmap.ic_launcher)
                                font_title.text = "Care Together"
                                font_title.textSize = 20F
                            }

                        }


                    }, {

                        // Log.d("Denied", it.localizedMessage)

                    })
            )
        }

    }

}

package com.za.caretogether.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.za.caretogether.MainActivity
import com.za.caretogether.R
import com.za.caretogether.response.Local
import com.za.caretogether.utils.LocaleAdapter
import com.za.caretogether.utils.availableConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_locale.*

class LocaleActivity : AppCompatActivity() {

    var disposable = CompositeDisposable()
    var localeList = mutableListOf<Local>()
    lateinit var localeAdapter : LocaleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locale)
        back.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        getSetting()
    }

    override fun onBackPressed() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    private fun getSetting(){

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

                        if (it.code() == 200){

                            localeList.addAll(it.body()!!.localList)

                            localeAdapter = LocaleAdapter(localeList,this,"")

                            val layoutManager = LinearLayoutManager(this)
                            recycler_locale.layoutManager = layoutManager
                            recycler_locale.adapter = localeAdapter
                            localeAdapter.notifyDataSetChanged()


                        }


                    }, {

                        // Log.d("Denied", it.localizedMessage)

                    })
            )
        }

    }

}

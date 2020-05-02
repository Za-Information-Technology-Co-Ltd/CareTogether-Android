package com.za.caretogether

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import com.za.caretogether.utils.ChCrypto
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class FileService :JobService() {

   @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
   override fun onStartJob(params: JobParameters):Boolean {
        /* executing a task synchronously */

       val baseDir =
           getExternalFilesDir(null)!!.absolutePath
       val fileName = "AnalysisData.csv"
       val filePath = baseDir + File.separator + fileName

       val reader2 = CSVReader(FileReader(filePath))
       val allElements =
           reader2.readAll()
       val copyElements =
           reader2.readAll()
       val sdf =
           SimpleDateFormat("yyyy:MM:dd", Locale.ENGLISH)
       val currentDate = sdf.format(Date())

       allElements.forEachIndexed { index, strings ->

           Log.i("today",currentDate)
           Log.i("Expired", ChCrypto.tryMeHackMore(strings[4],getSomething()))
           if(currentDate == ChCrypto.tryMeHackMore(strings[4],getSomething())){
               copyElements.removeAt(index)
           }
       }
       val sw = FileWriter(filePath)
       val writer = CSVWriter(sw)
       writer.writeAll(copyElements)
       writer.close()

        return false
    }
    override fun onStopJob(params:JobParameters):Boolean {
        return false
    }

    external fun getSomething(): String

    companion object {

        init {
            System.loadLibrary("native-lib")
        }
        private val JOB_ID = 1
        private val ONE_DAY_INTERVAL = 24 * 60 * 60 * 1000L // 1 Day
        private val ONE_WEEK_INTERVAL = 7 * 24 * 60 * 60 * 1000L // 1 Week
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun schedule(context:Context, intervalMillis:Long) {
            val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val componentName = ComponentName(context, FileService::class.java)
            val builder = JobInfo.Builder(JOB_ID, componentName)
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            builder.setPeriodic(intervalMillis)
            jobScheduler.schedule(builder.build())
        }
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun cancel(context:Context) {
            val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.cancel(JOB_ID)
        }
    }
}
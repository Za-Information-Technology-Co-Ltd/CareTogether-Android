package com.za.caretogether

import android.R
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.LeScanCallback
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.os.ParcelUuid
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import com.za.caretogether.utils.ChCrypto
import com.za.caretogether.utils.MyLocation
import com.za.caretogether.utils.Storage
import org.jetbrains.anko.bluetoothManager
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class BluetoothService : Service() {

    private val mDeviceList =
        ArrayList<String>()
    var device_data: String? = null
    var lat = 0.0
    var long = 0.0
    var count = 1
    var already_exist = false
    lateinit var mBtAdapter: BluetoothAdapter

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    external fun getSomething(): String
    override fun onCreate() {
        super.onCreate()
        startForeground()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //TODO do something useful

        mBtAdapter = BluetoothAdapter.getDefaultAdapter()

        var name = Storage.make(applicationContext).userPhone
        mBtAdapter!!.name = "CT-$name"

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && Storage.make(this).isFirstConnect) {

            val discoverableIntent: Intent =
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                    putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0)
                }
            discoverableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(discoverableIntent)
            Storage.make(this).saveIsFirstConnect(false)

        }

        Log.i("Service", "Running")

        getLastLocation()

        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {

            override fun run() {
                // Getting the Bluetooth adapter

                if (mBtAdapter != null) {

                    mBtAdapter.startDiscovery()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startAdvertising()
                    }
                    val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
                    registerReceiver(mBtReceiver, filter)
                    Log.i("Starting discovery...", " Service")

                }

            }
        }, 0, 180000)

        return START_NOT_STICKY
    }

    private fun startAdvertising() {
        val bluetoothLeAdvertiser: BluetoothLeAdvertiser? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bluetoothManager.adapter.bluetoothLeAdvertiser
            } else {
                TODO("VERSION.SDK_INT < LOLLIPOP")
            }

        bluetoothLeAdvertiser?.let {
            val settings = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AdvertiseSettings.Builder()
                    .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                    .setConnectable(true)
                    .setTimeout(0)
                    .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                    .build()
            } else {
                TODO("VERSION.SDK_INT < LOLLIPOP")
            }

            val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AdvertiseData.Builder()
                    .setIncludeDeviceName(true)
                    .setIncludeTxPowerLevel(false)
                    //.addServiceUuid(ParcelUuid(TimeProfile.TIME_SERVICE))
                    .build()
            } else {
                TODO("VERSION.SDK_INT < LOLLIPOP")
            }

            it.startAdvertising(settings, data, advertiseCallback)
        } ?: Log.w("TAG", "Failed to create advertiser")
    }

    private val advertiseCallback = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                Log.i("TAG", "LE Advertise Started.")
            }

            override fun onStartFailure(errorCode: Int) {
                Log.w("TAG", "LE Advertise Failed: $errorCode")
            }
        }
    } else {
        TODO("VERSION.SDK_INT < LOLLIPOP")
    }


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    private val mBtReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                if (!mDeviceList.contains(device.address + ", " + device.name)) {
                    mDeviceList.add(device.address + ", " + device.name) // get mac address

                    device_data = device.address

                    val sdf =
                        SimpleDateFormat("yyyy:MM:dd-HH:mm:ss", Locale.ENGLISH)
                    val sdf2 =
                        SimpleDateFormat("yyyy:MM:dd", Locale.ENGLISH)
                    val currentDateandTime = sdf.format(Date())

                    val calendar = Calendar.getInstance()
                    val today = calendar.time

                    calendar.add(Calendar.DAY_OF_YEAR, 60)
                    val expiredTime = calendar.time

                    val expiredDate = sdf2.format(expiredTime)
                    val todayDate = sdf2.format(today)

                    Log.i("Current Date ", currentDateandTime)
                    Log.i("Address ", device_data)

                    val baseDir =
                        getExternalFilesDir(null)!!.absolutePath
                    val fileName = "AnalysisData.csv"
                    val filePath = baseDir + File.separator + fileName
                    val f = File(filePath)
                    var writer: CSVWriter? = null
                    var reader2: CSVReader? = null

                    // File exist
                    if (f.exists() && !f.isDirectory) {
                        var mFileWriter: FileWriter? = null
                        var mFileReader: FileReader? = null
                        try {
                            mFileWriter = FileWriter(filePath, true)
                            mFileReader = FileReader(filePath)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        writer = CSVWriter(mFileWriter)
                        reader2 = CSVReader(mFileReader)
                    } else {
                        try {
                            writer = CSVWriter(FileWriter(filePath))
                            reader2 = CSVReader(FileReader(filePath))
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }

                    if (!device.name.isNullOrBlank()) {
                        if (device.name.startsWith("CT")) {

                            var name = device.name.toString()
                            name = name.substring(3)

                            val allElements = reader2!!.readAll()

                            allElements!!.forEachIndexed { index, strings ->

                                Log.i("User Phone", name)
                                Log.i("Size", allElements.size.toString())
                                Log.i("Index", index.toString())
                                Log.i("Phone ", strings[1])

                                if (name == strings[1]) {
                                    if (index > 0) {
                                        updateCSV(
                                            filePath,
                                            ChCrypto.tryMeHack(currentDateandTime, getSomething()),
                                            index,
                                            0
                                        )

                                        updateCSV(
                                            filePath,
                                            ChCrypto.tryMeHack(lat.toString(), getSomething()),
                                            index,
                                            2
                                        )

                                        updateCSV(
                                            filePath,
                                            ChCrypto.tryMeHack(long.toString(), getSomething()),
                                            index,
                                            3
                                        )

                                        updateCSV(
                                            filePath,
                                            ChCrypto.tryMeHack(expiredDate, getSomething()),
                                            index,
                                            4
                                        )

                                        Log.i("Updated", "CSV at" + index.toString())
                                    }
                                    already_exist = true
                                }
                            }

                            if (!already_exist) {

                                var data = arrayOf(
                                    ChCrypto.tryMeHack(currentDateandTime, getSomething()),
                                    name,
                                    ChCrypto.tryMeHack(lat.toString(), getSomething()),
                                    ChCrypto.tryMeHack(long.toString(), getSomething()),
                                    ChCrypto.tryMeHack(expiredDate, getSomething())
                                )

                                writer!!.writeNext(data)
                                var count = Storage.make(applicationContext).count
                                Storage.make(applicationContext).count = count + 1

                                try {
                                    writer!!.close()
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                            }

                            var count = Storage.make(applicationContext).count.toString()
                            Log.i("Data Count ", count)
                        }
                    }
                }
            }
        }
    }


    @Throws(IOException::class)
    fun updateCSV(
        fileToUpdate: String?, replace: String,
        row: Int, col: Int
    ) {
        val inputFile = File(fileToUpdate)

        // Read existing file
        val reader = CSVReader(FileReader(inputFile))
        val csvBody = reader.readAll()
        // get CSV row column  and replace with by using row and column
        csvBody[row][col] = replace
        reader.close()

        // Write to CSV file which is open
        val writer = CSVWriter(FileWriter(inputFile))
        writer.writeAll(csvBody)
        writer.flush()
        writer.close()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {

        if (isLocationEnabled()) {

            var locationResult: MyLocation.LocationResult = object : MyLocation.LocationResult() {

                override fun gotLocation(location: Location) {
                    try {

                        Log.i(
                            "Location Now :",
                            location.latitude.toFloat()
                                .toString() + " , " + location.longitude.toString()
                        )

                        Storage.make(applicationContext).lat =
                            location.latitude.toFloat().toString()
                        Storage.make(applicationContext).long =
                            location.longitude.toFloat().toString()

                        if (location.latitude != 0.0 && location.longitude != 0.0) {

                            if (lat != 0.0 && long != 0.0) {
                                val distance = FloatArray(1)
                                Location.distanceBetween(
                                    lat,
                                    long,
                                    location!!.latitude,
                                    location!!.longitude,
                                    distance
                                )
                                // distance[0] is now the distance between these lat/lons in meters
                                // distance[0] is now the distance between these lat/lons in meters
//                                if (distance[0].toFloat() > 100.0) {
////                                    val lockIntent =
////                                        Intent(applicationContext, WarningActivity::class.java)
////                                    lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
////                                    applicationContext.startActivity(lockIntent)
//                                }
                                Log.i("Distance Meter :", distance[0].toFloat().toString())

                                lat = location!!.latitude
                                long = location!!.longitude

                            } else {
                                lat = location!!.latitude
                                long = location!!.longitude
                            }

                            val sdf =
                                SimpleDateFormat("yyyy:MM:dd-HH:mm:ss", Locale.ENGLISH)
                            val sdf2 =
                                SimpleDateFormat("yyyy:MM:dd", Locale.ENGLISH)
                            val currentDateandTime = sdf.format(Date())

                            val calendar = Calendar.getInstance()
                            val today = calendar.time

                            val todayDate = sdf2.format(today)

                            Log.i("Current Date ", currentDateandTime)

                            val baseDir =
                                getExternalFilesDir(null)!!.absolutePath
                            val fileName = "AnalysisData2.csv"
                            val filePath = baseDir + File.separator + fileName
                            val f = File(filePath)
                            var writer: CSVWriter? = null

                            // File exist
                            if (f.exists() && !f.isDirectory) {
                                var mFileWriter: FileWriter? = null
                                var mFileReader: FileReader? = null
                                try {
                                    mFileWriter = FileWriter(filePath, true)
                                    mFileReader = FileReader(filePath)
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                                writer = CSVWriter(mFileWriter)
                                Log.i("CSV","File Exists")
                            } else {
                                try {
                                    writer = CSVWriter(FileWriter(filePath))
                                    Log.i("CSV","File Create")
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                            }

                            var data = arrayOf(
                                ChCrypto.tryMeHack(
                                    currentDateandTime,
                                    getSomething()
                                ),
                                ChCrypto.tryMeHack(lat.toString(), getSomething()),
                                ChCrypto.tryMeHack(long.toString(), getSomething())
                            )

                            writer!!.writeNext(data)
                            Log.i("CSV","File Write")

                            try {
                                writer!!.close()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }


                        } else {

                        }

                    } catch (e: NullPointerException) {
                    }
                }
            }

            var myLocation = MyLocation()
            myLocation.getLocation(this, locationResult)

        } else {
            //  Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun startForeground() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.alert_light_frame)
            .setPriority(PRIORITY_MIN)
            .setContentTitle("Care Together")
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(101, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}

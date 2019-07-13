package com.example.irem.examdates

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class MainActivity : AppCompatActivity() {

    var date:String=""
    var id:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val examArray = ArrayList<Exam>()
        val customAdapter=CustomAdapter(applicationContext,examArray)
        listView.adapter=customAdapter

        try {
            val database = this.openOrCreateDatabase("Exam", Context.MODE_PRIVATE, null)
            database.execSQL("CREATE TABLE IF NOT EXISTS exam(name VARCHAR, date TEXT)")

            val cursor = database.rawQuery("SELECT * FROM exam", null)
            val nameIx = cursor.getColumnIndex("name")

            val dateIx = cursor.getColumnIndex("date")

            cursor.moveToFirst()

            while (cursor != null) {

                date = cursor.getString(dateIx)
                examArray.add(Exam(cursor.getString(nameIx),cursor.getString(dateIx)/*dateArray.get(0),dateArray.get(1),dateArray.get(2),dateArray.get(3)*/))
                cursor.moveToNext()
                customAdapter.notifyDataSetChanged()

            }
            cursor?.close()

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("tagid",e.toString())
        }
    }

    fun add(view: View) {
        val intent = Intent(applicationContext, AddExamActivity::class.java)
        startActivity(intent)
    }


    fun notification(){
        val intent=Intent()
        val pendingIntent=PendingIntent.getActivity(this,0,intent,0)
        val notification=Notification.Builder(this)
                .setTicker(" ")
                .setContentTitle("My Exam Dates")
                .setContentText("Sınav Zamanı")
                .setContentIntent(pendingIntent).notification
        notification.flags=Notification.FLAG_AUTO_CANCEL
        val notificationmanager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationmanager.notify(0,notification)

    }


}



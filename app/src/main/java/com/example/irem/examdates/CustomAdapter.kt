package com.example.irem.examdates

import android.app.*
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.provider.Settings.Global.getString
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.lang.Exception
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CustomAdapter(public var context: Context, var exam: ArrayList<Exam>) : BaseAdapter() {
    var dateArray = ArrayList<Long>()
    companion object {
        var id=0
    }
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder:Notification.Builder
    private val channelId="com.example.irem.examdates"
    private val description="App notification"


    private class ViewHolder(row: View?) {

        var txtName: TextView = row?.findViewById(R.id.examName) as TextView
        var hourval: TextView
        var dayval: TextView
        var minval: TextView
        var secval: TextView


        init {
            this.dayval = row?.findViewById(R.id.dayText) as TextView
            this.hourval = row?.findViewById(R.id.hourText) as TextView
            this.minval = row?.findViewById(R.id.minuteText) as TextView
            this.secval = row?.findViewById(R.id.secondText) as TextView

        }

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            val layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.list_record, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder

        }

        val exam: Exam = getItem(position) as Exam
        viewHolder.txtName.text = exam.name
       /* runnable = object : Runnable {
              override fun run() {
              Log.e("tag0","run")
              if( viewHolder.dayval.text=="00" && viewHolder.hourval.text=="00"&& viewHolder.minval.text=="00"&& viewHolder.secval.text=="01"){
                    Log.e("time","time is up")
                    handler.removeCallbacks(runnable)
                }
                handler.postDelayed(this, 1000)
            }}
        handler.post(runnable)*/
        try {
            notificationManager= context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            var mil = getTime(exam.date)
            object : CountDownTimer(mil, 1000) {
                override fun onFinish() {
                    Log.e("tag", "time is off")
                    val intent=Intent()
                    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                        notificationChannel.enableLights(true)
                        notificationChannel.lightColor = Color.GREEN
                        notificationChannel.enableVibration(false)
                        notificationManager.createNotificationChannel(notificationChannel)

                        builder=Notification.Builder(context,channelId)

                                .setSmallIcon(R.drawable.date)
                                .setContentTitle("My Exam Dates")
                                .setContentText("${exam.name} Sınav Zamanı")
                                .setContentIntent(pendingIntent)
                    }
                    else{
                        builder=Notification.Builder(context)

                                .setSmallIcon(R.drawable.date)
                                .setContentTitle("My Exam Dates")
                                .setContentText("${exam.name} Sınavı Zamanı")
                                .setContentIntent(pendingIntent)
                    }
                    notificationManager.notify(1234,builder.build())


                    /*val intent = Intent()
                    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
                    val notification = Notification.Builder(context)
                            .setContentTitle("Bildirim")
                            .setSmallIcon(R.drawable.date)
                            .setContentTitle("My Exam Dates")
                            .setContentText("Sınav Zamanı")
                    notification.setContentIntent(pendingIntent)
                    Log.e("tag0", "time is off")
                    val notificationmanager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    Log.e("tag2", "time is off")
                    notificationmanager.notify(id, notification.build())
                    id++*/
                    Log.e("tag1", "time is off")

                    // Toast.makeText(viewHolder.txtName.context,"${exam.name} Sınavı Zamanı",Toast.LENGTH_LONG).show()

                }

                override fun onTick(millisUntilFinished: Long) {
                    dateArray = countdown(exam.date)


                    // Log.e("tag0", dateArray.toString())
                    viewHolder.dayval.setText("" + String.format("%02d", dateArray.get(0)))
                    viewHolder.hourval.setText("" + String.format("%02d", dateArray.get(1)))
                    viewHolder.minval.setText("" + String.format("%02d", dateArray.get(2)))
                    viewHolder.secval.setText("" + String.format("%02d", dateArray.get(3)))


                }

            }.start()
        }
        catch (e:Exception){
            Log.e("tag3",e.toString())
        }
        /* imgRemove.setOnClickListener(new OnClickListener() {
   @Override
   public void onClick(View v) {
    itemModelList.remove(position)
    notifyDataSetChanged()
   }
   });  */



        return view
    }

    override fun getItem(position: Int): Any {
        return exam.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return exam.count()
    }

    fun countdown(date: String): java.util.ArrayList<Long> {
        val select = ArrayList<Long>()
        try {
            var formatter = DateTimeFormatter.ISO_DATE_TIME
            val now = LocalDateTime.now()
            //Log.e("tag0",now.toString())
            val futureDate = LocalDateTime.parse(date, formatter)
           // Log.e("tag0",futureDate.toString())
            if (now.isBefore(futureDate)) {
                var diff = Duration.between(now, futureDate).toMillis()
                var days = (diff / (24 * 60 * 60 * 1000)) % 365
                var hours = ((diff / (1000 * 60 * 60)) % 24)
                var minutes = ((diff / (1000 * 60)) % 60)
                var seconds = (diff / 1000) % 60

                select.add(days)
                select.add(hours)
                select.add(minutes)
                select.add(seconds)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return select
    }
    fun getTime(date: String):Long{
        var diff:Long=0
        var formatter = DateTimeFormatter.ISO_DATE_TIME
        val now = LocalDateTime.now()
        // Log.e("time",now.toString())
        val futureDate = LocalDateTime.parse(date, formatter)
       // Log.e("tag0",futureDate.toString())
        if (now.isBefore(futureDate)) {
             diff = Duration.between(now, futureDate).toMillis()
        }

        return diff
    }


}





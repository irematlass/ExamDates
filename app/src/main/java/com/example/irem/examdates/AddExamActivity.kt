package com.example.irem.examdates

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_exam.*
import java.lang.Exception
import java.util.*

class AddExamActivity : AppCompatActivity() {

    var bool:Boolean=true
    var selectedHour = ""
    var selectedDate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exam)

    }

    fun clickDatePicker(view: View) {
        val c = Calendar.getInstance()
        val cyear = c.get(Calendar.YEAR)
        val cmonth = c.get(Calendar.MONTH)
        val cday = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in Toast

            val month = monthOfYear + 1
            if((cyear>year) ||(cyear==year && cmonth>monthOfYear) ||(cyear==year && cmonth==monthOfYear && cday>dayOfMonth)){
                Toast.makeText(this,"Geçersiz tarih seçtiniz.",Toast.LENGTH_LONG).show()
                bool=false
            }


            else {
                Toast.makeText(this, """$dayOfMonth - ${monthOfYear + 1} - $year""", Toast.LENGTH_LONG).show()
                selectedDate = String.format("%04d-%02d-%02d", year, month, dayOfMonth)
                bool=true
            }}, cyear, cmonth, cday)
        dpd.show()

    }

    fun clickTimePicker(view: View) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)
        val san = c.get(Calendar.SECOND)

        val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener(function = { view, h, m ->

            Toast.makeText(this, h.toString() + " : " + m, Toast.LENGTH_LONG).show()
            selectedHour = String.format("%02d:%02d:00.000", h, m)
        }), hour, minute, false)

        tpd.show()
    }

    fun save(view: View) {

        val examName = editText.text.toString()
        if(TextUtils.isEmpty(editText.getText().toString())){
            Toast.makeText(applicationContext,"Sınav ismi boş geçilemez",Toast.LENGTH_LONG).show()
        }
        else if (bool==false){
            Toast.makeText(applicationContext,"Lütfen geçerli bir tarih giriniz",Toast.LENGTH_LONG).show()
        }
        else{
        val selectedDates = selectedDate + "T" + selectedHour
        try {
            val database = this.openOrCreateDatabase("Exam", Context.MODE_PRIVATE, null)
            database.execSQL("CREATE TABLE IF NOT EXISTS exam(name VARCHAR(30) ,date TEXT)")

            val sqlString = "INSERT INTO exam(name,date) VALUES(?,?)"
            val statement = database.compileStatement(sqlString)
            statement.bindString(1, examName)
            statement.bindString(2, selectedDates)
            statement.execute()


            Toast.makeText(this, "Sınav Oluşturuldu", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("tag1",e.toString())
        }
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }}


}


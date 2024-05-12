package com.ionhax.androidalertapplication

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.ParseException
import java.util.Calendar

class RemainderActivity : AppCompatActivity() {

    private lateinit var mSubmitbtn: Button
    private lateinit var mDatebtn: Button
    private lateinit var mTimebtn: Button
    private lateinit var mTitledit: EditText
    private var timeToNotify: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_remainder)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mTitledit = findViewById(R.id.editTitle)
        mDatebtn = findViewById(R.id.btnDate)
        mTimebtn = findViewById(R.id.btnTime)
        mSubmitbtn = findViewById(R.id.btnSbumit)

        mTimebtn.setOnClickListener { selectTime() }

        mDatebtn.setOnClickListener { selectDate() }

        mSubmitbtn.setOnClickListener {
            val title = mTitledit.text.toString().trim()
            val date = mDatebtn.text.toString().trim()
            val time = mTimebtn.text.toString().trim()

            when {
                title.isEmpty() -> Toast.makeText(
                    applicationContext,
                    "Please Enter text",
                    Toast.LENGTH_SHORT
                ).show()

                time == "time" || date == "date" -> Toast.makeText(
                    applicationContext,
                    "Please select date and time",
                    Toast.LENGTH_SHORT
                ).show()

                else -> processInsert(title, date, time)
            }
        }
    }

    private fun processInsert(title: String, date: String, time: String) {
        val result = DbManager(this).addReminder(title, date, time)
        setAlarm(title, date, time)
        mTitledit.setText("")
        Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
    }

    private fun selectTime() {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(
            this,
            { _, i, i1 ->
                timeToNotify = "$i:$i1"
                mTimebtn.text = formatTime(i, i1)
            }, hour, minute, false
        )
        timePickerDialog.show()
    }

    private fun selectDate() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                mDatebtn.text = "$day-${month + 1}-$year"
            }, year, month, day
        )
        datePickerDialog.show()
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val time: String
        time = if (hour == 0) {
            "12:$minute AM"
        } else if (hour < 12) {
            "$hour:$minute AM"
        } else if (hour == 12) {
            "12:$minute PM"
        } else {
            val temp = hour - 12
            "$temp:$minute PM"
        }
        return time
    }

    private fun setAlarm(text: String, date: String, time: String) {
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(applicationContext, AlarmBroadcast::class.java).apply {
            putExtra("event", text)
            putExtra("time", date)
            putExtra("date", time)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val dateAndTime = "$date $timeToNotify"
        val formatter: DateFormat = SimpleDateFormat("d-M-yyyy hh:mm")
        try {
            val date1 = formatter.parse(dateAndTime)
            am.set(AlarmManager.RTC_WAKEUP, date1.time, pendingIntent)
            Toast.makeText(applicationContext, "Alaram", Toast.LENGTH_SHORT).show()
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val intentBack = Intent(applicationContext, MainActivity::class.java)
        intentBack.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intentBack)
    }
}
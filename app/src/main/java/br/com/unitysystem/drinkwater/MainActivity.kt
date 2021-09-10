package br.com.unitysystem.drinkwater

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private var hour = 0
    private var minute = 0
    private var interval = 0
    private var activated: Boolean = false

    private lateinit var sharedPref: SharedPreferences


    private lateinit var editTxtNumberInterval: EditText
    private lateinit var timePicker: TimePicker
    private lateinit var btnNotify: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNotify = findViewById<Button>(R.id.btn_notify)
        editTxtNumberInterval = findViewById<EditText>(R.id.edit_txt_number_interval)
        timePicker = findViewById<TimePicker>(R.id.time_picker)

        timePicker.setIs24HourView(true)

        sharedPref = getSharedPreferences("db", Context.MODE_PRIVATE)

        activated = sharedPref.getBoolean("activated", false)

        if (activated) {
            btnNotify.text = getText(R.string.pause)
            val color = ContextCompat.getColor(this, android.R.color.black)
            btnNotify.setBackgroundColor(color)

            var interval = sharedPref.getInt("interval", 0)
            var hour = sharedPref.getInt("hour", 0)
            var minute = sharedPref.getInt("minute", 0)

            editTxtNumberInterval.setText(interval.toString())
            timePicker.currentHour = hour
            timePicker.currentMinute = minute
        }

        btnNotify.setOnClickListener {
            notifyClick()
        }

    }

    fun notifyClick() {

        var sInterval = (editTxtNumberInterval?.text?.toString()) ?: "0"

        if (sInterval.isEmpty()) {
            Toast.makeText(this, R.string.error_msg, Toast.LENGTH_SHORT).show()
            return
        }

        hour = timePicker.currentHour
        minute = timePicker.currentMinute
        interval = sInterval.toInt()
        //interval = if (sInterval.isEmpty()) 0 else sInterval.toInt()

        if (!activated) {
            btnNotify.text = getText(R.string.pause)
            val color = ContextCompat.getColor(this, android.R.color.black)
            btnNotify.setBackgroundColor(color)
            activated = true

            val editor: SharedPreferences.Editor  = sharedPref.edit()
            editor.putBoolean("activated", true)
            editor.putInt("interval", interval)
            editor.putInt("hour", hour)
            editor.putInt("minute", minute)
            editor.apply()

        } else {
            btnNotify.text = getText(R.string.notify)
            val color = ContextCompat.getColor(this, R.color.teal_200)
            btnNotify.setBackgroundColor(color)
            activated = false
            val editor: SharedPreferences.Editor  = sharedPref.edit()
            editor.putBoolean("activated", false)
            editor.remove("interval")
            editor.remove("hour")
            editor.remove("minute")
            editor.apply()
        }
        
        Log.d("Test", "Hora: $hour Minuto: $minute Intervalo: $interval")
    }
}
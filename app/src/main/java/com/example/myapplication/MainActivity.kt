package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.myapplication.databinding.ActivityMainBinding
import java.net.URLEncoder


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var numberbox : EditText
    private lateinit var messagebox : EditText
    private lateinit var sendBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        numberbox = findViewById(R.id.numberBox)
        messagebox = findViewById(R.id.messagebox)
        sendBtn = findViewById(R.id.sendBtn)

        sendBtn.setOnClickListener {
            if(isAccessibilityServiceEnabled(this, "com.example.myapplication.MyAccessibilityService")){
                sendWhatsAppMessage(this, numberbox.text.toString(), messagebox.text.toString())
            }else{
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

//
        }
    }


    fun sendWhatsAppMessage(context: Context, phoneNumber: String, message: String) {
        try {
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber&text=${URLEncoder.encode(message, "UTF-8")}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle exceptions or display a message to the user
        }
    }

    private val TAG = "MainActivity"

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val message = intent.getStringExtra("message")
            // Do something with the message received from the service
            Log.d(TAG, "Received message from service: $message")
        }
    }

    fun isAccessibilityServiceEnabled(context: Context, accessibilityService: String): Boolean {
        try {
            val enabledServices = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            return enabledServices?.contains(accessibilityService) == true
        } catch (e: Settings.SettingNotFoundException) {
            // Handle the exception
            e.printStackTrace()
            return false
        }
    }
}
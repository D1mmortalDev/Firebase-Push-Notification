package com.example.pushnotificationsfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.pushnotificationsfirebase.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

const val TOPIC = "/topic/myTopic"
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val TAG ="MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //subscribe to topic
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        binding.btnSend.setOnClickListener {
            val title = binding.edTitle.text.toString()
            val message = binding.edMessage.text.toString()

            if (title.isNotEmpty() && message.isNotEmpty()){
                Toast.makeText(this,"Has values",Toast.LENGTH_SHORT).show()
              PushNotification(
                  NotificationData(title,message),
                  TOPIC
             ).also {
                 sendNotifications(it)
                  Toast.makeText(this,"I get inside",Toast.LENGTH_SHORT).show()
              }
            }
        }
    }
    private fun sendNotifications(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.d(TAG,"Response:  ${Gson().toJson(response)}")
            }else{
                Log.d(TAG,response.errorBody().toString())
            }

        }catch (e:Exception){
            Log.e("hello",e.toString())
        }
    }
}
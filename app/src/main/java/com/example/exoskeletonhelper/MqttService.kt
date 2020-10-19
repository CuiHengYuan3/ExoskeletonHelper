package com.example.exoskeletonhelper

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.myapplication.utils.logE
import com.example.myapplication.utils.logV
import com.example.myapplication.utils.showToast
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import javax.security.auth.login.LoginException

class MqttService : Service() {

    private var option: MqttConnectOptions? = null

    private var client: MqttAndroidClient? = null

    var onReciveTopicListener:OnReciveTopicListener?=null
    set(value) {
        field=value
    }

    fun setListenser(onReciveTopicListener:OnReciveTopicListener){
this.onReciveTopicListener=onReciveTopicListener
    }

    inner  class  MyBinder(): Binder() {
       val mqttService:MqttService
        get(){
            return this@MqttService
        }

    }


    private val isConnectIsNormal: Boolean
        get() {
            val connectivityManager = this.applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = connectivityManager.activeNetworkInfo
            return if (info != null && info.isAvailable) {
                val name = info.typeName
                Log.i(TAG, "MQTT 当前网络名称：$name")
                true
            } else {
                Log.i(TAG, "MQTT 没有可用网络")
                false
            }
        }


    private  val mqqtCallback=object :MqttCallback{

        override fun messageArrived(topic: String?, message: MqttMessage?) {

            logV(TAG,"messageArrived topic: $topic message: $message ")

            onReciveTopicListener?.onTopicRecive(topic?:"", message?.payload.toString())
            "接受消息 topic : $topic   message: ${message?.payload}".showToast()
        }

        override fun connectionLost(cause: Throwable?) {
            logV(TAG,"connectionLost")

        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            logV(TAG,"deliveryComplete")

        }

    }


    override fun onCreate() {
        super.onCreate()
        logV(TAG, "onCreat")
       init()

    }


    fun init(){
        val uri = Constant.host
        client=   MqttAndroidClient(this,uri ,Constant.clientId)
     client!!.setCallback(mqqtCallback)
     option=   MqttConnectOptions()
        option?.apply {
            // 清除缓存
            isCleanSession = true
            // 设置超时时间，单位：秒
            connectionTimeout = 10
            // 心跳包发送间隔，单位：秒
            keepAliveInterval = 20
            // 用户名
            userName = Constant.userName
            // 密码
            password = Constant.passWord.toCharArray() //将字符串转换为字符串数组
        }
// last will message
        var doConnect = true
        val message = "{\"terminal_uid\":\"${Constant.clientId}\"}"
        Log.e(TAG, "message是:$message")
        val topic = Constant.subscribeTopic
        val qos = 0
        val retained = false
        try {
            option?.setWill(topic, message.toByteArray(), qos, retained)
        } catch (e: Exception) {
            Log.i(TAG, "Exception Occured", e)
            doConnect = false
            iMqttActionListener.onFailure(null, e)
        }

        if (doConnect) {
            doClientConnection()
        }




    }

    /** 连接MQTT服务器  */
    private fun doClientConnection() {
        client?.let {client->
            if (!client.isConnected && isConnectIsNormal) {
                try {
                    client.connect(option, null, iMqttActionListener)

                } catch (e: MqttException) {
                    logV(TAG, "连接服务器失败 $e")
                }
            }
        }
    }

    // MQTT是否连接成功
    private val iMqttActionListener: IMqttActionListener = object : IMqttActionListener {
        override fun onSuccess(arg0: IMqttToken) {
            logV(TAG, "连接成功 ")
            try { // 订阅myTopic话题
                client?.subscribe(Constant.subscribeTopic, 1)
                logV(TAG, "订阅话题${Constant.subscribeTopic} ")

                "连接成功订阅topic:${Constant.subscribeTopic}".showToast()
            } catch (e: MqttException) {
                logV(TAG, "订阅话题${Constant.subscribeTopic}失败 ")

            }
        }

        override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
            Log.e(TAG,"$exception")
            // 连接失败，重连
        }
    }



    override fun onBind(intent: Intent?): IBinder? {
return  MyBinder()
    }




    companion object {
        private val TAG = "MqttService"
    }

}

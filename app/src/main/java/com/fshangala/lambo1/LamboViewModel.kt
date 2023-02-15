package com.fshangala.lambo1

import android.content.SharedPreferences
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.*

class LamboViewModel : ViewModel() {
    private val appClient = OkHttpClient()
    private var appSocket: WebSocket? = null

    var connectionStatus = MutableLiveData<String>("")
    var automationEvents = MutableLiveData<AutomationEvents>()
    var connected = MutableLiveData<Boolean>(false)
    var browserLoading = MutableLiveData<Boolean>(false)
    var oddButtons = MutableLiveData<Int>(0)
    var currentBetIndex = MutableLiveData<String>("")
    var currentBetIndexOdds = MutableLiveData<String>("")
    var jslog = MutableLiveData<String>("")

    fun createConnection(sharedPref: SharedPreferences){
        connectionStatus.value = "Connecting..."

        val hostIp = sharedPref.getString("hostIp","13.233.109.76")
        val hostPort = sharedPref.getInt("hostPort",80).toString()
        val hostCode = sharedPref.getString("hostCode","sample")

        val host = "ws://$hostIp:$hostPort/ws/pcautomation/$hostCode/"
        val appRequest = Request.Builder().url(host).build()

        try {
            appClient.newWebSocket(appRequest,object: WebSocketListener(){
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    appSocket = webSocket
                    val devicetype = "Brand:${Build.BRAND};Model:${Build.MODEL};Android:${Build.VERSION.RELEASE}"
                    webSocket.send("{\"event_type\":\"connection\",\"event\":\"phone_connected\",\"args\":[${devicetype}],\"kwargs\":{}}")
                    connectionStatus.postValue("Connected!")
                    connected.postValue(true)
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    automationEvents.postValue(AutomationEvents(text))
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    connectionStatus.postValue("Disconnected!")
                    connected.postValue(false)
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    connectionStatus.postValue("Error: ${t.toString()}")
                    connected.postValue(false)
                }
            })
            //appClient.dispatcher.executorService.shutdown()
        } catch (ex: Exception){
            connectionStatus.value = ex.toString()
            connected.postValue(false)
        }
    }

    fun disconnect() {
        appSocket!!.close(1000,"Disconnected.")
    }

    fun sendCommand(data:AutomationObject){
        if (connected.value == true){
            try {
                this.appSocket?.send(data.toString())
            } catch (ex: Exception) {
                connectionStatus.value = ex.toString()
            }
        } else {
            connectionStatus.value = "Failed! You are disconnected!"
        }
    }
}
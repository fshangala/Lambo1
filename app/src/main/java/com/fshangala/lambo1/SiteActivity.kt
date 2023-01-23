package com.fshangala.lambo1

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

class SiteActivity : AppCompatActivity() {
    private var webView: WebView? = null
    private var model: LamboViewModel? = null
    private var masterStatus: TextView? = null
    var sharedPref: SharedPreferences? = null
    var toast: Toast? = null
    var betSite: BetSite? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site)

        webView = findViewById(R.id.webView)

        true.also {
            webView!!.settings.javaScriptEnabled = it
            webView!!.settings.domStorageEnabled = it
        }
        webView!!.addJavascriptInterface(LamboJsInterface(),"lambo")
        model = ViewModelProvider(this)[LamboViewModel::class.java]
        sharedPref = getSharedPreferences("MySettings", Context.MODE_PRIVATE)
        masterStatus = findViewById(R.id.status)

        betSite = BetSite(sharedPref!!.getString("betSite","jack9").toString())
        startBrowser()

        model!!.connectionStatus.observe(this) {
            toast = Toast.makeText(this,it,Toast.LENGTH_SHORT)
            toast!!.show()
        }
        model!!.automationEvents.observe(this) {
            when (it.eventName) {
                "place_bet" -> {
                    placeBet(it)
                    toast = Toast.makeText(this,it.eventArgs.toString(),Toast.LENGTH_LONG)
                    toast!!.show()
                }
                "open_bet" -> {
                    onOpenBet(it)
                    toast = Toast.makeText(this,it.eventArgs.toString(),Toast.LENGTH_LONG)
                    toast!!.show()
                }
                "click_bet" -> {
                    onClickBet(it)
                    toast = Toast.makeText(this,it.eventArgs.toString(),Toast.LENGTH_LONG)
                    toast!!.show()
                }
                "confirm_bet" -> {
                    confirmBet()
                }
                else -> {
                    toast = Toast.makeText(this,it.eventName,Toast.LENGTH_LONG)
                    toast!!.show()
                }
            }
        }
        model!!.browserLoading.observe(this){
            if (it == true) {
                runOnUiThread {
                    masterStatus!!.text = "Loading..."
                }
            } else {
                runOnUiThread {
                    masterStatus!!.text = "Loaded!"
                }
                webView!!.evaluateJavascript(betSite!!.eventListenerScript()) {
                    runOnUiThread {
                        masterStatus!!.text = it
                    }
                }
            }
        }
        model!!.createConnection(sharedPref!!)
    }

    private inner class LamboJsInterface {
        @JavascriptInterface
        fun performClick(target: String){
            Log.d("WEBVIEW",target)
            model!!.sendCommand(AutomationObject("bet","click_bet", arrayOf(target)))
        }
    }

    private fun onOpenBet(automationEvents: AutomationEvents) {
        val backlay = automationEvents.eventArgs[1]
        val betindex = automationEvents.eventArgs[0]
        webView!!.evaluateJavascript(betSite!!.openBetScript(backlay.toString(), betindex.toString().toInt())){
            runOnUiThread{
                masterStatus!!.text = it
            }
        }
    }

    private fun onClickBet(automationEvents: AutomationEvents) {
        val betindex = automationEvents.eventArgs[0]
        Log.d("AUTOMATION",betindex.toString())
    }

    private fun placeBet(automationEvents: AutomationEvents) {
        val stake = automationEvents.eventArgs[3]

        webView!!.evaluateJavascript(betSite!!.placeBetScript(stake.toString().toDouble())){
            runOnUiThread{
                masterStatus!!.text = it
            }
        }
    }

    private fun confirmBet() {
        webView!!.evaluateJavascript(betSite!!.comfirmBetScript()) {
            runOnUiThread {
                masterStatus!!.text = it
            }
        }
    }

    private fun startBrowser(){
        webView!!.loadUrl(betSite!!.url())
        webView!!.webViewClient = object : WebViewClient(){

            override fun onPageFinished(view: WebView?, url: String?) {
                model!!.browserLoading.value = false
                //SystemClock.sleep(5000)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                model!!.browserLoading.value = true
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                model!!.sendCommand(AutomationObject("bet","confirm_bet", arrayOf()))
            }
        }
        return true
    }
}
package com.fshangala.lambo1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import java.util.*

class SiteActivity : AppCompatActivity() {
    private var webView: WebView? = null
    private var model: LamboViewModel? = null
    private var masterStatus: TextView? = null
    private var oddStatus: TextView? = null
    var sharedPref: SharedPreferences? = null
    var toast: Toast? = null
    var betSite: GeneralBetSite? = null
    private val buttonsTimer = Timer("buttonsTimer",true)

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
        oddStatus = findViewById(R.id.odd)

        //betSite = BetSite(sharedPref!!.getString("betSite","laser247.com")!!)
        //startBrowser()

        //buttonsTimer.scheduleAtFixedRate(UpdateButton(),0,1000)

        model!!.getRequest(sharedPref!!,"/betsite/")
        model!!.apiResponse.observe(this) {
            if (it != ""){
                val betSiteList = BetSiteList(it)
                val betSiteData = betSiteList.list[sharedPref!!.getInt("betSiteIndex",0)]
                betSite = GeneralBetSite(betSiteData)
                Log.d("JSON",betSite!!.eventListenerScript())
                startBrowser()
                buttonsTimer.scheduleAtFixedRate(UpdateButton(),0,1000)
            }
        }
        model!!.apiResponseError.observe(this){
            if(it != ""){
                Snackbar.make(findViewById(R.id.parentLayout),it, Snackbar.LENGTH_INDEFINITE).
                setAction("Retry", View.OnClickListener {
                    model!!.getRequest(sharedPref!!,"/betsite/")
                }).show()
            }
        }
        model!!.connectionStatus.observe(this) {
            toast = Toast.makeText(this,it,Toast.LENGTH_SHORT)
            toast!!.show()
        }
        model!!.automationEvents.observe(this) {
            when (it.eventName) {
                "place_bet" -> {
                    placeBet()
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
        model!!.browserLoading.observe(this){ isLoading ->
            if (isLoading == true) {
                runOnUiThread {
                    masterStatus!!.text = "Loading..."
                }
            } else {
                runOnUiThread {
                    masterStatus!!.text = "Loaded!"
                }
            }
        }
        model!!.oddButtons.observe(this) {
            var currentBetIndex = model!!.currentBetIndex.value
            var jslog = model!!.jslog.value
            var stake = sharedPref!!.getString("stake","200")
            runOnUiThread {
                oddStatus!!.text = "Buttons:$it; Index:$currentBetIndex; Stake:$stake; $jslog"
            }
        }
        model!!.currentBetIndex.observe(this) {
            var oddButtons = model!!.oddButtons.value
            var jslog = model!!.jslog.value
            var stake = sharedPref!!.getString("stake","200")
            runOnUiThread {
                oddStatus!!.text = "Buttons:$oddButtons; Index:$it; Stake:$stake; $jslog"
            }
            model!!.sendCommand(AutomationObject("bet","click_bet", arrayOf(it)))
        }
        model!!.jslog.observe(this) {
            var oddButtons = model!!.oddButtons.value
            var currentBetIndex = model!!.currentBetIndex.value
            var stake = sharedPref!!.getString("stake","200")
            runOnUiThread {
                oddStatus!!.text = "Buttons:$oddButtons; Index:$currentBetIndex; Stake:$stake; $it"
            }
        }
        model!!.createConnection(sharedPref!!)
    }

    private inner class UpdateButton:TimerTask(){
        override fun run() {
            runOnUiThread {
                webView!!.evaluateJavascript(betSite!!.eventListenerScript()) {
                    //model!!.jslog.postValue(it)
                }
            }
        }
    }

    private inner class LamboJsInterface {
        @JavascriptInterface
        fun performClick(target: String){
            model!!.currentBetIndex.postValue(target)
        }
        @JavascriptInterface
        fun buttonCount(buttons: Int){
            model!!.oddButtons.postValue(buttons)
        }
        @JavascriptInterface
        fun getOdds(odds: String){
            model!!.currentBetIndexOdds.postValue(odds)
        }
    }

    private fun onClickBet(automationEvents: AutomationEvents) {
        model!!.sendCommand(AutomationObject("bet","place_bet", arrayOf()))
    }

    private fun placeBet() {
        val stake = sharedPref!!.getString("stake", "200")

        webView!!.evaluateJavascript(betSite!!.placeBetScript(stake.toString().toDouble())){
            model!!.jslog.postValue("PlaceBet")
        }
    }

    private fun confirmBet() {
        val betindex = model!!.currentBetIndex.value

        webView!!.evaluateJavascript(betSite!!.comfirmBetScript(betindex.toString().toInt())) {
            model!!.jslog.postValue("ConfirmBet")
        }
    }

    private fun startBrowser(){
        webView!!.loadUrl(betSite!!.url())
        webView!!.webViewClient = object : WebViewClient(){

            override fun onPageFinished(view: WebView?, url: String?) {
                model!!.browserLoading.value = false
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
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                model!!.sendCommand(AutomationObject("bet","place_bet", arrayOf()))
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.lambomenu,menu)

        model!!.connected.observe(this){
            if (it){
                menu.getItem(1).setIcon(R.mipmap.reset_green_round)
            } else {
                menu.getItem(1).setIcon(R.mipmap.reset_red_round)
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.preferencesBtn -> {
                openConfig()
            }

            R.id.reconnectBtn -> {
                model!!.createConnection(sharedPref!!)
            }

            R.id.reloadBrowserBtn -> {
                webView!!.reload()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openConfig(){
        val intent = Intent(this,ConfigActivity::class.java)
        startActivity(intent)
    }
}
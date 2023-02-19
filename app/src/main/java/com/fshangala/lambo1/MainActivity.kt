package com.fshangala.lambo1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    private var model: LamboViewModel? = null
    var sharedPref: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = ViewModelProvider(this)[LamboViewModel::class.java]
        sharedPref = getSharedPreferences("MySettings", MODE_PRIVATE)

        val betSiteSpinner = findViewById<Spinner>(R.id.websiteSpinner)
        val openButton = findViewById<Button>(R.id.openButton)

        model!!.getRequest(sharedPref!!,"/betsite/")
        model!!.apiResponse.observe(this) {
            if (it != ""){
                val betSiteList = BetSiteList(it)
                val betSitesAdapter: ArrayAdapter<CharSequence> = ArrayAdapter(this,android.R.layout.simple_spinner_item,betSiteList.listByName)
                betSitesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                betSiteSpinner.adapter = betSitesAdapter
                betSiteSpinner.setSelection(sharedPref!!.getInt("betSiteIndex",0))
                openButton.isEnabled = true
            }
        }
        model!!.apiResponseError.observe(this){
            if(it != ""){
                Snackbar.make(findViewById(R.id.parentLayout),it,Snackbar.LENGTH_INDEFINITE).
                    setAction("Retry",View.OnClickListener {
                        model!!.getRequest(sharedPref!!,"/betsite/")
                    }).show()
            }
        }

        val betSite = BetSite()

        //val selectedBetSite = betSitesAdapter.getPosition(sharedPref!!.getString("betSite","laser247.com"))
        //betSiteSpinner.setSelection(selectedBetSite)

        val stakeInput = findViewById<TextInputEditText>(R.id.stakeInput)
        stakeInput.setText(sharedPref!!.getString("stake","200"))

        val hostCode = findViewById<TextInputEditText>(R.id.codeInput)
        hostCode.setText(sharedPref!!.getString("hostCode","sample"))
    }

    override fun onResume() {
        super.onResume()

        model!!.getRequest(sharedPref!!,"/betsite/")
        val sharedPref = getSharedPreferences("MySettings", MODE_PRIVATE)

        val betSiteSpinner = findViewById<Spinner>(R.id.websiteSpinner)

        val stakeInput = findViewById<TextInputEditText>(R.id.stakeInput)
        stakeInput.post {
            stakeInput.setText(sharedPref.getString("stake","200"))
        }

        val hostCode = findViewById<TextInputEditText>(R.id.codeInput)
        hostCode.post {
            hostCode.setText(sharedPref.getString("hostCode","sample"))
        }
    }

    override fun onRestart() {
        super.onRestart()

        model!!.getRequest(sharedPref!!,"/betsite/")
        val sharedPref = getSharedPreferences("MySettings", MODE_PRIVATE)

        val betSiteSpinner = findViewById<Spinner>(R.id.websiteSpinner)

        val stakeInput = findViewById<TextInputEditText>(R.id.stakeInput)
        stakeInput.post {
            stakeInput.setText(sharedPref.getString("stake","200"))
        }

        val hostCode = findViewById<TextInputEditText>(R.id.codeInput)
        hostCode.post {
            hostCode.setText(sharedPref.getString("hostCode","sample"))
        }
    }
    fun savePreferences(view: View){
        val betSiteSpinner = findViewById<Spinner>(R.id.websiteSpinner)
        val hostCode = findViewById<TextInputEditText>(R.id.codeInput)
        val stakeInput = findViewById<TextInputEditText>(R.id.stakeInput)

        val sharedPref = getSharedPreferences("MySettings", MODE_PRIVATE)
        val editSharedPref = sharedPref.edit()
        editSharedPref.putString("betSite",betSiteSpinner.selectedItem.toString())
        editSharedPref.putString("hostCode",hostCode.text.toString())
        editSharedPref.putString("stake",stakeInput.text.toString())
        editSharedPref.apply()

        val jsonResponse = model!!.apiResponse.value
        if (jsonResponse != ""){
            val betSiteList = BetSiteList(jsonResponse!!)
            for (x in (0 until betSiteList.list.count())){
                if (betSiteList.listByName[x] == sharedPref!!.getString("betSite","laser247.com")){
                    editSharedPref.putInt("betSiteIndex",x)
                    editSharedPref.apply()
                    break
                }
            }
        }

        openMain()
    }

    private fun openMain(){
        val intent = Intent(this,SiteActivity::class.java)
        startActivity(intent)
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
                model!!.getRequest(sharedPref!!,"/betsite/")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openConfig(){
        val intent = Intent(this,ConfigActivity::class.java)
        startActivity(intent)
    }
}
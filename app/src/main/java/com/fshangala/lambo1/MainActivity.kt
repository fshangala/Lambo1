package com.fshangala.lambo1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("MySettings", MODE_PRIVATE)
        val betSite = BetSite()
        val betSitesAdapter: ArrayAdapter<CharSequence> = ArrayAdapter(this,android.R.layout.simple_spinner_item,betSite.sites)
        betSitesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val betSiteSpinner = findViewById<Spinner>(R.id.websiteSpinner)
        betSiteSpinner.adapter = betSitesAdapter
        val selectedBetSite = betSitesAdapter.getPosition(sharedPref!!.getString("betSite","laser247.com"))
        betSiteSpinner.setSelection(selectedBetSite)

        val stakeInput = findViewById<TextInputEditText>(R.id.stakeInput)
        stakeInput.setText(sharedPref.getString("stake","200"))

        val hostCode = findViewById<TextInputEditText>(R.id.codeInput)
        hostCode.setText(sharedPref.getString("hostCode","sample"))
    }

    override fun onResume() {
        super.onResume()

        val sharedPref = getSharedPreferences("MySettings", MODE_PRIVATE)
        val betSite = BetSite()
        val betSitesAdapter: ArrayAdapter<CharSequence> = ArrayAdapter(this,android.R.layout.simple_spinner_item,betSite.sites)
        betSitesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val betSiteSpinner = findViewById<Spinner>(R.id.websiteSpinner)
        betSiteSpinner.post {
            betSiteSpinner.adapter = betSitesAdapter
            val selectedBetSite = betSitesAdapter.getPosition(sharedPref!!.getString("betSite","laser247.com"))
            betSiteSpinner.setSelection(selectedBetSite)
        }

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

        val sharedPref = getSharedPreferences("MySettings", MODE_PRIVATE)
        val betSite = BetSite()
        val betSitesAdapter: ArrayAdapter<CharSequence> = ArrayAdapter(this,android.R.layout.simple_spinner_item,betSite.sites)
        betSitesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val betSiteSpinner = findViewById<Spinner>(R.id.websiteSpinner)
        betSiteSpinner.post {
            betSiteSpinner.adapter = betSitesAdapter
            val selectedBetSite = betSitesAdapter.getPosition(sharedPref!!.getString("betSite","laser247.com"))
            betSiteSpinner.setSelection(selectedBetSite)
        }

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

        openMain()
    }

    private fun openMain(){
        val intent = Intent(this,SiteActivity::class.java)
        startActivity(intent)
    }


}
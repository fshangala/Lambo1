package com.fshangala.lambo1

import org.json.JSONObject

class BetSiteData(json:String): JSONObject(json) {
    val id = this.optInt("id")
    val sitename = this.optString("name")
    val url = this.optString("url")
    val bet_buttons = this.optString("bet_buttons")
    val input_elements = this.optString("input_elements")
    val odds_input = this.optInt("odds_input")
    val stake_input = this.optInt("stake_input")
    val alt_stake_input = this.optInt("alt_stake_input")
    val betslip_buttons = this.optString("betslip_buttons")
    val confirm_button = this.optInt("confirm_button")
}
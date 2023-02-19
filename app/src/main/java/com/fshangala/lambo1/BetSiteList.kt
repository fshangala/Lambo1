package com.fshangala.lambo1

import org.json.JSONArray

class BetSiteList(json:String):JSONArray(json) {
    val list:List<BetSiteData> = (0 until length()).map {
        BetSiteData(getString(it))
    }
    val listByName:List<String> = (0 until length()).map {
        BetSiteData(getString(it)).sitename
    }
}
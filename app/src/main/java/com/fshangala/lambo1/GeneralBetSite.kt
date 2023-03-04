package com.fshangala.lambo1

class GeneralBetSite(val betSiteData: BetSiteData) {
    fun url():String{
        return betSiteData.url
    }
    fun initScript():String{
        return "var bet_buttons = '${betSiteData.bet_buttons}';\n" +
                "var input_elements = '${betSiteData.input_elements}';\n" +
                "var betslip_buttons = '${betSiteData.betslip_buttons}';\n" +
                "var odds_input = ${betSiteData.odds_input};\n" +
                "var stake_input = ${betSiteData.stake_input};\n" +
                "var alt_stake_input = ${betSiteData.alt_stake_input};\n" +
                "var confirm_button = ${betSiteData.confirm_button};\n"
    }
    fun openBetScript(betIndex: Int):String{
        return initScript() +
                "var betIndex = $betIndex;\n" +
                "var targetElements = document.querySelectorAll(bet_buttons);\n" +
                "targetElements[betIndex].click();"
    }
    fun placeBetScript(stake: Double, odds: Double = 0.0): String {
        return initScript() +
                "var stake = $stake;\n" +
                "var inputElements = document.querySelectorAll(input_elements)\n" +
                "if(inputElements.length == 2){\n" +
                "    inputElements[stake_input].value = stake;\n" +
                "    inputElements[stake_input].dispatchEvent(new Event('input', { bubbles: true}));\n" +
                "    inputElements[stake_input].dispatchEvent(new Event('change', { bubbles: true}));\n" +
                "} else {\n" +
                "    inputElements[alt_stake_input].value = stake;\n" +
                "    inputElements[alt_stake_input].dispatchEvent(new Event('input', { bubbles: true}));\n" +
                "    inputElements[alt_stake_input].dispatchEvent(new Event('change', { bubbles: true}));\n" +
                "}"
    }
    fun comfirmBetScript(): String {
        return initScript() +
                "document.querySelectorAll(betslip_buttons)[confirm_button].click();"
    }
    fun eventListenerScript(): String {
        return initScript() +
                "var targetElements = document.querySelectorAll(bet_buttons);\n" +
                "function getSelected(a){\n" +
                "    if(a.hasAttribute(\"lambolistenning\")){\n" +
                "        return a;\n" +
                "    } else {\n" +
                "        return getSelected(a.parentElement);\n" +
                "    }\n" +
                "}\n" +
                "function clickEventListener(event){\n" +
                "    checkElements();\n" +
                "    var a = event.target;\n" +
                "    var selected = getSelected(event.target);\n" +
                "    //console.log(selected.getAttribute(\"lambolistenning\"));\n" +
                "    window.lambo.buttonCount(targetElements.length);\n" +
                "    window.lambo.performClick(selected.getAttribute(\"lambolistenning\"));\n" +
                "}\n" +
                "function checkElements(){\n" +
                "    targetElements = document.querySelectorAll(bet_buttons)\n" +
                "    targetElements.forEach((item,index,arr)=>{\n" +
                "        if(item.hasAttribute(\"lambolistenning\")){\n" +
                "            //console.log(item.innerText);\n" +
                "        } else {\n" +
                "            item.addEventListener(\"click\",clickEventListener);\n" +
                "        }\n" +
                "        item.setAttribute(\"lambolistenning\",index);\n" +
                "    });\n" +
                "    window.lambo.buttonCount(targetElements.length);\n" +
                "}\n" +
                "checkElements();"
    }
}
package com.fshangala.lambo1

class GeneralBetSite(val betSiteData: BetSiteData) {
    fun url():String{
        return betSiteData.url
    }
    fun placeBetScript(stake: Double, odds: Double = 0.0): String {
        return "var inputElements = document.querySelectorAll('${betSiteData.input_elements}');\n" +
                "if(inputElements.length == 2){\n" +
                "    inputElements[${betSiteData.stake_input}].value = $stake\n" +
                "    inputElements[${betSiteData.stake_input}].dispatchEvent(new Event('input', { bubbles: true}));\n" +
                "    inputElements[${betSiteData.stake_input}].dispatchEvent(new Event('change', { bubbles: true}));\n" +
                "} else {\n" +
                "    inputElements[${betSiteData.alt_stake_input}].value = $stake\n" +
                "    inputElements[${betSiteData.alt_stake_input}].dispatchEvent(new Event('input', { bubbles: true}));\n" +
                "    inputElements[${betSiteData.alt_stake_input}].dispatchEvent(new Event('change', { bubbles: true}));\n" +
                "}"
    }
    fun comfirmBetScript(betIndex: Int): String {
        return "document.querySelectorAll('${betSiteData.betslip_buttons}')[${betSiteData.confirm_button}].click();"
    }
    fun eventListenerScript(): String {
        return "var targetElements = document.querySelectorAll('${betSiteData.bet_buttons}');\n" +
                "function clickEventListener(event){\n" +
                "    checkElements();\n" +
                "    var a = event.target;\n" +
                "    var selected = null;\n" +
                "    if(a.className === \"odd-button__price\" || a.className === \"odd-button__volume\"){\n" +
                "        selected = a.parentElement.parentElement;\n" +
                "    } else {\n" +
                "        selected = event.target;\n" +
                "    }\n" +
                "    //console.log(selected.getAttribute(\"lambolistenning\"));\n" +
                "    window.lambo.buttonCount(targetElements.length);\n" +
                "    window.lambo.performClick(selected.getAttribute(\"lambolistenning\"));\n" +
                "}\n" +
                "function checkElements(){\n" +
                "    targetElements = document.querySelectorAll('${betSiteData.bet_buttons}');\n" +
                "    targetElements.forEach((item,index,arr)=>{\n" +
                "        if(item.hasAttribute(\"lambolistenning\")){\n" +
                "            console.log(item.innerText);\n" +
                "        } else {\n" +
                "            item.addEventListener(\"click\",clickEventListener);\n" +
                "        }\n" +
                "        item.setAttribute(\"lambolistenning\",index);\n" +
                "    });\n" +
                "    window.lambo.buttonCount(targetElements.length);\n" +
                "}\n" +
                "checkElements();window.alert('working');"
    }
}
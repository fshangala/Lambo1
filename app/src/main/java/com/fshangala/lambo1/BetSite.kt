package com.fshangala.lambo1

class BetSite(val name: String = "laser247.com") {
    val sites = arrayOf<String>(
        "laser247.com",
        "lotus365.com"
    )
    fun url():String{
        when(name){
            "laser247.com" -> {
                return "https://www.laser247.com"
            }
            "lotus365.com" -> {
                return "https://www.lotus365.com"
            }
            else -> {
                return "https://www.laser247.com"
            }
        }
    }
    fun clickBetScript(betIndex: Int, stake: String): String {
        when(name){
            "laser247.com" -> {
                var theScript = "document.querySelectorAll(\".odds_body button\")[$betIndex].click();\n"
                theScript += placeBetScript(stake.toString().toDouble())
                return  theScript
            }
            else -> {
                return "document.querySelectorAll(\".odds_body button\")[$betIndex].click()"
            }
        }
    }
    fun placeBetScript(stake: Double, odds: Double = 0.0): String {
        when(name){
            "laser247.com" -> {
                var setodds = ""
                if (odds != 0.0) {
                    setodds = "inputElements[0].value = $odds\n" +
                            "inputElements[0].dispatchEvent(new Event('input', { bubbles: true}));\n" +
                            "inputElements[0].dispatchEvent(new Event('change', { bubbles: true}));"
                }
                return "var inputElements = document.querySelectorAll(\".odds_body app-bet-slip input\")\n" +
                        "inputElements[2].value = $stake\n" +
                        "inputElements[2].dispatchEvent(new Event('input', { bubbles: true}));\n" +
                        "inputElements[2].dispatchEvent(new Event('change', { bubbles: true}));\n" +
                        setodds
            }
            "lotus365.com" -> {
                return "var inputElements = document.querySelectorAll(\".SportEvent__market ion-input input\")\n" +
                        "if(inputElements.length == 2){\n" +
                        "    inputElements[1].value = $stake\n" +
                        "    inputElements[1].dispatchEvent(new Event('input', { bubbles: true}));\n" +
                        "    inputElements[1].dispatchEvent(new Event('change', { bubbles: true}));\n" +
                        "} else {\n" +
                        "    inputElements[0].value = $stake\n" +
                        "    inputElements[0].dispatchEvent(new Event('input', { bubbles: true}));\n" +
                        "    inputElements[0].dispatchEvent(new Event('change', { bubbles: true}));\n" +
                        "}"
            }
            else -> {
                var setodds = ""
                if (odds != 0.0) {
                    setodds = "inputElements[0].value = $odds\n" +
                            "inputElements[0].dispatchEvent(new Event('input', { bubbles: true}));\n" +
                            "inputElements[0].dispatchEvent(new Event('change', { bubbles: true}));"
                }
                return "var inputElements = document.querySelectorAll(\".odds_body app-bet-slip input\")\n" +
                        "inputElements[2].value = $stake\n" +
                        "inputElements[2].dispatchEvent(new Event('input', { bubbles: true}));\n" +
                        "inputElements[2].dispatchEvent(new Event('change', { bubbles: true}));\n" +
                        setodds
            }
        }
    }
    fun comfirmBetScript(betIndex: Int): String {
        when(name){
            "laser247.com" -> {
                return "document.querySelectorAll(\".odds_body .btn-betplace\")[1].click();"
            }
            "lotus365.com" -> {
                return "document.querySelector(\".BetPlacing__btn--place\").click();"
            }
            else -> {
                return "document.querySelectorAll(\".odds_body .btn-betplace\")[1].click();"
            }
        }
    }
    fun eventListenerScript(): String {
        when(name){
            "laser247.com" -> {
                return "var targetElements = document.querySelectorAll(\".odds_body button\");\n" +
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
                        "    targetElements = document.querySelectorAll(\".odds_body button\")\n" +
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
            "lotus365.com" -> {
                return "var targetElements = document.querySelectorAll(\".SportEvent__market .odd-button\");\n" +
                        "function clickEventListener(event){\n" +
                        "    checkElements();\n" +
                        "    var a = event.target;\n" +
                        "    var selected = null;\n" +
                        "    if(a.className === \"odd-button__price\" || a.className === \"odd-button__volume\"){\n" +
                        "        selected = a.parentElement.parentElement;\n" +
                        "    } else {\n" +
                        "        selected = event.target;\n" +
                        "    }\n" +
                        "    console.log(selected.getAttribute(\"lambolistenning\"));\n" +
                        "    window.lambo.buttonCount(targetElements.length);\n" +
                        "    window.lambo.performClick(selected.getAttribute(\"lambolistenning\"));\n" +
                        "    window.lambo.getOdds(selected.firstChild.firstChild.innerText);\n" +
                        "}\n" +
                        "function checkElements(){\n" +
                        "    targetElements = document.querySelectorAll(\".SportEvent__market .odd-button\")\n" +
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
            else -> {
                return "var targetElements = document.querySelectorAll(\".odds_body button\");\n" +
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
                        "    targetElements = document.querySelectorAll(\".odds_body button\")\n" +
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
    }
}
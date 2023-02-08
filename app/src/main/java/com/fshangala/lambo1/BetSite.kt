package com.fshangala.lambo1

import android.content.Context

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
                    setodds = "\nnativeInputValueSetter.call(inputOdds, '$odds');\n" +
                            "inputOdds.dispatchEvent(ev1);\n" +
                            "inputOdds.dispatchEvent(ev2);"
                }
                return "var input = document.querySelectorAll(\"app-bet-slip input\")[2];\n" +
                        "var inputOdds = document.querySelectorAll(\"app-bet-slip input\")[0];\n" +

                        "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, \"value\").set;\n" +
                        "var ev1 = new Event('input', { bubbles: true});\n" +
                        "var ev2 = new Event('change', { bubbles: true});\n" +

                        "nativeInputValueSetter.call(input, '$stake');\n" +
                        "input.dispatchEvent(ev1);\n" +
                        "input.dispatchEvent(ev2);" +
                        setodds
            }
            "lotus365.com" -> {
                var setodds = ""
                if (odds != 0.0) {
                    setodds = "\nnativeInputValueSetter.call(inputOdds, '$odds');\n" +
                            "inputOdds.dispatchEvent(ev1);\n" +
                            "inputOdds.dispatchEvent(ev2);"
                }
                return "var betRows = document.querySelectorAll(\".SportEvent__market\")\n" +
                        "var inputElements = document.querySelectorAll(\"ion-input input\")\n" +
                        "inputElements[1].value = $stake\n" +
                        "inputElements[1].dispatchEvent(new Event('input', { bubbles: true}));\n" +
                        "inputElements[1].dispatchEvent(new Event('change', { bubbles: true}));"
            }
            else -> {
                var setodds = ""
                if (odds != 0.0) {
                    setodds = "\nnativeInputValueSetter.call(inputOdds, '$odds');\n" +
                            "inputOdds.dispatchEvent(ev1);\n" +
                            "inputOdds.dispatchEvent(ev2);"
                }
                return "var input = document.querySelectorAll(\"app-bet-slip input\")[2];\n" +
                        "var inputOdds = document.querySelectorAll(\"app-bet-slip input\")[0];\n" +

                        "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, \"value\").set;\n" +
                        "var ev1 = new Event('input', { bubbles: true});\n" +
                        "var ev2 = new Event('change', { bubbles: true});\n" +

                        "nativeInputValueSetter.call(input, '$stake');\n" +
                        "input.dispatchEvent(ev1);\n" +
                        "input.dispatchEvent(ev2);" +
                        setodds
            }
        }
    }
    fun comfirmBetScript(betIndex: Int): String {
        when(name){
            "laser247.com" -> {
                return "document.querySelector(\"app-bet-slip button.btn-betplace\").click();"
            }
            "lotus365.com" -> {
                return "document.querySelector(\".BetPlacing__btn--place\").click();"
            }
            else -> {
                return "document.querySelector(\"app-bet-slip button.btn-betplace\").click();"
            }
        }
    }
    fun eventListenerScript(): String {
        when(name){
            "laser247.com" -> {
                return "function checkElements(){\n" +
                        "    setTimeout(()=>{\n" +
                        "        targetElements = document.querySelectorAll(\".odds_body button\")\n" +
                        "        if (targetElements.length > 0){\n" +
                        "            targetElements.forEach((item)=>{\n" +
                        "                item.addEventListener(\"click\",(event)=>{\n" +
                        "                var itemb = event.target\n" +
                        "                targetElements.forEach((itema,index,arr)=>{\n" +
                        "                    if(itema==itemb){\n" +
                        "                        window.lambo.performClick(index,itemb.innerText)\n" +
                        "                    }\n" +
                        "                })\n" +
                        "            })})\n" +
                        "        } else {\n" +
                        "            checkElements()\n" +
                        "        }\n" +
                        "    },1000)\n" +
                        "}\n" +
                        "checkElements()"
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
                return "function checkElements(){\n" +
                        "    setTimeout(()=>{\n" +
                        "        targetElements = document.querySelectorAll(\".odds_body button\")\n" +
                        "        if (targetElements.length > 0){\n" +
                        "            targetElements.forEach((item)=>{\n" +
                        "                item.addEventListener(\"click\",(event)=>{\n" +
                        "                var itemb = event.target\n" +
                        "                targetElements.forEach((itema,index,arr)=>{\n" +
                        "                    if(itema==itemb){\n" +
                        "                        window.lambo.performClick(index)\n" +
                        "                    }\n" +
                        "                })\n" +
                        "            })})\n" +
                        "        } else {\n" +
                        "            checkElements()\n" +
                        "        }\n" +
                        "    },1000)\n" +
                        "}\n" +
                        "checkElements()"
            }
        }
    }
    fun manualEventListenerScript(): String {
        when(name){
            "laser247.com" -> {
                return "function checkElements(){\n" +
                        "    setTimeout(()=>{\n" +
                        "        targetElements = document.querySelectorAll(\".odds_body button\")\n" +
                        "        if (targetElements.length > 0){\n" +
                        "            targetElements.forEach((item)=>{\n" +
                        "                item.addEventListener(\"click\",(event)=>{\n" +
                        "                var itemb = event.target\n" +
                        "                targetElements.forEach((itema,index,arr)=>{\n" +
                        "                    if(itema==itemb){\n" +
                        "                        window.lambo.performClick(index,itemb.innerText)\n" +
                        "                    }\n" +
                        "                })\n" +
                        "            })})\n" +
                        "        } else {\n" +
                        "            checkElements()\n" +
                        "        }\n" +
                        "    },1000)\n" +
                        "}\n" +
                        "checkElements()"
            }
            "lotus365.com" -> {
                return "function clickEventListener(event){\n" +
                        "    targetElements = document.querySelectorAll(\".SportEvent__market .odd-button\")\n" +
                        "    var selected = null;\n" +
                        "    targetElements.forEach((item,index,arr)=>{\n" +
                        "        if(item===event.target){\n" +
                        "            selected = index;\n" +
                        "        }\n" +
                        "    });\n" +
                        "    window.lambo.buttonCount(targetElements.length);\n" +
                        "    window.lambo.performClick(selected);\n" +
                        "}\n" +
                        "function checkElements(){\n" +
                        "    var targetElements = document.querySelectorAll(\".SportEvent__market .odd-button\")\n" +
                        "    targetElements.forEach((item)=>{\n" +
                        "        if(Boolean(item.hasAttribute(\"lambolistenning\"))){\n" +
                        "            console.log(item.innerText);\n" +
                        "        } else {\n" +
                        "            item.addEventListener(\"click\",clickEventListener);\n" +
                        "            item.setAttribute(\"lambolistenning\",\"true\");\n" +
                        "        }\n" +
                        "    });\n" +
                        "    window.lambo.buttonCount(targetElements.length);\n" +
                        "}\n" +
                        "checkElements();"
            }
            else -> {
                return "function checkElements(){\n" +
                        "    setTimeout(()=>{\n" +
                        "        targetElements = document.querySelectorAll(\".odds_body button\")\n" +
                        "        if (targetElements.length > 0){\n" +
                        "            targetElements.forEach((item)=>{\n" +
                        "                item.addEventListener(\"click\",(event)=>{\n" +
                        "                var itemb = event.target\n" +
                        "                targetElements.forEach((itema,index,arr)=>{\n" +
                        "                    if(itema==itemb){\n" +
                        "                        window.lambo.performClick(index)\n" +
                        "                    }\n" +
                        "                })\n" +
                        "            })})\n" +
                        "        } else {\n" +
                        "            checkElements()\n" +
                        "        }\n" +
                        "    },1000)\n" +
                        "}\n" +
                        "checkElements()"
            }
        }
    }
}
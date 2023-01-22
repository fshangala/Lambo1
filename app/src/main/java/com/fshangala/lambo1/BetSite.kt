package com.fshangala.lambo1

class BetSite(val name: String = "laser247.com") {
    val sites = arrayOf<String>(
        "laser247.com",
    )
    fun url():String{
        when(name){
            "laser247.com" -> {
                return "https://laser247.com"
            }
            else -> {
                return "https://laser247.com"
            }
        }
    }
    fun openBetScript(backlay: String, betIndex: Int): String {
        when(name){
            "laser247.com" -> {
                var bl = 0
                if (backlay == "back"){
                    bl = 2
                }
                return "document.querySelectorAll(\".odds_body\")[$betIndex].querySelectorAll(\"button.$backlay\")[$bl].click()"
            }
            else -> {
                var bl = 0
                if (backlay == "back"){
                    bl = 2
                }
                return "document.querySelectorAll(\".odds_body\")[$betIndex].querySelectorAll(\"button.$backlay\")[$bl].click()"
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
    fun comfirmBetScript(): String {
        when(name){
            "laser247.com" -> {
                return "document.querySelector(\"app-bet-slip button.btn-betplace\").click();"
            }
            else -> {
                return "document.querySelector(\"app-bet-slip button.btn-betplace\").click();"
            }
        }
    }
}
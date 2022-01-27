package com.wegrzyn.marcin  .hexcalc

import android.util.Log
import androidx.lifecycle.MutableLiveData

enum class TypeNumber(number: String){
    HEX("HEX"),
    DEC("DEC"),
    BIN("BIN")
}
object Repo {

    val TAG = "TestTAG"
    val hexString = MutableLiveData<String>("0")
    val decString = MutableLiveData<String>("0")
    val binString = MutableLiveData<String>("0")
    val dec = MutableLiveData<Long>(0)

    val typeSelected =  MutableLiveData<TypeNumber>(TypeNumber.DEC)

    init {
        dec.observeForever({
          LongToString(it)
        })
    }

    fun LongToString(long: Long){
        hexString.value = long.toString(16).uppercase()
        decString.value = long.toString()
        binString.value = long.toString(2)
    }

    fun charDecoder(s: String){

        when(s){
            "/" -> div()
            "+" -> plus()
            "-" -> minus()
            "x" -> multiplication()
            "=" -> result()
            "<-" -> delChar(typeSelected.value?:TypeNumber.DEC)
            " " -> notDefine()
            else -> readHex(s, typeSelected.value?:TypeNumber.DEC)
        }
    }

    private fun delChar(numberType: TypeNumber) {

        when(numberType){
            TypeNumber.HEX -> {
                if(hexString.value?.length ?: 0 >1){
                    hexString.value = hexString.value?.dropLast(1)
                    val value = hexString.value
                    dec.value = value?.toLong(16)
                } else clearDisplay()

            }
            TypeNumber.DEC ->{
                if(decString.value?.length ?: 0 >1) {
                    decString.value = decString.value?.dropLast(1)
                    val value = decString.value
                    dec.value = value?.toLong()
                } else clearDisplay()
            }
            else -> {
                // TODO:
            }
        }
    }

    fun readHex(s: String, numberType: TypeNumber){
        when{
            numberType==TypeNumber.HEX && hexString.value?.length ?:0 < 15 ->{
                if(hexString.value ?: "0" == "0") hexString.value = s
                else hexString.value +=s

                dec.value = hexString.value?.toLong(16)
            }
            numberType==TypeNumber.DEC && decString.value?.length ?:0 < 18 -> {
                if (decString.value ?: "0" == "0") decString.value = s
                else decString.value += s

                dec.value = decString.value?.toLong()
            }
        }
    }



    // TODO: 16.01.2022 cleardisplay

    fun clearDisplay(){
        dec.value = 0
        Log.d(TAG, "clear display")
    }



    fun spaceDiv(s: String, div: Int): String{
        val sb = StringBuilder()
        var i = 0

        s.reversed().forEach {
            if(i==div){
                sb.append("  ")
                i = 0
            }
            i++
            sb.append(it)
        }
        return sb.reverse().toString()
    }

    private fun result() {

    }

    private fun notDefine() {

    }

    fun multiplication() {

    }

   fun minus() {

    }

    fun div(){
        // TODO: 02.01.2022
    }
    fun plus(){
        // TODO: 02.01.2022

    }
}
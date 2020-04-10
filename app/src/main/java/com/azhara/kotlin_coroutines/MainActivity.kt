package com.azhara.kotlin_coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val RESULT1 = "RESULT#1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
            /* Coroutine memiliki 3 Scope IO,Main, dan Default
                IO: Berjalan pada Input Output seperti melakukan request api dari ke server atau
                    melakukan request pada database local seperti SQLite
                Main: Berjalan pada MainThread (thread utama) seperti berinteraksi dengan UI
                Default: Digunakan untuk proses yang berat maka Corountine akan menentukan dimana corountine itu berjalan
                    secara otomatis
             */
            CoroutineScope(Dispatchers.IO).launch {
                fakeApiRequest()
            }
        }
    }

    private suspend fun fakeApiRequest(){
        val result1 = getRequesApi1()
        println("debug: $result1")
        setTextOnMainThread(result1)
    }

    private suspend fun getRequesApi1(): String{
        logThread("getRequesApi1")
        delay(1000)
        return RESULT1
    }

    private fun setText(input: String){
        val inputText = textview.text.toString() + "\n$input"
        textview.text = inputText
    }

    private suspend fun setTextOnMainThread(result: String){
        /*Mengganti thread yang sebelumnya berjalan di scope IO dan dialihkan ke scope Main
        untuk memasukkan data ke dalam tampilan UI*/
        withContext(Dispatchers.Main){
            logThread("setTextOnMainThread")
            setText(result)
        }
    }

    private fun logThread(methodName: String){
        println("debug: $methodName : Thread: ${Thread.currentThread().name}")
    }

}

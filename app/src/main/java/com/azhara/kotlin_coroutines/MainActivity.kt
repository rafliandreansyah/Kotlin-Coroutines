package com.azhara.kotlin_coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val RESULT1 = "RESULT#1"
    private val RESULT2 = "RESULT#2"

    private val TIMEOUT = 1900L

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

            // withTimeOutOrNull atau withTimeOut melakukan pembatasan waktu pada suatu proses coroutines
            // withTimeOutOrNull akan mengembalikan null jika waktu timeout terpenuhi
            val job = withTimeoutOrNull(TIMEOUT){
                val result1 = getRequesApi1()
                println("debug: $result1")
                setTextOnMainThread(result1)

                // Proses ini akan dijalankan ketika pemanggilan proses getRequesApi2 selesai dijalankan
                val result2 = getRequestApi2()
                println("debug: $result2")
                setTextOnMainThread(result2)
            }

            if (job == null){
                val alertTimeOut = "Cancelling job... Job took longer than $TIMEOUT ms"
                println("debug: $alertTimeOut")
                setTextOnMainThread(alertTimeOut)
            }


    }

    private suspend fun getRequesApi1(): String{
        logThread("getRequesApi1")
        delay(1000)
        return RESULT1
    }

    private suspend fun getRequestApi2(): String{
        logThread("getRequestApi2")
        delay(1000)
        return RESULT2
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

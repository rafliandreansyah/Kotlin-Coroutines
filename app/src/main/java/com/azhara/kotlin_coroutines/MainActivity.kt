package com.azhara.kotlin_coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    private val RESULT1 = "RESULT#1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private suspend fun getRequesApi1(): String{
        logThread("getRequesApi1")
        delay(1000)
        return RESULT1
    }

    private fun logThread(methodName: String){
        println("debug: $methodName : ${Thread.currentThread().name}")
    }

}

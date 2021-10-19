package com.gamatechno.chatobase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gamatechno.chato.sdk.module.core.ChatoBaseApplication

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
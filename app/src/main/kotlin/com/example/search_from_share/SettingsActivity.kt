package com.example.search_from_share

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val textView = TextView(this)
        textView.text = "Search from Share Settings"
        setContentView(textView)
    }
}

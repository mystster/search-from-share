package com.example.search_from_share

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import java.net.URLEncoder

class CustomTabActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Intent.ACTION_SEND == intent.action && intent.type == "text/plain") {
            handleSharedText(intent)
        }

        finish()
    }

    private fun handleSharedText(intent: Intent) {
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return
        if (sharedText.isBlank()) return

        // Remove URLs
        val urlRegex = Regex("https?://\\S+")
        var cleanText = urlRegex.replace(sharedText, "").trim()

        // Remove surrounding quotes
        if (cleanText.startsWith("\"") && cleanText.endsWith("\"") && cleanText.length >= 2) {
            cleanText = cleanText.substring(1, cleanText.length - 1).trim()
        }

        if (cleanText.isBlank()) return

        val query = URLEncoder.encode(cleanText, "UTF-8")
        val searchUrl = "https://www.google.com/search?q=$query"

        try {
            val customTabsIntent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setUrlBarHidingEnabled(true)
                .build()
            
            customTabsIntent.launchUrl(this, Uri.parse(searchUrl))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

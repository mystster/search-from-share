package com.example.search_from_share

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent

class CustomTabActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CustomTabActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Intent.ACTION_SEND == intent.action && intent.type == "text/plain") {
            handleSharedText(intent)
        }

        finish()
    }

    private fun handleSharedText(intent: Intent) {
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        val searchUrl = sharedText.toSearchUrl()

        if (searchUrl == null) {
            android.widget.Toast.makeText(
                            this,
                            R.string.toast_search_skipped,
                            android.widget.Toast.LENGTH_LONG
                    )
                    .show()
            return
        }

        try {
            val customTabsIntent =
                    CustomTabsIntent.Builder()
                            .setShowTitle(true)
                            .setUrlBarHidingEnabled(true)
                            .build()

            customTabsIntent.launchUrl(this, Uri.parse(searchUrl))
        } catch (e: Exception) {
            Log.e(TAG, "Failed to launch custom tab for URL: $searchUrl", e)
        }
    }
}

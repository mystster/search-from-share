package com.example.search_from_share

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class ShareActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ShareActivity"
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
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl))

            // Verify there's a browser to handle this
            if (browserIntent.resolveActivity(packageManager) != null) {
                startActivity(browserIntent)
            } else {
                Log.w(TAG, "No browser app found to handle the intent.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to launch browser for URL: $searchUrl", e)
        }
    }
}

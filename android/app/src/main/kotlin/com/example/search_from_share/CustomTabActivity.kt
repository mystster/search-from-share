package com.example.search_from_share

import io.flutter.embedding.android.FlutterActivity

class CustomTabActivity : FlutterActivity() {
    override fun getDartEntrypointFunctionName(): String {
        return "mainForCustomTabs"
    }
}

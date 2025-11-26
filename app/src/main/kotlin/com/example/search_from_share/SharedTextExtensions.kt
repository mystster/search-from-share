package com.example.search_from_share

import java.net.URLEncoder

private const val GOOGLE_SEARCH_BASE_URL = "https://www.google.com/search?q="

/**
 * Processes the shared text to generate a Google search URL.
 * Removes URLs and surrounding quotes from the text.
 * Returns null if the processed text is blank.
 */
fun String?.toSearchUrl(): String? {
    if (this == null || this.isBlank()) return null

    // Remove URLs
    val urlRegex = Regex("https?://\\S+")
    var cleanText = urlRegex.replace(this, "").trim()

    // Remove surrounding quotes
    if (cleanText.startsWith("\"") && cleanText.endsWith("\"") && cleanText.length >= 2) {
        cleanText = cleanText.substring(1, cleanText.length - 1).trim()
    }

    if (cleanText.isBlank()) return null

    val query = URLEncoder.encode(cleanText, "UTF-8")
    return "$GOOGLE_SEARCH_BASE_URL$query"
}

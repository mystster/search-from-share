package com.example.search_from_share

import java.net.URLEncoder

private const val GOOGLE_SEARCH_BASE_URL = "https://www.google.com/search?q="
// For performance, this Regex should be a top-level constant.
private val URL_REGEX = Regex("https?://\\S+")

/**
 * Processes the shared text to generate a Google search URL. Removes URLs and surrounding quotes
 * from the text. Returns null if the processed text is blank.
 */
fun String?.toSearchUrl(): String? {
    if (this.isNullOrBlank()) return null

    val cleanText = URL_REGEX.replace(this, "").trim().removeSurrounding("\"").trim()

    if (cleanText.isBlank()) return null

    val query = URLEncoder.encode(cleanText, "UTF-8")
    return "$GOOGLE_SEARCH_BASE_URL$query"
}

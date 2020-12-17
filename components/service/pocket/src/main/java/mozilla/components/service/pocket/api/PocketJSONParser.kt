/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.api

import androidx.annotation.VisibleForTesting
import mozilla.components.service.pocket.logger
import mozilla.components.support.ktx.android.org.json.mapNotNull
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Holds functions that parse the JSON returned by the Pocket API and converts them to more usable Kotlin types.
 */
internal class PocketJSONParser internal constructor() {
    /**
     * @return The articles, removing entries that are invalid, or null on error; the list will never be empty.
     */
    fun jsonToGlobalArticlesRecommendation(jsonStr: String): List<PocketGlobalArticleRecommendation>? = try {
        val rawJSON = JSONObject(jsonStr)
        val articlesJSON = rawJSON.getJSONArray(KEY_ARRAY_ITEMS)
        val articles = articlesJSON.mapNotNull(JSONArray::getJSONObject) { jsonToGlobalArticlesRecommendation(it) }

        // We return null, rather than the empty list, because devs might forget to check an empty list.
        if (articles.isNotEmpty()) articles else null
    } catch (e: JSONException) {
        logger.warn("invalid JSON from Pocket server", e)
        null
    }

    private fun jsonToGlobalArticlesRecommendation(jsonObj: JSONObject): PocketGlobalArticleRecommendation? = try {
        PocketGlobalArticleRecommendation(
            id = jsonObj.getLong("id"),
            url = jsonObj.getString("url"),
            domain = jsonObj.getString("domain"),
            title = jsonObj.getString("title"),
            excerpt = jsonObj.getString("excerpt"),
            imageSrc = jsonObj.getString("image_src"),
            publishedTimestamp = jsonObj.getString("published_timestamp"),
            engagement = jsonObj.getString("engagement"),
            dedupeUrl = jsonObj.getString("dedupe_url"),
            sortId = jsonObj.getInt("sort_id")
        )
    } catch (e: JSONException) {
        null
    }

    companion object {
        @VisibleForTesting const val KEY_ARRAY_ITEMS = "list"

        /**
         * Returns a new instance of [PocketJSONParser].
         */
        fun newInstance(): PocketJSONParser {
            return PocketJSONParser()
        }
    }
}

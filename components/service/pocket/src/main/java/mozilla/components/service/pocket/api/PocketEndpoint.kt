/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.api

import androidx.annotation.WorkerThread
import mozilla.components.concept.fetch.Client
import mozilla.components.lib.fetch.httpurlconnection.HttpURLConnectionClient
import mozilla.components.service.pocket.api.PocketEndpoint.Companion.newInstance

/**
 * Makes requests to the Pocket API and returns the requested data.
 *
 * @see [newInstance] to retrieve an instance.
 */
internal class PocketEndpoint internal constructor(
    private val rawEndpoint: PocketEndpointRaw,
    private val jsonParser: PocketJSONParser
) {

    /**
     * Gets a response, filled with the Pocket global articles recommendations from the Pocket API server on success.
     *
     * If the API returns unexpectedly formatted results, these entries will be omitted and the rest of the items are
     * returned.
     *
     * @return a [PocketResponse.Success] with the Pocket global articles recommendations (the list will never be empty)
     * or, on error, a [PocketResponse.Failure].
     */
    @WorkerThread
    fun getTopStories(
        count: Int,
        locale: String
    ): PocketResponse<List<PocketGlobalArticleRecommendation>> {
        val response = rawEndpoint.getGlobalArticlesRecommandations(
            count,
            locale
        )
        val articles = response?.let { jsonParser.jsonToGlobalArticlesRecommendation(it) }
        return PocketResponse.wrap(articles)
    }

    companion object {
        /**
         * Returns a new instance of [PocketEndpoint].
         *
         * @param pocketApiKey the API key for Pocket network requests.
         * @param client the HTTP client to use for network requests.
         *
         * @throws IllegalArgumentException if the provided API key is deemed invalid.
         */
        fun newInstance(
            pocketApiKey: String,
            client: Client = HttpURLConnectionClient()
        ): PocketEndpoint {
            val rawEndpoint = PocketEndpointRaw.newInstance(
                pocketApiKey,
                client
            )
            return PocketEndpoint(
                rawEndpoint,
                PocketJSONParser()
            )
        }
    }
}

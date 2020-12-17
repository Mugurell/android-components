/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.api

import android.net.Uri
import androidx.annotation.WorkerThread
import mozilla.components.concept.fetch.Client
import mozilla.components.concept.fetch.Request
import mozilla.components.service.pocket.DEFAULT_ARTICLES_COUNT
import mozilla.components.service.pocket.DEFAULT_ARTICLES_LOCALE
import mozilla.components.service.pocket.api.Arguments.assertApiKeyHasValidStructure
import mozilla.components.service.pocket.api.Arguments.assertIsNotBlank
import mozilla.components.service.pocket.api.PocketEndpointRaw.Companion.newInstance
import mozilla.components.service.pocket.api.ext.fetchBodyOrNull

/**
 * Make requests to the Pocket endpoint and returns the raw JSON data: this class is intended to be very dumb.
 *
 * @see [PocketEndpoint], which wraps this to make it more practical.
 * @see [newInstance] to retrieve an instance.
 */
internal class PocketEndpointRaw internal constructor(
    private val client: Client,
    private val urls: PocketURLs
) {
    /**
     * Get the current articles recommendations from the Pocket server.
     *
     * Specifying an articles count for how many to download and an articles locale
     * is highly recommended but defaults are provided.
     *
     * @return The global articles recommendations as a raw JSON string or null on error.
     */
    @WorkerThread
    fun getGlobalArticlesRecommandations(
        count: Int = DEFAULT_ARTICLES_COUNT,
        locale: String = DEFAULT_ARTICLES_LOCALE
    ) = makeRequest(urls.getLocaleArticlesRecommendations(count, locale))

    /**
     * @return The requested JSON as a String or null on error.
     */
    @WorkerThread // synchronous request.
    private fun makeRequest(pocketEndpoint: Uri): String? {
        val request = Request(
            url = pocketEndpoint.toString()
        )
        return client.fetchBodyOrNull(request)
    }

    companion object {

        /**
         * Returns a new instance of [PocketEndpointRaw].
         *
         * @param pocketApiKey the API key for Pocket network requests.
         * @param client the HTTP client to use for network requests.
         *
         * @throws IllegalArgumentException if the provided API key is deemed invalid.
         */
        fun newInstance(pocketApiKey: String, client: Client): PocketEndpointRaw {
            assertIsValidApiKey(
                pocketApiKey
            )

            return PocketEndpointRaw(
                client,
                PocketURLs(pocketApiKey)
            )
        }
    }
}

private fun assertIsValidApiKey(apiKey: String) {
    assertIsNotBlank(apiKey, "API key")
    assertApiKeyHasValidStructure(apiKey)
}

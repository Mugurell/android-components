/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.stories

import android.content.Context
import androidx.annotation.VisibleForTesting
import mozilla.components.service.pocket.PocketRecommendedArticle
import mozilla.components.service.pocket.api.PocketEndpoint
import mozilla.components.service.pocket.api.PocketResponse

/**
 * Possible actions regarding the list of recommended articles.
 */
internal class PocketStoriesUseCases {

    /**
     * Allows for refreshing the list of pocket stories we have cached.
     *
     * @param context Android Context. Prefer sending application context to limit the possibility of even small leaks.
     * @param pocketToken Pocket OAuth request token used for downloading recommended articles.
     * @param pocketItemsCount How many recommended articles to download.
     *     Once downloaded these articles will replace any that we already had cached.
     * @param pocketItemsLocale The ISO-639 locale for Pocket recommended articles.
     */
    internal inner class RefreshPocketStories(
        @VisibleForTesting
        internal val context: Context,
        @VisibleForTesting
        internal val pocketToken: String,
        @VisibleForTesting
        internal val pocketItemsCount: Int,
        @VisibleForTesting
        internal val pocketItemsLocale: String
    ) {
        /**
         * Do a full download from Pocket -> persist locally cycle for recommended articles.
         */
        suspend operator fun invoke(): Boolean {
            val pocket = getPocketEndpoint(pocketToken)
            val response = pocket.getTopStories(pocketItemsCount, pocketItemsLocale)

            if (response is PocketResponse.Success) {
                getPocketRepository(context)
                    .addAllPocketPocketRecommendedArticles(response.data)
                return true
            }

            return false
        }
    }

    /**
     * Allows for querying the list of locally available Pocket recommended articles.
     */
    internal inner class GetPocketStories(private val context: Context) {
        /**
         * Get the current locally persisted list of Pocket recommended articles.
         */
        suspend operator fun invoke(): List<PocketRecommendedArticle> {
            return getPocketRepository(context)
                .getPocketRecommendedArticles()
        }
    }

    @VisibleForTesting
    internal fun getPocketRepository(context: Context) = PocketRecommendationsRepository(context)

    @VisibleForTesting
    internal fun getPocketEndpoint(apiKey: String) = PocketEndpoint.newInstance(apiKey)
}

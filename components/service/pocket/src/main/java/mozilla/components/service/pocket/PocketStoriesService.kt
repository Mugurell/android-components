/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket

import android.content.Context
import mozilla.components.service.pocket.stories.PocketStoriesUseCases
import mozilla.components.service.pocket.stories.update.PocketStoriesRefreshScheduler

/**
 * Allows for getting a list of pocket articles based on the provided [PocketArticlesConfig]
 *
 * @param context Android Context. Prefer sending application context to limit the possibility of even small leaks.
 * @param pocketArticlesConfig full configuration for how and what pocket articles to get.
 */
class PocketStoriesService(
    private val context: Context,
    pocketArticlesConfig: PocketArticlesConfig
) {
    internal var scheduler = PocketStoriesRefreshScheduler(pocketArticlesConfig)
    internal var getStoriesUsecase = PocketStoriesUseCases().GetPocketStories(context)

    /**
     * Single starting point for the "get Pocket articles" functionality.
     *
     * Use this at an as low as possible level in your application.
     * Must be paired in a similar way with the [stopPeriodicStoriesRefresh] method.
     *
     * This starts the process of downloading and caching Pocket articles in the background,
     * making them available for the [getStories] method.
     */
    fun startPeriodicStoriesRefresh() {
        scheduler.schedulePeriodicRefreshes(context)
    }

    /**
     * Single stopping point for the "get Pocket articles" functionality.
     *
     * Use this at an as low as possible level in your application.
     * Must be paired in a similar way with the [startPeriodicStoriesRefresh] method.
     *
     * This stops the process of downloading and caching Pocket articles in the background.
     */
    fun stopPeriodicStoriesRefresh() {
        scheduler.stopPeriodicRefreshes(context)
    }

    /**
     * Get a list of Pocket recommended articles based on the initial configuration.
     *
     * To be called after [startPeriodicStoriesRefresh] to ensure the recommendations are up-to-date.
     */
    suspend fun getStories(): List<PocketRecommendedArticle> {
        return getStoriesUsecase.invoke()
    }
}

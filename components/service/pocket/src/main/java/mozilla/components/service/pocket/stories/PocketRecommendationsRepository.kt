/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.stories

import android.content.Context
import androidx.annotation.VisibleForTesting
import mozilla.components.service.pocket.PocketRecommendedArticle
import mozilla.components.service.pocket.api.PocketGlobalArticleRecommendation
import mozilla.components.service.pocket.stories.db.PocketRecommendationsDatabase
import mozilla.components.service.pocket.stories.ext.toPocketRecommendedArticle
import mozilla.components.service.pocket.stories.ext.toPocketTopStoryEntity

/**
 * Wrapper over our local database.
 * Allows for easy CRUD operations.
 */
internal class PocketRecommendationsRepository(context: Context) {
    private val database: Lazy<PocketRecommendationsDatabase> = lazy { PocketRecommendationsDatabase.get(context) }
    @VisibleForTesting
    internal val pocketRecommendationsDao by lazy { database.value.pocketRecommendationsDao() }

    /**
     * Get the current locally persisted list of Pocket recommended articles.
     */
    suspend fun getPocketRecommendedArticles(): List<PocketRecommendedArticle> {
        return pocketRecommendationsDao.getPocketStories().map { it.toPocketRecommendedArticle() }
    }

    /**
     * Replace the current list of locally persisted Pocket recommended articles.
     *
     * @param articles The list of Pocket recommended articles to persist locally.
     */
    suspend fun addAllPocketPocketRecommendedArticles(
        articles: List<PocketGlobalArticleRecommendation>
    ) {
        pocketRecommendationsDao.cleanOldAndInsertNewPocketStories(articles.map { it.toPocketTopStoryEntity() })
    }
}

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.stories.ext

import mozilla.components.service.pocket.PocketRecommendedArticle
import mozilla.components.service.pocket.api.PocketGlobalArticleRecommendation
import mozilla.components.service.pocket.stories.db.PocketTopStoryEntity

/**
 * Map Pocket API objects to the object type that we persist locally.
 */
internal fun PocketGlobalArticleRecommendation.toPocketTopStoryEntity(): PocketTopStoryEntity =
    PocketTopStoryEntity(
        id, url, domain, title, excerpt, imageSrc, publishedTimestamp, engagement, dedupeUrl, sortId
    )

/**
 * Maps Room entities to the object type that we expose to service clients.
 */
internal fun PocketTopStoryEntity.toPocketRecommendedArticle(): PocketRecommendedArticle =
    PocketRecommendedArticle(
        id, url, domain, title, excerpt, imageSrc, publishedTimestamp, engagement, dedupeUrl, sortId
    )

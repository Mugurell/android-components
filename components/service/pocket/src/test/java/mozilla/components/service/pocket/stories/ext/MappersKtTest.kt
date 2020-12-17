/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.stories.ext

import mozilla.components.service.pocket.api.PocketGlobalArticleRecommendation
import mozilla.components.service.pocket.stories.db.PocketTopStoryEntity
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertSame
import org.junit.Test
import kotlin.reflect.full.memberProperties

class MappersKtTest {
    @Test
    fun `toPocketTopStoryEntity should do a one to one mapping of the api article to the db article`() {
        val apiArticle = PocketGlobalArticleRecommendation(
            3, "url", "domain", "title", "excerpt", "imageSrc",
            "publishedTimestamp", "engagement", "dedupeUrl", 4
        )

        val result = apiArticle.toPocketTopStoryEntity()

        assertNotEquals(apiArticle::class.memberProperties, result::class.memberProperties)
        assertSame(apiArticle.id, result.id)
        assertSame(apiArticle.url, result.url)
        assertSame(apiArticle.domain, result.domain)
        assertSame(apiArticle.title, result.title)
        assertSame(apiArticle.excerpt, result.excerpt)
        assertSame(apiArticle.imageSrc, result.imageSrc)
        assertSame(apiArticle.publishedTimestamp, result.publishedTimestamp)
        assertSame(apiArticle.engagement, result.engagement)
        assertSame(apiArticle.dedupeUrl, result.dedupeUrl)
        assertSame(apiArticle.sortId, result.sortId)
    }

    @Test
    fun `toPocketRecommendedArticle should do a one to one mapping of the db article to the domain article`() {
        val dbArticle = PocketTopStoryEntity(
            3, "url", "domain", "title", "excerpt", "imageSrc",
            "publishedTimestamp", "engagement", "dedupeUrl", 4
        )

        val result = dbArticle.toPocketRecommendedArticle()

        assertNotEquals(dbArticle::class.memberProperties, result::class.memberProperties)
        assertSame(dbArticle.id, result.id)
        assertSame(dbArticle.url, result.url)
        assertSame(dbArticle.domain, result.domain)
        assertSame(dbArticle.title, result.title)
        assertSame(dbArticle.excerpt, result.excerpt)
        assertSame(dbArticle.imageSrc, result.imageSrc)
        assertSame(dbArticle.publishedTimestamp, result.publishedTimestamp)
        assertSame(dbArticle.engagement, result.engagement)
        assertSame(dbArticle.dedupeUrl, result.dedupeUrl)
        assertSame(dbArticle.sortId, result.sortId)
    }
}
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.helpers

import mozilla.components.service.pocket.api.PocketGlobalArticleRecommendation
import mozilla.components.service.pocket.stories.db.PocketTopStoryEntity

private const val POCKET_DIR = "pocket"

/**
 * Accessors to resources used in testing. These files are available in `app/src/test/resources`.
 */
internal enum class PocketTestResource(private val path: String) {
    // For expected Kotlin data type representations of this test data, see the companion object.
    POCKET_ARTICLES_RECOMMENDATIONS("$POCKET_DIR/articles_recommendations.json");

    /** @return the raw resource. */
    fun get(): String = this::class.java.classLoader!!.getResource(path)!!.readText()

    companion object {
        const val TEST_ARTICLES_DATA_SIZE = 5

        fun getApiExpectedPocketArticlesRecommendationFirstTwo(): List<PocketGlobalArticleRecommendation> = listOf(
            PocketGlobalArticleRecommendation(
                id = 76779,
                url = "https://pocket.co/xhJLgl",
                domain = "hbr.org",
                title = "The Most Common Type of Incompetent Leader",
                excerpt = "Absentee leadership rarely comes up in today’s leadership or business literature, but research shows it is alarmingly common.",
                imageSrc = "https://img-getpocket.cdn.mozilla.net/direct?url=https%3A%2F%2Fpocket-image-cache.com%2F1200x%2Ffilters%3Ano_upscale%28%29%3Aformat%28jpg%29%3Aextract_cover%28%29%2Fhttps%253A%252F%252Fhbr.org%252Fresources%252Fimages%252Farticle_assets%252F2018%252F03%252FMar18_30_730266687.jpg&resize=w450",
                publishedTimestamp = "-62169962400",
                engagement = "",
                dedupeUrl = "https://getpocket.com/explore/item/the-most-common-type-of-incompetent-leader?utm_source=pocket-newtab",
                sortId = 0
            ),
            PocketGlobalArticleRecommendation(
                id = 76715,
                url = "https://pocket.co/xhJLg5",
                domain = "nautil.us",
                title = "Why Doing Good Makes It Easier to Be Bad",
                excerpt = "What makes people who seem so good in public act so bad in private?",
                imageSrc = "https://img-getpocket.cdn.mozilla.net/direct?url=https%3A%2F%2Fpocket-image-cache.com%2F1200x%2Ffilters%3Ano_upscale%28%29%3Aformat%28jpg%29%3Aextract_cover%28%29%2Fhttp%253A%252F%252Fstatic.nautil.us%252F15705_3292c5c2ca71351a9406a9614e147ad3.jpg&resize=w450",
                publishedTimestamp = "-62169962400",
                engagement = "",
                dedupeUrl = "https://getpocket.com/explore/item/why-doing-good-makes-it-easier-to-be-bad?utm_source=pocket-newtab",
                sortId = 1
            )
        )

        fun getDbExpectedPocketArticle() = PocketTopStoryEntity(
            id = 76779,
            url = "https://pocket.co/xhJLgl",
            domain = "hbr.org",
            title = "The Most Common Type of Incompetent Leader",
            excerpt = "Absentee leadership rarely comes up in today’s leadership or business literature, but research shows it is alarmingly common.",
            imageSrc = "https://img-getpocket.cdn.mozilla.net/direct?url=https%3A%2F%2Fpocket-image-cache.com%2F1200x%2Ffilters%3Ano_upscale%28%29%3Aformat%28jpg%29%3Aextract_cover%28%29%2Fhttps%253A%252F%252Fhbr.org%252Fresources%252Fimages%252Farticle_assets%252F2018%252F03%252FMar18_30_730266687.jpg&resize=w450",
            publishedTimestamp = "-62169962400",
            engagement = "",
            dedupeUrl = "https://getpocket.com/explore/item/the-most-common-type-of-incompetent-leader?utm_source=pocket-newtab",
            sortId = 0
        )
    }
}

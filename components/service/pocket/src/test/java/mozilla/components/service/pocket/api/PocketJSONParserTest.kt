/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import mozilla.components.service.pocket.api.PocketJSONParser.Companion.KEY_ARRAY_ITEMS
import mozilla.components.service.pocket.helpers.PocketTestResource
import mozilla.components.service.pocket.helpers.PocketTestResource.Companion.TEST_ARTICLES_DATA_SIZE
import mozilla.components.service.pocket.helpers.assertClassVisibility
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.reflect.KVisibility

@RunWith(AndroidJUnit4::class)
class PocketJSONParserTest {

    private lateinit var parser: PocketJSONParser

    @Before
    fun setUp() {
        parser = PocketJSONParser()
    }

    @Test
    fun `GIVEN a PocketJSONParser THEN its visibility is internal`() {
        assertClassVisibility(PocketJSONParser::class, KVisibility.INTERNAL)
    }

    @Test
    fun `GIVEN PocketJSONParser WHEN parsing valid global articles recommendations THEN PocketGlobalArticleRecommendation are returned`() {
        val expectedSubset = PocketTestResource.getApiExpectedPocketArticlesRecommendationFirstTwo()
        val pocketJSON = PocketTestResource.POCKET_ARTICLES_RECOMMENDATIONS.get()
        val actualVideos = parser.jsonToGlobalArticlesRecommendation(pocketJSON)

        // We only test a subset of the data for developer sanity. :)
        assertNotNull(actualVideos)
        assertEquals(TEST_ARTICLES_DATA_SIZE, actualVideos!!.size)
        expectedSubset.forEachIndexed { i, expected ->
            assertEquals(expected, actualVideos[i])
        }
    }

    @Test
    fun `WHEN parsing global articles recommendations with missing fields on some items THEN those entries are dropped`() {
        val pocketJSON = PocketTestResource.POCKET_ARTICLES_RECOMMENDATIONS.get()

        val expectedFirstTitle = JSONObject(pocketJSON)
            .getJSONArray(KEY_ARRAY_ITEMS)
            .getJSONObject(0)
            .getString("title")
        assertNotNull(expectedFirstTitle)

        val pocketJSONWithNoExcerptExceptFirst = removeTitleStartingAtIndex(1, pocketJSON)
        val actualVideos =
            parser.jsonToGlobalArticlesRecommendation(pocketJSONWithNoExcerptExceptFirst)

        assertNotNull(actualVideos)
        assertEquals(1, actualVideos!!.size)
        assertEquals(expectedFirstTitle, actualVideos[0].title)
    }

    @Test
    fun `WHEN parsing global articles recommendations with missing fields on all items THEN null is returned`() {
        val pocketJSON = PocketTestResource.POCKET_ARTICLES_RECOMMENDATIONS.get()
        val pocketJSONWithNoTitles = removeTitleStartingAtIndex(0, pocketJSON)
        val actualVideos = parser.jsonToGlobalArticlesRecommendation(pocketJSONWithNoTitles)
        assertNull(actualVideos)
    }

    @Test
    fun `WHEN parsing global articles recommendations for an empty string THEN null is returned`() {
        assertNull(parser.jsonToGlobalArticlesRecommendation(""))
    }

    @Test
    fun `WHEN parsing global articles recommendations for an invalid JSON String THEN null is returned`() {
        assertNull(parser.jsonToGlobalArticlesRecommendation("{!!}}"))
    }

    @Test
    fun `WHEN newInstance is called THEN no exception is thrown`() {
        PocketJSONParser.newInstance()
    }
}

private fun removeTitleStartingAtIndex(startIndex: Int, json: String): String {
    val obj = JSONObject(json)
    val articlesJson = obj.getJSONArray(KEY_ARRAY_ITEMS)
    for (i in startIndex until articlesJson.length()) {
        articlesJson.getJSONObject(i).remove("title")
    }
    return obj.toString()
}

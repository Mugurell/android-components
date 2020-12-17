/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import mozilla.components.concept.fetch.Client
import mozilla.components.service.pocket.helpers.PocketTestResource
import mozilla.components.service.pocket.helpers.TEST_ARTICLES_COUNT
import mozilla.components.service.pocket.helpers.TEST_ARTICLES_LOCALE
import mozilla.components.service.pocket.helpers.TEST_VALID_API_KEY
import mozilla.components.service.pocket.helpers.assertClassVisibility
import mozilla.components.service.pocket.helpers.assertResponseIsFailure
import mozilla.components.support.test.any
import mozilla.components.support.test.mock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import kotlin.reflect.KVisibility

@RunWith(AndroidJUnit4::class)
class PocketEndpointTest {

    private lateinit var endpoint: PocketEndpoint
    private lateinit var raw: PocketEndpointRaw // we shorten the name to avoid confusion with endpoint.
    private lateinit var jsonParser: PocketJSONParser

    private lateinit var client: Client

    @Before
    fun setUp() {
        raw = mock()
        jsonParser = mock()
        endpoint = PocketEndpoint(raw, jsonParser)

        client = mock()
    }

    @Test
    fun `GIVEN a PocketEndpoint THEN its visibility is internal`() {
        assertClassVisibility(PocketEndpoint::class, KVisibility.INTERNAL)
    }

    @Test
    fun `WHEN getting articles recommendations and the endpoint returns null THEN a failure is returned`() {
        Mockito.`when`(raw.getGlobalArticlesRecommandations(TEST_ARTICLES_COUNT, TEST_ARTICLES_LOCALE)).thenReturn(null)
        Mockito.`when`(jsonParser.jsonToGlobalArticlesRecommendation(any())).thenThrow(AssertionError("We assume this won't get called so we don't mock it"))
        assertResponseIsFailure(endpoint.getTopStories(TEST_ARTICLES_COUNT, TEST_ARTICLES_LOCALE))
    }

    @Test
    fun `WHEN getting articles recommendations and the endpoint returns a String THEN the String is put into the jsonParser`() {
        arrayOf(
            "",
            " ",
            "{}",
            """{"expectedJSON": 101}"""
        ).forEach { expected ->
            Mockito.`when`(raw.getGlobalArticlesRecommandations(TEST_ARTICLES_COUNT, TEST_ARTICLES_LOCALE)).thenReturn(expected)
            endpoint.getTopStories(TEST_ARTICLES_COUNT, TEST_ARTICLES_LOCALE)
            Mockito.verify(jsonParser, Mockito.times(1)).jsonToGlobalArticlesRecommendation(expected)
        }
    }

    @Test
    fun `WHEN getting articles recommendations, the server returns a String, and the JSON parser returns null THEN a failure is returned`() {
        Mockito.`when`(raw.getGlobalArticlesRecommandations(TEST_ARTICLES_COUNT, TEST_ARTICLES_LOCALE)).thenReturn("")
        Mockito.`when`(jsonParser.jsonToGlobalArticlesRecommendation(any())).thenReturn(null)
        assertResponseIsFailure(endpoint.getTopStories(TEST_ARTICLES_COUNT, TEST_ARTICLES_LOCALE))
    }

    @Test
    fun `WHEN getting articles recommendations, the server returns a String, and the jsonParser returns an empty list THEN a failure is returned`() {
        Mockito.`when`(raw.getGlobalArticlesRecommandations(TEST_ARTICLES_COUNT, TEST_ARTICLES_LOCALE)).thenReturn("")
        Mockito.`when`(jsonParser.jsonToGlobalArticlesRecommendation(any())).thenReturn(emptyList())
        assertResponseIsFailure(endpoint.getTopStories(TEST_ARTICLES_COUNT, TEST_ARTICLES_LOCALE))
    }

    @Test
    fun `WHEN getting articles recommendations, the server returns a String, and the jsonParser returns valid data THEN a success with the data is returned`() {
        val expected = PocketTestResource.getApiExpectedPocketArticlesRecommendationFirstTwo()
        Mockito.`when`(raw.getGlobalArticlesRecommandations(TEST_ARTICLES_COUNT, TEST_ARTICLES_LOCALE)).thenReturn("")
        Mockito.`when`(jsonParser.jsonToGlobalArticlesRecommendation(any())).thenReturn(expected)

        val actual = endpoint.getTopStories(TEST_ARTICLES_COUNT, TEST_ARTICLES_LOCALE)
        assertEquals(expected, (actual as? PocketResponse.Success)?.data)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `WHEN newInstance is called with a blank API key THEN an exception is thrown`() {
        PocketEndpoint.newInstance(" ", client)
    }

    @Test
    fun `WHEN newInstance is called with valid args THEN no exception is thrown`() {
        PocketEndpoint.newInstance(TEST_VALID_API_KEY, client)
    }
}
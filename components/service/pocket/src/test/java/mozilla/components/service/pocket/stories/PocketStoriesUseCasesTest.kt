/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.stories

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import mozilla.components.service.pocket.PocketRecommendedArticle
import mozilla.components.service.pocket.api.PocketEndpoint
import mozilla.components.service.pocket.api.PocketResponse
import mozilla.components.service.pocket.helpers.PocketTestResource
import mozilla.components.service.pocket.helpers.TEST_ARTICLES_COUNT
import mozilla.components.service.pocket.helpers.TEST_ARTICLES_LOCALE
import mozilla.components.service.pocket.helpers.TEST_VALID_API_KEY
import mozilla.components.service.pocket.helpers.assertClassVisibility
import mozilla.components.service.pocket.stories.ext.toPocketRecommendedArticle
import mozilla.components.support.test.any
import mozilla.components.support.test.mock
import mozilla.components.support.test.robolectric.testContext
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.never
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import kotlin.reflect.KVisibility

@RunWith(AndroidJUnit4::class)
class PocketStoriesUseCasesTest {
    private val usecases = spy(PocketStoriesUseCases())
    private val pocketRepo: PocketRecommendationsRepository = mock()
    private val pocketEndoint: PocketEndpoint = mock()

    @Test
    fun `GIVEN a PocketStoriesUseCases THEN its visibility is internal`() {
        assertClassVisibility(PocketStoriesUseCases::class, KVisibility.INTERNAL)
    }

    @Test
    fun `GIVEN a RefreshPocketStories THEN its visibility is internal`() {
        assertClassVisibility(
            PocketStoriesUseCases.RefreshPocketStories::class,
            KVisibility.INTERNAL
        )
    }

    @Test
    fun `GIVEN a GetPocketStories THEN its visibility is internal`() {
        assertClassVisibility(PocketStoriesUseCases.GetPocketStories::class, KVisibility.INTERNAL)
    }

    @Before
    fun setup() {
        doReturn(pocketEndoint).`when`(usecases).getPocketEndpoint(ArgumentMatchers.anyString())
        doReturn(pocketRepo).`when`(usecases).getPocketRepository(any())
    }

    @Test
    fun `GIVEN PocketStoriesUseCases WHEN RefreshPocketStories is called THEN it should download articles from API and return early if unsuccessful response`() {
        val refreshUsecase = spy(
            usecases.RefreshPocketStories(
                testContext, TEST_VALID_API_KEY, TEST_ARTICLES_COUNT, TEST_ARTICLES_LOCALE
            )
        )
        val successfulResponse = getSuccessfulPocketArticles()

        doReturn(successfulResponse)
            .`when`(pocketEndoint)
            .getTopStories(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())

        val result = runBlocking {
            refreshUsecase.invoke()
        }

        assertTrue(result)
        verify(pocketEndoint).getTopStories(TEST_ARTICLES_COUNT, TEST_ARTICLES_LOCALE)
        runBlocking {
            verify(pocketRepo).addAllPocketPocketRecommendedArticles((successfulResponse as PocketResponse.Success).data)
        }
    }

    @Test
    fun `GIVEN PocketStoriesUseCases WHEN RefreshPocketStories is called THEN it should download articles from API and save a successful response locally`() {
        val refreshUsecase = spy(
            usecases.RefreshPocketStories(
                testContext, TEST_VALID_API_KEY, TEST_ARTICLES_COUNT, TEST_ARTICLES_LOCALE
            )
        )
        val successfulResponse = getFailedPocketArticles()

        doReturn(successfulResponse)
            .`when`(pocketEndoint)
            .getTopStories(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())

        val result = runBlocking {
            refreshUsecase.invoke()
        }

        assertFalse(result)
        verify(pocketEndoint).getTopStories(TEST_ARTICLES_COUNT, TEST_ARTICLES_LOCALE)
        runBlocking {
            verify(pocketRepo, never()).addAllPocketPocketRecommendedArticles(any())
        }
    }

    @Test
    fun `GIVEN PocketStoriesUseCases WHEN GetPocketStories is called THEN it should delegate the repository to return locally stored articles`() =
        runBlocking {
            val getStoriesUsecase = spy(usecases.GetPocketStories(testContext))

            doReturn(emptyList<PocketRecommendedArticle>())
                .`when`(pocketRepo).getPocketRecommendedArticles()
            var result = getStoriesUsecase.invoke()
            verify(pocketRepo).getPocketRecommendedArticles()
            assertTrue(result.isEmpty())

            val locallyStoredArticles =
                listOf(PocketTestResource.getDbExpectedPocketArticle().toPocketRecommendedArticle())
            doReturn(locallyStoredArticles)
                .`when`(pocketRepo).getPocketRecommendedArticles()
            result = getStoriesUsecase.invoke()
            // getPocketRecommendedArticles() should've been called 2 times. Once in the above check, once now.
            verify(pocketRepo, times(2)).getPocketRecommendedArticles()
            assertEquals(result, locallyStoredArticles)
        }

    private fun getSuccessfulPocketArticles() =
        PocketResponse.wrap(PocketTestResource.getApiExpectedPocketArticlesRecommendationFirstTwo())

    private fun getFailedPocketArticles() = PocketResponse.wrap(null)
}
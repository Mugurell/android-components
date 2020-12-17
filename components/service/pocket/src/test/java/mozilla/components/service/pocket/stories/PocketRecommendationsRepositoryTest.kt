/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.stories

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import mozilla.components.service.pocket.helpers.PocketTestResource
import mozilla.components.service.pocket.stories.db.PocketRecommendationsDao
import mozilla.components.service.pocket.stories.ext.toPocketRecommendedArticle
import mozilla.components.service.pocket.stories.ext.toPocketTopStoryEntity
import mozilla.components.support.test.robolectric.testContext
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class PocketRecommendationsRepositoryTest {

    private val pocketRepo = spy(PocketRecommendationsRepository(testContext))
    private lateinit var dao: PocketRecommendationsDao

    @Before
    fun setUp() {
        dao = mock(PocketRecommendationsDao::class.java)
        `when`(pocketRepo.pocketRecommendationsDao).thenReturn(dao)
    }

    @Test
    fun `GIVEN PocketRecommendationsRepository WHEN getPocketRecommendedArticles then + should return db entities mapped to domain type`() {
        runBlocking {
            val dbArticle = PocketTestResource.getDbExpectedPocketArticle()
            `when`(dao.getPocketStories()).thenReturn(listOf(dbArticle))

            val result = pocketRepo.getPocketRecommendedArticles()

            verify(dao).getPocketStories()
            assertEquals(1, result.size)
            assertEquals(dbArticle.toPocketRecommendedArticle(), result[0])
        }
    }

    @Test
    fun `GIVEN PocketRecommendationsRepository WHEN addAllPocketPocketRecommendedArticles should persist the received article to db`() {
        runBlocking {
            val apiArticles = PocketTestResource.getApiExpectedPocketArticlesRecommendationFirstTwo()

            pocketRepo.addAllPocketPocketRecommendedArticles(apiArticles)

            val apiArticlesMappedForDb = apiArticles.map { it.toPocketTopStoryEntity() }
            verify(dao).cleanOldAndInsertNewPocketStories(apiArticlesMappedForDb)
        }
    }
}
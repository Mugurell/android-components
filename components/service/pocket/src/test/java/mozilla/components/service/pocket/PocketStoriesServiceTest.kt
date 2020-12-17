/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import mozilla.components.service.pocket.helpers.assertConstructorsVisibility
import mozilla.components.support.test.any
import mozilla.components.support.test.mock
import mozilla.components.support.test.robolectric.testContext
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import kotlin.reflect.KVisibility

@RunWith(AndroidJUnit4::class)
class PocketStoriesServiceTest {
    private val storiesService = PocketStoriesService(testContext, mock()).also {
        it.scheduler = mock()
        it.getStoriesUsecase = mock()
    }

    @Test
    fun `GIVEN PocketStoriesService THEN it should be publicly available`() {
        assertConstructorsVisibility(PocketArticlesConfig::class, KVisibility.PUBLIC)
    }

    @Test
    fun `GIVEN PocketStoriesService WHEN startPeriodicStoriesRefresh THEN scheduler#schedulePeriodicRefreshes should be called`() {
        storiesService.startPeriodicStoriesRefresh()

        verify(storiesService.scheduler).schedulePeriodicRefreshes(any())
    }

    @Test
    fun `GIVEN PocketStoriesService WHEN stopPeriodicStoriesRefresh THEN scheduler#stopPeriodicRefreshes should be called`() {
        storiesService.stopPeriodicStoriesRefresh()

        verify(storiesService.scheduler).stopPeriodicRefreshes(any())
    }

    @Test
    fun `GIVEN PocketStoriesService WHEN getStories THEN getStoriesUsecase should return`() = runBlocking {
        val articles = listOf(mock<PocketRecommendedArticle>())
        `when`(storiesService.getStoriesUsecase.invoke()).thenReturn(articles)

        val result = storiesService.getStories()

        assertEquals(articles, result)
    }
}
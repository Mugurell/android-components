/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket

import androidx.test.ext.junit.runners.AndroidJUnit4
import mozilla.components.service.pocket.helpers.assertClassVisibility
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.reflect.KVisibility

@RunWith(AndroidJUnit4::class)
class PocketArticlesConfigTest {
    @Test
    fun `GIVEN a PocketArticlesConfig THEN its visibility is internal`() {
        assertClassVisibility(PocketArticlesConfig::class, KVisibility.PUBLIC)
    }

    @Test
    fun `WHEN instantiating a PocketArticlesConfig THEN all but pocketApiKey is a mandatory parameter`() {
        val config = PocketArticlesConfig(pocketApiKey = "mock()")

        val defaultFrequency = Frequency(DEFAULT_REFRESH_INTERVAL, DEFAULT_REFRESH_TIMEUNIT)
        assertEquals(defaultFrequency.repeatInterval, config.frequency.repeatInterval)
        assertEquals(defaultFrequency.repeatIntervalTimeUnit, config.frequency.repeatIntervalTimeUnit)
        assertEquals(config.articlesCount, DEFAULT_ARTICLES_COUNT)
        assertEquals(config.locale, DEFAULT_ARTICLES_LOCALE)
    }

    @Test
    fun `GIVEN a Frequency THEN its visibility is internal`() {
        assertClassVisibility(Frequency::class, KVisibility.PUBLIC)
    }
}
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.stories.update

import androidx.test.ext.junit.runners.AndroidJUnit4
import mozilla.components.service.pocket.helpers.TEST_ARTICLES_COUNT
import mozilla.components.service.pocket.helpers.TEST_ARTICLES_LOCALE
import mozilla.components.service.pocket.helpers.TEST_VALID_API_KEY
import mozilla.components.service.pocket.helpers.assertClassVisibility
import mozilla.components.service.pocket.stories.update.RefreshPocketWorker.Companion.KEY_EXTRA_DATA_POCKET_ITEMS_COUNT
import mozilla.components.service.pocket.stories.update.RefreshPocketWorker.Companion.KEY_EXTRA_DATA_POCKET_ITEMS_LOCALE
import mozilla.components.service.pocket.stories.update.RefreshPocketWorker.Companion.KEY_EXTRA_DATA_POCKET_TOKEN
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.reflect.KVisibility

@RunWith(AndroidJUnit4::class)
class RefreshPocketWorkerTest {

    @Test
    fun `GIVEN a RefreshPocketWorker THEN its visibility is internal`() {
        assertClassVisibility(RefreshPocketWorker::class, KVisibility.INTERNAL)
    }

    @Test
    fun `GIVEN RefreshPocketWorker WHEN getPopulatedWorkerData THEN a properly built Data is returned`() {
        val result = RefreshPocketWorker.getPopulatedWorkerData(
            TEST_VALID_API_KEY, TEST_ARTICLES_COUNT, TEST_ARTICLES_LOCALE
        )

        assertEquals(3, result.size())
        assertEquals(TEST_VALID_API_KEY, result.getString(KEY_EXTRA_DATA_POCKET_TOKEN))
        assertEquals(TEST_ARTICLES_COUNT, result.getInt(KEY_EXTRA_DATA_POCKET_ITEMS_COUNT, -1))
        assertEquals(TEST_ARTICLES_LOCALE, result.getString(KEY_EXTRA_DATA_POCKET_ITEMS_LOCALE))
    }
}

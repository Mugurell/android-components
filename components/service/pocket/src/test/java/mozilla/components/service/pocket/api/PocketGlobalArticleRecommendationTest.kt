/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.api

import mozilla.components.service.pocket.helpers.assertClassVisibility
import mozilla.components.service.pocket.helpers.assertConstructorsVisibility
import org.junit.Test
import kotlin.reflect.KVisibility

class PocketGlobalArticleRecommendationTest {

    @Test
    fun `GIVEN a PocketGlobalArticleRecommendation THEN its constructors are internal`() {
        assertConstructorsVisibility(PocketGlobalArticleRecommendation::class, KVisibility.INTERNAL)
    }

    @Test
    fun `GIVEN a PocketGlobalArticleRecommendation THEN its visibility is internal`() {
        assertClassVisibility(PocketGlobalArticleRecommendation::class, KVisibility.INTERNAL)
    }
}

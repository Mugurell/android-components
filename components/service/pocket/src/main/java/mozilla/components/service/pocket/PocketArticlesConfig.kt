/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket

import java.util.concurrent.TimeUnit

internal const val DEFAULT_REFRESH_INTERVAL = 1L
@Suppress("TopLevelPropertyNaming")
internal val DEFAULT_REFRESH_TIMEUNIT = TimeUnit.HOURS
internal const val DEFAULT_ARTICLES_COUNT = 3
internal const val DEFAULT_ARTICLES_LOCALE = "en-US"

/**
 * Indicating all details for how the pocket stories should be refreshed.
 *
 * @param pocketApiKey The Pocket access token needed for retrieving Pocket recommendations.
 * @param frequency Optional - The interval at which to try and refresh items. Defaults to 1 hour.
 * @param articlesCount Optional - count for how many articles to be downloaded and made available to clients.
 *     Default value is 3.
 * @param locale Optional - ISO-639 locale used to retrieve recommended items for a certain region.
 */
class PocketArticlesConfig(
    val pocketApiKey: String,
    val frequency: Frequency = Frequency(
        DEFAULT_REFRESH_INTERVAL,
        DEFAULT_REFRESH_TIMEUNIT
    ),
    val articlesCount: Int = DEFAULT_ARTICLES_COUNT,
    val locale: String = DEFAULT_ARTICLES_LOCALE
)

/**
 * Indicates how often the pocket stories should be refreshed.
 *
 * @param repeatInterval Integer indicating how often the update should happen.
 * @param repeatIntervalTimeUnit The time unit of the [repeatInterval].
 */
class Frequency(val repeatInterval: Long, val repeatIntervalTimeUnit: TimeUnit)

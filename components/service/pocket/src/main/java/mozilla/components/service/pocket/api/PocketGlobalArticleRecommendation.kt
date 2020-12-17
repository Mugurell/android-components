/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.api

/**
 * A recommended article as returned from the Pocket Global Recommendations endpoint v3.
 *
 * @param id a unique identifier for this recommendation.
 * @param url a "pocket.co" shortlink for the original article's page.
 * @param domain the domain where the article appears, e.g. "bbc.com".
 * @param title the title of the article.
 * @param excerpt a summary of the article.
 * @param imageSrc a url to a still image representing the article.
 * @param publishedTimestamp unknown: please ask for clarification if needed.
 * @param engagement unknown: please ask for clarification if needed.
 * @param dedupeUrl the full url to the article's page. Will contain "?utm_source=pocket-newtab".
 * @param sortId the index of this recommendation in the list which is sorted by date added to the API results.
 */
// If we change the properties, e.g. adding a field, any consumers that rely on persisting and constructing new
// instances of this data type, will be required to implement code to migrate the persisted data. To prevent this,
// we mark the constructors internal. If a consumer is required to persist this data, we should consider providing a
// persistence solution.
internal data class PocketGlobalArticleRecommendation internal constructor(
    val id: Long,
    val url: String,
    val domain: String,
    val title: String,
    val excerpt: String,
    val imageSrc: String,
    val publishedTimestamp: String,
    val engagement: String,
    val dedupeUrl: String,
    val sortId: Int
)

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.stories.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = PocketRecommendationsDatabase.TABLE_NAME_ARTICLES)
internal data class PocketTopStoryEntity(
    @PrimaryKey
    val id: Long,
    val url: String,
    val domain: String,
    val title: String,
    val excerpt: String,
    val imageSrc: String,
    @ColumnInfo(name = "published_timestamp")
    val publishedTimestamp: String,
    @ColumnInfo(name = "dedupe_url")
    val engagement: String,
    @ColumnInfo(name = "image_src")
    val dedupeUrl: String,
    @ColumnInfo(name = "sortId")
    val sortId: Int
)

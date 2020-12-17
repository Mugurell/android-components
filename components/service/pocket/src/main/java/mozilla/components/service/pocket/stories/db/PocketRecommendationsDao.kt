/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.stories.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
internal interface PocketRecommendationsDao {
    @Transaction
    suspend fun cleanOldAndInsertNewPocketStories(stories: List<PocketTopStoryEntity>) {
        deleteAll()
        insertPocketStories(stories)
    }

    @Query("SELECT * FROM ${PocketRecommendationsDatabase.TABLE_NAME_ARTICLES}")
    suspend fun getPocketStories(): List<PocketTopStoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPocketStories(stories: List<PocketTopStoryEntity>)

    @Query("DELETE FROM ${PocketRecommendationsDatabase.TABLE_NAME_ARTICLES}")
    suspend fun deleteAll()
}

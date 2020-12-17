/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.pocket.stories.update

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import mozilla.components.service.pocket.Frequency
import mozilla.components.service.pocket.PocketArticlesConfig
import mozilla.components.service.pocket.logger
import mozilla.components.service.pocket.stories.update.RefreshPocketWorker.Companion.REFRESH_WORK_TAG

/**
 * Class used to schedule Pocket recommended articles refresh.
 */
internal class PocketStoriesRefreshScheduler(
    private val pocketArticlesConfig: PocketArticlesConfig
) {
    internal fun schedulePeriodicRefreshes(context: Context) {
        logger.info("Scheduling pocket recommendations background refresh")

        val refreshWork = createPeriodicWorkerRequest(
            pocketApiKey = pocketArticlesConfig.pocketApiKey,
            articlesCount = pocketArticlesConfig.articlesCount,
            locale = pocketArticlesConfig.locale,
            frequency = pocketArticlesConfig.frequency
        )

        getWorkManager(context)
            .enqueueUniquePeriodicWork(REFRESH_WORK_TAG, ExistingPeriodicWorkPolicy.KEEP, refreshWork)
    }

    internal fun stopPeriodicRefreshes(context: Context) {
        getWorkManager(context)
            .cancelAllWorkByTag(REFRESH_WORK_TAG)
    }

    @VisibleForTesting
    internal fun createPeriodicWorkerRequest(
        pocketApiKey: String,
        frequency: Frequency,
        articlesCount: Int,
        locale: String
    ): PeriodicWorkRequest {
        val constraints = getWorkerConstrains()
        val extras = getPopulatedWorkerData(pocketApiKey, articlesCount, locale)

        return PeriodicWorkRequestBuilder<RefreshPocketWorker>(
            frequency.repeatInterval,
            frequency.repeatIntervalTimeUnit
        ).apply {
            setConstraints(constraints)
            setInputData(extras)
            addTag(REFRESH_WORK_TAG)
        }.build()
    }

    @VisibleForTesting
    internal fun getWorkerConstrains() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    @VisibleForTesting
    internal fun getPopulatedWorkerData(pocketApiKey: String, articlesCount: Int, locale: String) =
        RefreshPocketWorker.getPopulatedWorkerData(
            pocketApiKey, articlesCount, locale
        )

    @VisibleForTesting
    internal fun getWorkManager(context: Context) = WorkManager.getInstance(context)
}

package co.si

import android.app.Application
import androidx.databinding.ktx.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Copyright © 2022 Hell Corporation. All rights reserved.
 *
 * @author Mr. Lucifer
 * @since Feb 16, 2022
 */
@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
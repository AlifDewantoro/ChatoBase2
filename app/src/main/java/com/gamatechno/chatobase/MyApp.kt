package com.gamatechno.chatobase

import android.content.Context
import android.content.res.Configuration
import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate
import com.gamatechno.chato.sdk.module.core.ChatoBaseApplication
import java.util.*

class MyApp: ChatoBaseApplication() {

    private val localizationDelegate = LocalizationApplicationDelegate()

    override fun onCreate() {
        super.onCreate()
        instance = this
        setChatoPlaceholder(R.drawable.ic_logo_space)
        KEY = "EOVIZ"
    }

    companion object {
        @JvmStatic
        lateinit var instance: MyApp
            private set
    }

    override fun attachBaseContext(base: Context) {
        val locale = Locale("in")
        Locale.setDefault(locale)
        localizationDelegate.setDefaultLanguage(base, locale)
        super.attachBaseContext(localizationDelegate.attachBaseContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localizationDelegate.onConfigurationChanged(this)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

}
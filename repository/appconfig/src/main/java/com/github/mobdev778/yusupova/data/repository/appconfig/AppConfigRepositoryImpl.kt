package com.github.mobdev778.yusupova.data.repository.appconfig

import android.content.Context
import android.os.Build
import com.github.mobdev778.yusupova.domain.model.appconfig.AppLocale

class AppConfigRepositoryImpl(val context: Context): AppConfigRepository {

    override val appLocale: AppLocale by lazy {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }
        when (locale.country.lowercase()) {
            "ru" -> AppLocale.RU
            else -> AppLocale.EN
        }
    }

    override val serverUrl: String
        get() = "https://raw.githubusercontent.com/mobdev778/yusupova-server/master/"

}

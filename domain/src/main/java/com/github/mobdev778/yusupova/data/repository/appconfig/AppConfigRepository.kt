package com.github.mobdev778.yusupova.data.repository.appconfig

import com.github.mobdev778.yusupova.domain.model.appconfig.AppLocale

interface AppConfigRepository {
    val appLocale: AppLocale
    val serverUrl: String
}

package com.github.mobdev778.yusupova.data.repository.appconfig

import com.github.mobdev778.yusupova.domain.model.appconfig.AppLocale
import javax.inject.Inject

class AppConfigRepositoryImpl @Inject constructor(@MockServerUrlAnnotation mServerUrl: String): AppConfigRepository {
    override val appLocale: AppLocale = AppLocale.EN
    override val serverUrl: String = mServerUrl
}

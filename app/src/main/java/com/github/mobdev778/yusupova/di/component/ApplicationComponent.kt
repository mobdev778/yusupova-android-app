package com.github.mobdev778.yusupova.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component

@Component(
    dependencies = [ VersesComponent::class ]
)
@ApplicationScope
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
            versesComponent: VersesComponent,
        ): ApplicationComponent
    }
}

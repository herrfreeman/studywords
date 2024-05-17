package ru.blackmesa.studywords.domain

import org.koin.dsl.module

val settingsDomainModule = module {

    single<SettingsInteractor> {
        SettingsInteractorImpl(
            repository = get(),
        )
    }
}
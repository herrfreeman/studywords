package ru.blackmesa.studywords.domain

import org.koin.dsl.module

val domainModule = module {

    single<SettingsInteractor> {
        SettingsInteractorImpl(
            repository = get(),
        )
    }

    single<LibraryInteractor> {
        LibraryInteractorImpl(
            context = get(),
            reposytory = get(),
        )
    }

    single<AuthInteractor> {
        AuthInteractorImpl(
            context = get(),
            reposytory = get(),
        )
    }
}
package ru.blackmesa.studywords.ui

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel {
        AuthenticationViewModel(
            application = get(),
            networkClient = get(),
        )
    }
}
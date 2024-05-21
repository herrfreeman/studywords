package ru.blackmesa.studywords.ui

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.blackmesa.studywords.ui.authentication.AuthenticationViewModel
import ru.blackmesa.studywords.ui.library.LibraryViewModel

val uiModule2 = module {

    viewModel {
        AuthenticationViewModel(
            application = get(),
            settingsInteractor = get(),
            libraryInteractor = get(),
        )
    }

    viewModel {
        LibraryViewModel(
            application = get(),
            libInteractor = get(),
            settingsInteractor = get(),
        )
    }
}
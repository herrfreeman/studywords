package ru.blackmesa.studywords.ui

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.blackmesa.studywords.ui.library.LibraryViewModel

val uiModule2 = module {

    viewModel {
        AuthenticationViewModel(
            application = get(),
            networkClient = get(),
        )
    }

    viewModel {
        LibraryViewModel(
            application = get(),
            libInteractor = get(),
        )
    }
}
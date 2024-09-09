package ru.blackmesa.studywords.ui

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.blackmesa.studywords.data.models.WordData
import ru.blackmesa.studywords.ui.authentication.AuthenticationViewModel
import ru.blackmesa.studywords.ui.library.LibraryViewModel
import ru.blackmesa.studywords.ui.study.StudyViewModel
import ru.blackmesa.studywords.ui.words.WordsViewModel

val uiModule2 = module {

    viewModel {
        AuthenticationViewModel(
            application = get(),
            settingsInteractor = get(),
            authInteractor = get(),
            analitics = get(),
        )
    }

    viewModel {
        LibraryViewModel(
            application = get(),
            libInteractor = get(),
            settingsInteractor = get(),
            analitics = get(),
        )
    }

    viewModel {(dictionaryId: Int) ->
        WordsViewModel(
            application = get(),
            libInteractor = get(),
            dictionaryId = dictionaryId,
        )
    }

    viewModel {(wordList: List<WordData>) ->
        StudyViewModel(
            application = get(),
            libInteractor = get(),
            wordList = wordList,
        )
    }
}
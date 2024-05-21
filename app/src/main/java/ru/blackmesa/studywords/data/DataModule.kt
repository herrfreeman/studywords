package ru.blackmesa.studywords.data

import androidx.room.Room
import org.koin.dsl.module
import ru.blackmesa.studywords.data.db.AppDatabase
import ru.blackmesa.studywords.data.network.NetworkClient
import ru.blackmesa.studywords.data.network.RetrofitNetworkClient
import ru.blackmesa.studywords.data.settings.LocalSettingsStorage
import ru.blackmesa.studywords.data.settings.SettingsRepository
import ru.blackmesa.studywords.data.settings.SettingsRepositoryImpl

val dataModule = module {

    single {
        LocalSettingsStorage(
            context = get(),
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            localStorage = get(),
        )
    }

    single<NetworkClient> {
        RetrofitNetworkClient(
            context = get(),
        )
    }

    single<LibraryRepository> {
        LibraryRepositoryImpl(
            context = get(),
            settings = get(),
            networkClient = get(),
            database = get(),
        )
    }

    single {
        Room.databaseBuilder(
            //context = androidContext(),
            context = get(),
            klass = AppDatabase::class.java,
            name = "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}
package ru.blackmesa.studywords.data

import org.koin.dsl.module
import ru.blackmesa.studywords.data.network.NetworkClient
import ru.blackmesa.studywords.data.network.RetrofitNetworkClient

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
}
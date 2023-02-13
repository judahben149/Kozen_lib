package com.lovisgod.kozenlib.di

import com.lovisgod.kozenlib.core.data.dataInteractor.IswConfigSourceInteractor
import com.lovisgod.kozenlib.core.data.dataInteractor.IswDetailsAndKeySourceInteractor
import com.lovisgod.kozenlib.core.data.dataSourceImpl.IswDataConfig
import com.lovisgod.kozenlib.core.data.dataSourceImpl.IswDetailsAndKeysImpl
import com.lovisgod.kozenlib.core.data.datasource.IswConfigDataSource
import com.lovisgod.kozenlib.core.data.datasource.IswDetailsAndKeyDataSource
import org.koin.dsl.module.module

val configModule = module {
    single { IswDataConfig() }
    single { IswDetailsAndKeysImpl( get(), get() ) }
    factory<IswConfigDataSource> { return@factory IswDataConfig() }
    factory<IswDetailsAndKeyDataSource> {
        return@factory IswDetailsAndKeysImpl(
            get(),
            get()
        ) }

    single { IswConfigSourceInteractor(get()) }
    single { IswDetailsAndKeySourceInteractor( get() ) }

}
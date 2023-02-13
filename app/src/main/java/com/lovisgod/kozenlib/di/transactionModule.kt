package com.lovisgod.kozenlib.di

import com.lovisgod.kozenlib.core.data.dataInteractor.IswTransactionInteractor
import com.lovisgod.kozenlib.core.data.dataSourceImpl.IswTransactionImpl
import com.lovisgod.kozenlib.core.data.datasource.IswTransactionDataSource
import com.lovisgod.kozenlib.core.utilities.EmvHandler
import org.koin.dsl.module.module


val transactionModule = module {
    single { EmvHandler() }
    single { IswTransactionImpl( get() ) }
    factory<IswTransactionDataSource> { return@factory IswTransactionImpl( get() ) }
    single { IswTransactionInteractor(get()) }

}
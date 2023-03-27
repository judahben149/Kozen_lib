package com.lovisgod.kozenlib.di

import com.lovisgod.kozenlib.core.data.dataInteractor.IswTransactionInteractor
import com.lovisgod.kozenlib.core.data.dataSourceImpl.IswTransactionImpl
import com.lovisgod.kozenlib.core.data.datasource.IswTransactionDataSource
import com.lovisgod.kozenlib.core.utilities.CardCheckerHandler
import com.lovisgod.kozenlib.core.utilities.EmvHandler
import org.koin.dsl.module.module


val transactionModule = module {
    single { EmvHandler() }
    single { CardCheckerHandler() }
    single { IswTransactionImpl( get(), get() ) }
    factory<IswTransactionDataSource> { return@factory IswTransactionImpl( get(), get()) }
    single { IswTransactionInteractor(get()) }

}
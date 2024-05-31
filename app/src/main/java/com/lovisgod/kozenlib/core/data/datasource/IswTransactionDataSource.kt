package com.lovisgod.kozenlib.core.data.datasource

import android.content.Context
import com.lovisgod.kozenlib.core.data.dataInteractor.EMVEvents
import com.lovisgod.kozenlib.core.data.models.TransactionData
import com.lovisgod.kozenlib.core.data.utilsData.RequestIccData

interface IswTransactionDataSource {

    suspend fun startTransaction(
        hasContactless: Boolean = true,
        hasContact: Boolean = true,
        amount: Long,
        amountOther: Long,
        transType: Int,
        emvEvents: EMVEvents
    )
    suspend fun getTransactionData(): RequestIccData

    suspend fun setEmvContect(context: Context)

    suspend fun setEmvPINMODE(pinMode: Int)

    suspend fun checkCard(
        hasContactless: Boolean = true,
        hasContact: Boolean = true,
        amount: Long,
        amountOther: Long,
        transType: Int,
        emvEvents: EMVEvents
    )
}
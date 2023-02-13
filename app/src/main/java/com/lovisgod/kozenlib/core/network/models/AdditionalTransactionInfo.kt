package com.lovisgod.kozenlib.core.network.models

import android.os.Parcelable
import com.lovisgod.kozenlib.core.data.utilsData.AccountType
import com.lovisgod.kozenlib.core.data.utilsData.TransactionType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AdditionalTransactionInfo (
    var amount: String  = "",
    var surcharge: String = "",
    var extendedTransactionType: String = "6101",
    var receivingInstitutionId: String = "",
    var destinationAccountNumber: String = "",
    var fromAccount: String = "",
    var accountType: AccountType = AccountType.Default

 ): Parcelable
package com.lovisgod.kozenlib.core.network.models

import android.os.Parcelable
import com.lovisgod.kozenlib.core.data.models.TerminalInfo
import com.lovisgod.kozenlib.core.data.utilsData.RequestIccData
import com.lovisgod.kozenlib.core.data.utilsData.TransactionType
import com.lovisgod.kozenlib.core.utilities.DisplayUtilsKozen
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
data class TransactionResultData(
    val paymentType: String = "",
    val stan: String = "",
    val dateTime: String = "",
    val amount: String = "",
    val type: TransactionType,
    val cardPan: String ="",
    val cardType: Int = 0,
    val cardExpiry: String = "",
    val authorizationCode: String = "",
    var tid: String = "",
    var merchantId: String = "",
    var merchantName: String = "",
    var merchantLocation: String = "",
    val responseMessage: String = "",
    val responseCode: String = "",
    val AID: String = "",
    val code: String = "",
    val telephone: String = "",
    val txnDate: Long = 0L,
    val transactionId: String = "",
    val cardHolderName: String,
    val remoteResponseCode: String = "",
    val biller: String? = "",
    val customerDescription: String? = "",
    val surcharge: String? = "",
    val additionalAmounts: String? = "",
    var customerName: String? = "",
    var ref : String? = "",
    var accountNumber: String? = "",
    var transactionCurrencyCode: String = "566"
    //val additionalInfo: String? = ""
):Parcelable

fun createTransResultData(purchaseResponse: PurchaseResponseKozen,
                          iccData: RequestIccData,
                          transactionName: String,
                          transactionType: TransactionType,
                          terminalData: TerminalInfo
): TransactionResultData {
    return purchaseResponse?.let {
        return@let TransactionResultData(
            AID = iccData.DEDICATED_FILE_NAME,
            stan = it?.stan,
            dateTime = iccData.TRANSACTION_DATE,
            cardExpiry = "",
            cardPan = iccData.EMC_CARD_?.cardNumber?.let { it1 -> DisplayUtilsKozen.maskPan(it1) }
                .toString(),
            paymentType = transactionName,
            cardHolderName = iccData.CARD_HOLDER_NAME,
            type = transactionType,
            amount = iccData.TRANSACTION_AMOUNT,
            tid = terminalData.terminalCode,
            merchantId = terminalData.merchantId,
            merchantName = terminalData.merchantName,
            merchantLocation = terminalData.merchantAddress1 + terminalData.merchantAddress2,
            responseMessage = purchaseResponse.description,
            responseCode = purchaseResponse.responseCode,
            txnDate = Date().time
        )
    }


}

//fun createTransactionResultFromCardLess(
//                          paymentStatus: Transaction,
//                          paymentInfo: CardLessPaymentInfo,
//                          transactionName: String,
//                          transactionType: TransactionType,
//                          terminalData: TerminalInfo): TransactionResultData {
//    return paymentStatus.let {
//        val responseMsg = IsoUtils.getIsoResult(paymentStatus.responseCode)?.second
//            ?: paymentStatus.responseDescription
//            ?: "Error"
//        val now = Date()
//        return@let TransactionResultData(
//            AID = "",
//            stan = paymentInfo.currentStan,
//            dateTime = DisplayUtilsKozen.getIsoString(now),
//            cardExpiry = "",
//            cardPan = "",
//            paymentType = transactionName,
//            cardHolderName = "",
//            type = transactionType,
//            amount = ((paymentInfo.amount.toString().toInt()) *100) .toString(),
//            tid = terminalData.terminalCode,
//            merchantId = terminalData.merchantId,
//            merchantName = terminalData.merchantName,
//            merchantLocation = terminalData.merchantAddress1 + terminalData.merchantAddress2,
//            responseMessage = responseMsg,
//            responseCode = paymentStatus.responseCode,
//            txnDate = Date().time,
//            ref = paymentInfo.reference
//        )
//    }
//
//
//}




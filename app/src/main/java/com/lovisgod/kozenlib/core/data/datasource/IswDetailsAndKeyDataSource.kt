package com.lovisgod.kozenlib.core.data.datasource

import com.lovisgod.kozenlib.core.data.models.IswTerminalModel
import com.lovisgod.kozenlib.core.data.models.TerminalInfo
import com.lovisgod.kozenlib.core.network.models.TokenRequestModelKozen


interface IswDetailsAndKeyDataSource {
    suspend fun downloadTerminalDetails(terminalData: IswTerminalModel)
    suspend fun getISWToken(tokenRequestModelKozen: TokenRequestModelKozen)
    suspend fun writeDukPtKey(keyIndex: Int, keyData: String, KsnData: String): Int
    suspend fun writePinKey(keyIndex: Int, keyData: String): Int
    suspend fun loadMasterKey(masterkey: String)
    suspend fun readTerminalInfo():TerminalInfo
    suspend fun eraseKey(): Int
    suspend fun saveTerminalInfo(data: TerminalInfo)

}
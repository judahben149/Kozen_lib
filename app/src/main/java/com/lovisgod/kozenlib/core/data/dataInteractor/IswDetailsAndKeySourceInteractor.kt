package com.lovisgod.kozenlib.core.data.dataInteractor

import com.lovisgod.kozenlib.core.data.datasource.IswDetailsAndKeyDataSource
import com.lovisgod.kozenlib.core.data.models.IswTerminalModel
import com.lovisgod.kozenlib.core.data.models.TerminalInfo
import com.lovisgod.kozenlib.core.network.models.TokenRequestModelKozen

class IswDetailsAndKeySourceInteractor(
    var iswDetailsAndKeyDataSource: IswDetailsAndKeyDataSource,
    ) {
    suspend fun writeDukPtKey(keyIndex: Int, keyData: String, KsnData: String) =
        iswDetailsAndKeyDataSource.writeDukPtKey(keyIndex, keyData, KsnData)
    suspend fun writePinKey(keyIndex: Int, keyData: String) =
        iswDetailsAndKeyDataSource.writePinKey(keyIndex, keyData)
    suspend fun readTerminalInfo() =
        iswDetailsAndKeyDataSource.readTerminalInfo()
    suspend fun eraseKey() =
        iswDetailsAndKeyDataSource.eraseKey()

    suspend fun loadMasterKey(masterKey: String) = iswDetailsAndKeyDataSource.loadMasterKey(masterKey)

    suspend fun saveTerminalInfo(data: TerminalInfo) =
        iswDetailsAndKeyDataSource.saveTerminalInfo(data)

    suspend fun downloadTerminalDetails(data: IswTerminalModel) =
        iswDetailsAndKeyDataSource.downloadTerminalDetails(data)

    suspend fun getISWToken(tokenRequestModelKozen: TokenRequestModelKozen) =
        iswDetailsAndKeyDataSource.getISWToken(tokenRequestModelKozen)
}
package com.lovisgod.kozenlib.core.network.models

import com.lovisgod.kozenlib.core.data.models.AllTerminalInfo
import com.lovisgod.kozenlib.core.data.models.TerminalInfo
import com.lovisgod.kozenlib.core.data.models.TmsRouteTypeConfig
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "allTerminalInfo", strict = false)
data class ConfigResponseKozen (
    @field:Element(name = "responseCode", required = false)
    var responseCode: String = "",

    @field:Element(name = "responseMessage", required = false)
    var responseMessage: String = "",

    @field:Element(name = "data", required = false)
    var data: Data? = null,

)

data class Data (
    @field:ElementList(name = "terminalAllowedTxTypes", inline=true, required = false)
    var terminalAllowedTxTypes: List<String>? = null,

    @field:Element(name = "terminalInfo", required = false)
    var terminalInfo: TerminalInfo? = null,

    @field:Element(name = "tmsRouteTypeConfig", required = false)
    var tmsRouteTypeConfig: TmsRouteTypeConfig? = null
)

 fun convertConfigResponseToAllTerminalInfo(auto: ConfigResponseKozen): AllTerminalInfo {
    val allInfo = AllTerminalInfo()
    allInfo.responseCode = auto.responseCode
    allInfo.responseMessage = auto.responseMessage
    allInfo.terminalAllowedTxTypes = auto.data?.terminalAllowedTxTypes
    allInfo.terminalInfo = auto.data?.terminalInfo
    allInfo.tmsRouteTypeConfig = auto.data?.tmsRouteTypeConfig

    return allInfo
}



package com.lovisgod.kozenlib.core.utilities

import com.lovisgod.kozenlib.core.utilities.BerTlv

object BerTlvLogger {
    fun log(
        aPadding: String?,
        aTlv: com.lovisgod.kozenlib.core.utilities.BerTlvs,
        aLogger: com.lovisgod.kozenlib.core.utilities.IBerTlvLogger?
    ) {
        for (tlv in aTlv.list) {
            if (aPadding != null) {
                if (aLogger != null) {
                    log(aPadding, tlv, aLogger)
                }
            }
        }
    }

    fun log(
        aPadding: String,
        aTlv: BerTlv?,
        aLogger: com.lovisgod.kozenlib.core.utilities.IBerTlvLogger
    ) {
        if (aTlv == null) {
            aLogger.debug("{} is null", aPadding)
            return
        }
        if (aTlv.isConstructed() == true) {
            aLogger.debug(
                "{} [{}]", aPadding, com.lovisgod.kozenlib.core.utilities.HexUtil.toHexString(
                    aTlv.getTag()!!.bytes
                )
            )
            for (child in aTlv.getValues()!!) {
                log("$aPadding    ", child, aLogger)
            }
        } else {
            aLogger.debug(
                "{} [{}] {}", aPadding, com.lovisgod.kozenlib.core.utilities.HexUtil.toHexString(
                    aTlv.getTag()!!.bytes
                ), aTlv.getHexValue()
            )
        }
    }
}
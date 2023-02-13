package com.lovisgod.kozenlib.core.data.models

import com.lovisgod.kozenlib.core.data.utilsData.Constants.ISW_DUKPT_KSN

data class EmvPinData (
    var ksn : String = ISW_DUKPT_KSN,
    var CardPinBlock: String = "")
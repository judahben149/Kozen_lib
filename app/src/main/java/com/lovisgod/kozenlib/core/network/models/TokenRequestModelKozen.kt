package com.lovisgod.kozenlib.core.network.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Parcelize
@Root(name = "tokenPassportRequest", strict = false)
data class TokenRequestModelKozen (
    @field:Element(name = "terminalInformation", required = false)
    var terminalInformation: TerminalInformationRequestKozen? = null
): Parcelable
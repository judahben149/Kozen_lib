package com.lovisgod.kozenlib.core.network.models

import com.lovisgod.kozenlib.core.data.utilsData.Constants
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Track2Kozen", strict = false)
class PinDataKozen {
    @field:Element(name = "ksnd", required = false)
    var ksnd: String = "605"

    @field:Element(name = "pinType", required = false)
    var pinType: String = "Dukpt"

    @field:Element(name = "ksn", required = false)
    var ksn: String = ""

    @field:Element(name = "pinBlock", required = false)
    var pinBlock: String = ""

    companion object {
        fun create(pinInfo: MemoryPinData) = PinDataKozen().apply {
            ksn = pinInfo.ksn
            pinBlock = pinInfo.pinBlock
        }

    }
}


data class MemoryPinData(
    var pinBlock: String = "",
    var type: String = "",
    var ksn: String = Constants.ISW_DUKPT_KSN,
    var ksnd: String = "605"
)

object StringManipulator{
    fun dropLastCharacter(values: String): String {
        if (values.isNotEmpty()) {
            val xxx = values.dropLast(1)
            return  xxx
        } else {
            return values
        }
    }


    fun dropFirstCharacter(values: String): String {
        if (values.isNotEmpty()) {
            val xxx = values.drop(4)
            println("this is usable string : ${xxx}")
            return xxx
        } else {
            return values
        }

    }
}

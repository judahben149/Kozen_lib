package com.lovisgod.kozenlib.core.network.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Track2Kozen", strict = false)
class Track2Kozen {
    @field:Element(name = "pan", required = false)
    var pan: String = ""

    @field:Element(name = "expiryMonth", required = false)
    var expiryMonth: String = ""

    @field:Element(name = "expiryYear", required = false)
    var expiryYear: String = ""

    @field:Element(name = "track2Kozen", required = false)
    var track2: String = ""
}
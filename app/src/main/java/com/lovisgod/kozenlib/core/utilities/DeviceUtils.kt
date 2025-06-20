package com.lovisgod.kozenlib.core.utilities

sealed class Model {
    /**
    L200 is the P5 device
     */
    object L200 : Model()
    object P10 : Model()
    object P13 : Model()
    object P3 : Model()
    object N4 : Model()
    object UNKNOWN : Model()
}

fun getDeviceModel(): Model {
    val name = android.os.Build.MODEL

    return when {
        name.contains("L200", ignoreCase = true) -> Model.L200
        name.contains("P10", ignoreCase = true) -> Model.P10
        name.contains("P13", ignoreCase = true) -> Model.P13
        name.contains("P3", ignoreCase = true) -> Model.P3
        name.contains("N4", ignoreCase = true) -> Model.N4
        else -> Model.UNKNOWN
    }
}
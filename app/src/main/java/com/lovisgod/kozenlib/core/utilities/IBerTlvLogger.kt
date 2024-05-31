package com.lovisgod.kozenlib.core.utilities

interface IBerTlvLogger {
    val isDebugEnabled: Boolean
    fun debug(aFormat: String?, vararg args: Any?)
}
package com.lovisgod.kozenlib.core.data.dataInteractor

interface PrinterEvent {
     fun onPrintSuccess(code: Int)
     fun onPrintFail(code: Int)
     fun onPrintStart()
}
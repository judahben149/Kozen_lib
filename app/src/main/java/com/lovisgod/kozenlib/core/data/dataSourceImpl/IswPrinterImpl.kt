package com.lovisgod.kozenlib.core.data.dataSourceImpl

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.lovisgod.kozenlib.core.data.dataInteractor.PrinterEvent
import com.lovisgod.kozenlib.core.data.datasource.IswPrinterDataSource
import com.pos.sdk.printer.POIPrinterManager
import com.pos.sdk.printer.models.BitmapPrintLine
import com.pos.sdk.printer.models.PrintLine
import com.pos.sdk.printer.models.TextPrintLine

class IswPrinterImpl: IswPrinterDataSource {
    lateinit var context: Context
    lateinit var printerManager: POIPrinterManager
    lateinit var printerEvent: PrinterEvent

    override suspend fun print(context: Context,
                               printerEvent: PrinterEvent, bitmapx: Bitmap) {
        this.context = context
        this.printerManager  = POIPrinterManager(context)
        this.printerEvent = printerEvent

        printerManager.open()
        val state = printerManager.printerState
        Log.d("printer kozen", "printer state = $state")
        //printerManager.setPrintFont("/system/fonts/Android-1.ttf");
        //printerManager.setPrintFont("/system/fonts/Android-1.ttf");
        printerManager.setPrintGray(5)
        printerManager.setLineSpace(5)
        //printerManager.cleanCache();
        //printerManager.cleanCache();
        val str1 = "This is an example of a receipt"
        val p1: PrintLine = TextPrintLine(str1, PrintLine.CENTER)
        printerManager.addPrintLine(p1)
//        val bitmap =
        printerManager.addPrintLine(BitmapPrintLine(bitmapx, PrintLine.CENTER))

        if (state == 4) {
            printerManager.close()
            this.printerEvent.onPrintFail(4)
            return
        }
        printerManager.beginPrint(listener)
    }

    var listener: POIPrinterManager.IPrinterListener = object : POIPrinterManager.IPrinterListener {
        override fun onStart()  {
            this@IswPrinterImpl.printerEvent.onPrintStart()
        }
        override fun onFinish() {
            //printerManager.cleanCache();
            printerManager.close()
            this@IswPrinterImpl.printerEvent.onPrintSuccess(0)
        }

        override fun onError(code: Int, msg: String) {
            Log.e("POIPrinterManager", "code: " + code + "msg: " + msg)
            printerManager.close()
            this@IswPrinterImpl.printerEvent.onPrintFail(4)
        }
    }
}
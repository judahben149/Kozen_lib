package com.lovisgod.kozenlib.core.data.datasource

import android.content.Context
import android.graphics.Bitmap
import com.lovisgod.kozenlib.core.data.dataInteractor.PrinterEvent

interface IswPrinterDataSource {

    suspend fun print(context: Context, printerEvent: PrinterEvent, bitmap: Bitmap)
}
package com.lovisgod.kozenlib.core.data.dataInteractor

import android.content.Context
import android.graphics.Bitmap
import com.lovisgod.kozenlib.core.data.datasource.IswPrinterDataSource

class IswPrinterInteractor(val iswPrinterDataSource: IswPrinterDataSource) {

    suspend fun print(context: Context, printerEvent: PrinterEvent, bitmap: Bitmap) =
        iswPrinterDataSource.print(context, printerEvent, bitmap)
}
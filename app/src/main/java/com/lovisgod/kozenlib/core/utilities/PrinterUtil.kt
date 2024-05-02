package com.lovisgod.kozenlib.core.utilities

import android.graphics.Bitmap

class PrinterUtil {

     fun generateTestBitmap(): Bitmap? {
        //Print Logo
        val combBitmap = CombBitmap()
//        val bitmap: Bitmap
//        bitmap =
//        combBitmap.addBitmap(bitmap)
        //Title
//        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Horizon Test Slip", 50, GenerateBitmap.AlignEnum.CENTER, true, false));
        combBitmap.addBitmap(GenerateBitmap.generateLine(1)) // print one line

        //Content
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Terminal ID:", "123456", 26, true, false))
        combBitmap.addBitmap(
            GenerateBitmap.str2Bitmap(
                "Merchant ID:",
                "123456789012345",
                26,
                true,
                false
            )
        )
        combBitmap.addBitmap(
            GenerateBitmap.str2Bitmap(
                "Merchant Name",
                "Demo Test",
                26,
                true,
                false
            )
        )
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Operator ID:", "01", 26, true, false))
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Issuer code", "12345", 26, true, false))
        combBitmap.addBitmap(
            GenerateBitmap.str2Bitmap(
                "Card NO.",
                26,
                GenerateBitmap.AlignEnum.LEFT,
                true,
                false
            )
        )
        combBitmap.addBitmap(
            GenerateBitmap.str2Bitmap(
                "62179380*****7654",
                36,
                GenerateBitmap.AlignEnum.RIGHT,
                true,
                false
            )
        )
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Stan code:", "000012", 26, true, false))
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Batch NO.:", "000034", 26, true, false))
        combBitmap.addBitmap(
            GenerateBitmap.str2Bitmap(
                "Date/Time:",
                "2019-09-10 17:06:56",
                26,
                true,
                false
            )
        )
        combBitmap.addBitmap(
            GenerateBitmap.str2Bitmap(
                "Reference NO.:",
                "987654324567",
                26,
                true,
                false
            )
        )
        combBitmap.addBitmap(
            GenerateBitmap.str2Bitmap(
                "Amount:",
                26,
                GenerateBitmap.AlignEnum.LEFT,
                true,
                false
            )
        )
        combBitmap.addBitmap(
            GenerateBitmap.str2Bitmap(
                "USD 123456.89",
                40,
                GenerateBitmap.AlignEnum.RIGHT,
                true,
                true
            )
        )
        combBitmap.addBitmap(
            GenerateBitmap.str2Bitmap(
                "--------------------------------------",
                20,
                GenerateBitmap.AlignEnum.CENTER,
                true,
                false
            )
        ) // 打印一行直线
        combBitmap.addBitmap(
            GenerateBitmap.str2Bitmap(
                "Card Holder Signature:",
                20,
                GenerateBitmap.AlignEnum.LEFT,
                true,
                false
            )
        )
        combBitmap.addBitmap(GenerateBitmap.generateGap(60)) // print row gap
        combBitmap.addBitmap(GenerateBitmap.generateLine(1)) // print one line
        combBitmap.addBitmap(
            GenerateBitmap.formatBitmap(
                GenerateBitmap.generateQRCodeBitmap(
                    "123545678901235456789012354567890",
                    200
                ), GenerateBitmap.AlignEnum.CENTER
            )
        )
        combBitmap.addBitmap(
            GenerateBitmap.formatBitmap(
                GenerateBitmap.generateBarCodeBitmap(
                    "123456",
                    260,
                    50
                ), GenerateBitmap.AlignEnum.CENTER
            )
        )
        combBitmap.addBitmap(
            GenerateBitmap.str2Bitmap(
                "--X--X--X--X--X--X--X--X--X--X--X--X",
                20,
                GenerateBitmap.AlignEnum.CENTER,
                true,
                false
            )
        )
        combBitmap.addBitmap(GenerateBitmap.generateGap(60)) // print row gap
        return combBitmap.combBitmap
    }
}
package com.lovisgod.kozenlib.core.network

import com.lovisgod.kozenlib.core.data.utilsData.Constants
import com.lovisgod.kozenlib.core.network.models.ConfigResponseKozen
import com.lovisgod.kozenlib.core.utilities.simplecalladapter.Simple
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface AuthInterfaceKozen
{
    @Headers("Content-Type: application/xml")
    @GET(Constants.KIMONO_MERCHANT_DETAILS_END_POINT_AUTO)
    fun getMerchantDetails(@Path("terminalSerialNo") terminalSerialNo: String):
            Simple<ConfigResponseKozen>

}
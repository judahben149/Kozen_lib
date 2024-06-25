package com.lovisgod.kozenlib.core.data.dataSourceImpl

import android.content.Context
import android.os.Bundle
import com.interswitchng.smartpos.shared.utilities.console
import com.lovisgod.kozenlib.core.data.datasource.IswConfigDataSource
import com.lovisgod.kozenlib.core.data.models.TerminalInfo
import com.lovisgod.kozenlib.core.utilities.BerTag
import com.lovisgod.kozenlib.core.utilities.BerTlv
import com.lovisgod.kozenlib.core.utilities.BerTlvBuilder
import com.lovisgod.kozenlib.core.utilities.HexUtil
import com.lovisgod.kozenlib.core.utilities.PrefUtils
import com.pos.sdk.emvcore.*
import com.pos.sdk.emvcore.POIEmvCoreManager.*
import com.pos.sdk.utils.PosUtils
import java.util.ArrayList
import kotlin.experimental.or

class IswDataConfig(private val context: Context) : IswConfigDataSource {

    override suspend fun loadTerminal(terminalData: TerminalInfo) {
        console.log("load terminal info", "load terminal info")
        val emvCoreManager = getDefault()
        val bundle = Bundle()

        // EMV TAG 9F1C Len 8
        bundle.putByteArray(EmvTerminalConstraints.TERMINAL_ID, terminalData.terminalCode.toByteArray())
        // EMV TAG 9F16 Len 15
        bundle.putByteArray(EmvTerminalConstraints.MERCHANT_ID, terminalData.merchantId.toByteArray())
        // EMV TAG 9F15 Len 4
        bundle.putByteArray(EmvTerminalConstraints.MERCHANT_CATEGORY_CODE, terminalData.merchantCategoryCode.toByteArray())
        // EMV TAG 9F4E Len V
        bundle.putByteArray(EmvTerminalConstraints.MERCHANT_NAME, terminalData.merchantName.toByteArray())
        // EMV TAG 9F1A Len 4
        bundle.putByteArray(EmvTerminalConstraints.TERMINAL_COUNTRY_CODE, terminalData.terminalCountryCode.toByteArray())
        // EMV TAG 5F2A Len 4
        bundle.putByteArray(EmvTerminalConstraints.TRANS_CURRENCY_CODE, terminalData.transCurrencyCode.toByteArray())
        // EMV TAG 5F36 Len 2
        bundle.putByteArray(EmvTerminalConstraints.TRANS_CURRENCY_EXP, terminalData.transCurrencyExp.toByteArray())
        // EMV TAG 9F3C Len 4
        bundle.putByteArray(EmvTerminalConstraints.TRANS_REFER_CURRENCY_CODE, terminalData.transCurrencyCode.toByteArray())
        // EMV TAG 9F3D Len 2
        bundle.putByteArray(EmvTerminalConstraints.TRANS_REFER_CURRENCY_EXP, terminalData.transCurrencyExp.toByteArray())

        // EMV TAG 9F35 Len 2
        bundle.putByteArray(EmvTerminalConstraints.TERMINAL_TYPE, terminalData.terminalType.toByteArray())
        // EMV TAG 9F33 Len 6
        bundle.putByteArray(EmvTerminalConstraints.TERMINAL_CAPABILITY, terminalData.terminalCapabilities.toByteArray())
        // EMV TAG 9F40 Len 10
        bundle.putByteArray(
            EmvTerminalConstraints.TERMINAL_EX_CAPABILITY,
            terminalData.terminalExtCapabilities.toByteArray()
        )
        // EMV TAG 9F1E Len 8
//        bundle.putByteArray(EmvTerminalConstraints.IFD_SERIAL_NUMBER,
//                POIGeneralAPI.getDefault().getVersion(POIGeneralAPI.VERSION_TYPE_DSN).getBytes());
        // EMV TAG 9F39 Len 2
        bundle.putByteArray(EmvTerminalConstraints.TERMINAL_ENTRY_MODE, terminalData.terminalEntryMode.toByteArray())

        // EMV default configuration.
        bundle.putBoolean(EmvTerminalConstraints.PSE, true)
        bundle.putBoolean(EmvTerminalConstraints.CARD_HOLDER_CONFIRM, true)
        bundle.putBoolean(EmvTerminalConstraints.LANGUAGE_SELECT, true)
        bundle.putBoolean(EmvTerminalConstraints.DEFAULT_DDOL, true)
        bundle.putBoolean(EmvTerminalConstraints.DEFAULT_TDOL, true)
        bundle.putBoolean(EmvTerminalConstraints.BYPASS_PIN_ENTRY, true)
        bundle.putBoolean(EmvTerminalConstraints.SUBSEQUENT_BYPASS_PIN_ENTRY, true)
        bundle.putBoolean(EmvTerminalConstraints.GET_DATA_FOR_PIN_COUNTER, true)
        bundle.putBoolean(EmvTerminalConstraints.FLOOR_LIMIT_CHECKING, true)
        bundle.putBoolean(EmvTerminalConstraints.RANDOM_TRANSACTION_SELECTION, true)
        bundle.putBoolean(EmvTerminalConstraints.VELOCITY_CHECKING, true)
        bundle.putBoolean(EmvTerminalConstraints.EXCEPTION_FILE, true)
        bundle.putBoolean(EmvTerminalConstraints.REVOCATION_ISSUER_PUBLIC_KEY, true)
        bundle.putBoolean(EmvTerminalConstraints.ISSUER_REFERRAL, false)
        bundle.putBoolean(EmvTerminalConstraints.UNABLE_TO_GO_ONLINE, false)

        // Mandatory online, mandatory approval.
        bundle.putBoolean(EmvTerminalConstraints.FORCED_ONLINE, false)
        bundle.putBoolean(EmvTerminalConstraints.FORCED_ACCEPT, false)
        emvCoreManager.EmvSetTerminal(EmvTerminalConstraints.TYPE_TERMINAL, bundle)
    }

    override suspend fun loadAid() {
        console.log("load terminal aid", "load terminal aid")
        val emvCoreManager = getDefault()
        emvCoreManager.EmvDeleteAid()
        var aid: PosEmvAid

//        aid.AcquirerId = PosUtils.hexStringToBytes("A00000000001");
//        aid.TerminalCountryCode = PosUtils.hexStringToBytes("0156");
//        aid.MerchantCategoryCode = PosUtils.hexStringToBytes("0156");
//        aid.TransCurrencyCode = PosUtils.hexStringToBytes("0156");
//        aid.TransCurrencyExp = PosUtils.hexStringToBytes("02");
//        aid.TerminalType = PosUtils.hexStringToBytes("22");
//        aid.TerminalCapabilities = PosUtils.hexStringToBytes("E0F8C8");
//        aid.TerminalRiskManagementData = PosUtils.hexStringToBytes("0000000000000000");
//        aid.AdditionalTerminalCapabilities = PosUtils.hexStringToBytes("F000F0F001");

        // VISA
        aid = addAid("A0000000031010", "008C")
        aid.ContactlessTransLimit = PrefUtils.getContactlessTransLimit(context).toInt()
        aid.ContactlessCVMLimit =  PrefUtils.getContactlessCvmLimit(context).toInt()
        aid.ContactlessFloorLimit =  PrefUtils.getContactlessFloorLimit(context).toInt()
        emvCoreManager.EmvSetAid(aid)
        aid = addAid("A0000000032010", "008C")
        aid.ContactlessTransLimit =  PrefUtils.getContactlessTransLimit(context).toInt()
        aid.ContactlessCVMLimit =  PrefUtils.getContactlessCvmLimit(context).toInt()
        aid.ContactlessFloorLimit =  PrefUtils.getContactlessFloorLimit(context).toInt()
        emvCoreManager.EmvSetAid(aid)
        aid = addAid("A0000000033010", "008C")
        aid.ContactlessTransLimit =  PrefUtils.getContactlessTransLimit(context).toInt()
        aid.ContactlessCVMLimit =  PrefUtils.getContactlessCvmLimit(context).toInt()
        aid.ContactlessFloorLimit =  PrefUtils.getContactlessFloorLimit(context).toInt()
        emvCoreManager.EmvSetAid(aid)

        // Unionpay
        aid = addAid("A000000333010101", "0020")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000")
        aid.TACOnline = PosUtils.hexStringToBytes("d84004f800")
        aid.TACDefault = PosUtils.hexStringToBytes("084000a800")
        emvCoreManager.EmvSetAid(aid)
        aid = addAid("A000000333010102", "0020")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000")
        aid.TACOnline = PosUtils.hexStringToBytes("d84004f800")
        aid.TACDefault = PosUtils.hexStringToBytes("084000a800")
        emvCoreManager.EmvSetAid(aid)
        aid = addAid("A000000333010103", "0020")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000")
        aid.TACOnline = PosUtils.hexStringToBytes("d84004f800")
        aid.TACDefault = PosUtils.hexStringToBytes("084000a800")
        emvCoreManager.EmvSetAid(aid)
        aid = addAid("A000000333010106", "0020")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000")
        aid.TACOnline = PosUtils.hexStringToBytes("d84004f800")
        aid.TACDefault = PosUtils.hexStringToBytes("084000a800")
        emvCoreManager.EmvSetAid(aid)

        // MasterCard
        aid = addAid("A00000000410", "0002")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0400000000")
        aid.TACOnline = PosUtils.hexStringToBytes("f850acf800")
        aid.TACDefault = PosUtils.hexStringToBytes("fc50aca000")
        aid.TerminalRiskManagementData = PosUtils.hexStringToBytes("6C00000000000000")
        aid.ContactlessTransLimit =  PrefUtils.getContactlessTransLimit(context).toInt()
        aid.ContactlessCVMLimit =  PrefUtils.getContactlessCvmLimit(context).toInt()
        aid.ContactlessFloorLimit =  PrefUtils.getContactlessFloorLimit(context).toInt()
        aid.TerminalRiskManagementData = PosUtils.hexStringToBytes("4C00800000000000")
        emvCoreManager.EmvSetAid(aid)

        aid = addAid("A00000000430", "0002")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0400000000")
        aid.TACOnline = PosUtils.hexStringToBytes("f850acf800")
        aid.TACDefault = PosUtils.hexStringToBytes("fc50aca000")
        aid.ContactlessTransLimit =  PrefUtils.getContactlessTransLimit(context).toInt()
        aid.ContactlessCVMLimit =  PrefUtils.getContactlessCvmLimit(context).toInt()
        aid.ContactlessFloorLimit =  PrefUtils.getContactlessFloorLimit(context).toInt()
        aid.TerminalRiskManagementData = PosUtils.hexStringToBytes("4C00800000000000")
        emvCoreManager.EmvSetAid(aid)

        aid = addAid("A0000000043060", "0002")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0000800000")
        aid.TACOnline = PosUtils.hexStringToBytes("fc50bcf800")
        aid.TACDefault = PosUtils.hexStringToBytes("fc50bca000")
        aid.TerminalRiskManagementData = PosUtils.hexStringToBytes("4C00800000000000")
        aid.ContactlessTransLimit =  PrefUtils.getContactlessTransLimit(context).toInt()
        aid.ContactlessCVMLimit =  PrefUtils.getContactlessCvmLimit(context).toInt()
        aid.ContactlessFloorLimit =  PrefUtils.getContactlessFloorLimit(context).toInt()
        emvCoreManager.EmvSetAid(aid)

        aid = addAid("A0000000041010", "0002")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0000000000")
        aid.TACOnline = PosUtils.hexStringToBytes("fc50808800")
        aid.TACDefault = PosUtils.hexStringToBytes("fc50b8a000")
        aid.TerminalRiskManagementData = PosUtils.hexStringToBytes("6C00000000000000")
        aid.ContactlessTransLimit =  PrefUtils.getContactlessTransLimit(context).toInt()
        aid.ContactlessCVMLimit =  PrefUtils.getContactlessCvmLimit(context).toInt()
        aid.ContactlessFloorLimit =  PrefUtils.getContactlessFloorLimit(context).toInt()
        emvCoreManager.EmvSetAid(aid)

        // Discover
        aid = addAid("A0000001523010", "0001")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000")
        aid.TACOnline = PosUtils.hexStringToBytes("fce09cf800")
        aid.TACDefault = PosUtils.hexStringToBytes("dc00002000")
        emvCoreManager.EmvSetAid(aid)
        aid = addAid("A0000001524010", "0001")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000")
        aid.TACOnline = PosUtils.hexStringToBytes("fce09cf800")
        aid.TACDefault = PosUtils.hexStringToBytes("dc00002000")
        emvCoreManager.EmvSetAid(aid)

        // AMEX
        aid = addAid("A00000002501", "0001")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0000000000")
        aid.TACOnline = PosUtils.hexStringToBytes("c800000000")
        aid.TACDefault = PosUtils.hexStringToBytes("c800000000")
        aid.ContactlessTransLimit =  PrefUtils.getContactlessTransLimit(context).toInt()
        aid.ContactlessCVMLimit =  PrefUtils.getContactlessCvmLimit(context).toInt()
        aid.ContactlessFloorLimit =  PrefUtils.getContactlessFloorLimit(context).toInt()
        emvCoreManager.EmvSetAid(aid)

        // RuPay
        aid = addAid("A0000005241010", "0064")
        emvCoreManager.EmvSetAid(aid)

        // MIR
        aid = addAid("A0000006581010", "0100")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000")
        aid.TACOnline = PosUtils.hexStringToBytes("fc60acf800")
        aid.TACDefault = PosUtils.hexStringToBytes("fc60242800")
        emvCoreManager.EmvSetAid(aid)
        aid = addAid("A0000006581099", "0100")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000")
        aid.TACOnline = PosUtils.hexStringToBytes("fc60acf800")
        aid.TACDefault = PosUtils.hexStringToBytes("fc60242800")
        emvCoreManager.EmvSetAid(aid)
        aid = addAid("A0000006582010", "0100")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000")
        aid.TACOnline = PosUtils.hexStringToBytes("fc60acf800")
        aid.TACDefault = PosUtils.hexStringToBytes("fc60242800")
        emvCoreManager.EmvSetAid(aid)

        // JCB
        aid = addAid("A0000000651010", "0021")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0010000000")
        aid.TACOnline = PosUtils.hexStringToBytes("fc60acf800")
        aid.TACDefault = PosUtils.hexStringToBytes("fc60242800")
        emvCoreManager.EmvSetAid(aid)


        // Verve
        aid = addAid("A0000003710002", "0096")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0000008000")
        aid.TACOnline = PosUtils.hexStringToBytes("BCF8049800")
        aid.TACDefault = PosUtils.hexStringToBytes("BCF8049800")
        aid.ContactlessTransLimit =  PrefUtils.getContactlessTransLimit(context).toInt()
        aid.ContactlessCVMLimit =  PrefUtils.getContactlessCvmLimit(context).toInt()
        aid.ContactlessFloorLimit =  PrefUtils.getContactlessFloorLimit(context).toInt()
        aid.TerminalRiskManagementData = PosUtils.hexStringToBytes("6C00800000000000")
        emvCoreManager.EmvSetAid(aid)

        aid = addAid("A0000003710001", "0001")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0400000000")
        aid.TACOnline = PosUtils.hexStringToBytes("f850acf800")
        aid.TACDefault = PosUtils.hexStringToBytes("fc50aca000")
        aid.ContactlessTransLimit =  PrefUtils.getContactlessTransLimit(context).toInt()
        aid.ContactlessCVMLimit =  PrefUtils.getContactlessCvmLimit(context).toInt()
        aid.ContactlessFloorLimit =  PrefUtils.getContactlessFloorLimit(context).toInt()
        aid.TerminalRiskManagementData = PosUtils.hexStringToBytes("4C00800000000000")
        emvCoreManager.EmvSetAid(aid)

        // Afrigo
        aid = addAid("A000000891010101", "0001")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0000008000")
        aid.TACOnline = PosUtils.hexStringToBytes("BCF8049800")
        aid.TACDefault = PosUtils.hexStringToBytes("BCF8049800")
        aid.ContactlessTransLimit =  PrefUtils.getContactlessTransLimit(context).toInt()
        aid.ContactlessCVMLimit =  PrefUtils.getContactlessCvmLimit(context).toInt()
        aid.ContactlessFloorLimit =  PrefUtils.getContactlessFloorLimit(context).toInt()
        aid.TerminalRiskManagementData = PosUtils.hexStringToBytes("6C00800000000000")
        emvCoreManager.EmvSetAid(aid)

        aid = addAid("A000000891010102", "0001")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0400000000")
        aid.TACOnline = PosUtils.hexStringToBytes("f850acf800")
        aid.TACDefault = PosUtils.hexStringToBytes("fc50aca000")
        aid.ContactlessTransLimit =  PrefUtils.getContactlessTransLimit(context).toInt()
        aid.ContactlessCVMLimit =  PrefUtils.getContactlessCvmLimit(context).toInt()
        aid.ContactlessFloorLimit =  PrefUtils.getContactlessFloorLimit(context).toInt()
        aid.TerminalRiskManagementData = PosUtils.hexStringToBytes("4C00800000000000")
        emvCoreManager.EmvSetAid(aid)

        aid = addAid("A000000891010103", "0001")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0000008000")
        aid.TACOnline = PosUtils.hexStringToBytes("BCF8049800")
        aid.TACDefault = PosUtils.hexStringToBytes("BCF8049800")
        aid.ContactlessTransLimit =  PrefUtils.getContactlessTransLimit(context).toInt()
        aid.ContactlessCVMLimit =  PrefUtils.getContactlessCvmLimit(context).toInt()
        aid.ContactlessFloorLimit =  PrefUtils.getContactlessFloorLimit(context).toInt()
        aid.TerminalRiskManagementData = PosUtils.hexStringToBytes("6C00800000000000")
        emvCoreManager.EmvSetAid(aid)

        aid = addAid("A000000891010104", "0001")
        aid.dDOL = PosUtils.hexStringToBytes("9F3704")
        aid.tDOL = PosUtils.hexStringToBytes("9F3704")
        aid.TACDenial = PosUtils.hexStringToBytes("0400000000")
        aid.TACOnline = PosUtils.hexStringToBytes("f850acf800")
        aid.TACDefault = PosUtils.hexStringToBytes("fc50aca000")
        aid.ContactlessTransLimit =  PrefUtils.getContactlessTransLimit(context).toInt()
        aid.ContactlessCVMLimit =  PrefUtils.getContactlessCvmLimit(context).toInt()
        aid.ContactlessFloorLimit =  PrefUtils.getContactlessFloorLimit(context).toInt()
        aid.TerminalRiskManagementData = PosUtils.hexStringToBytes("4C00800000000000")
        emvCoreManager.EmvSetAid(aid)
    }

    private fun addAid(aid: String, version: String): PosEmvAid {
        val appList = PosEmvAid()
        appList.AID = PosUtils.hexStringToBytes(aid)
        appList.Version = PosUtils.hexStringToBytes(version)
        appList.SelectIndicator = true
        appList.dDOL = PosUtils.hexStringToBytes("9F0206")
        appList.tDOL = PosUtils.hexStringToBytes("9F3704")
        appList.TACDenial = PosUtils.hexStringToBytes("0010000000")
        appList.TACOnline = PosUtils.hexStringToBytes("dc4004f800")
        appList.TACDefault = PosUtils.hexStringToBytes("dc4000a800")
        appList.Threshold = 10000
        appList.TargetPercentage = 0
        appList.MaxTargetPercentage = 99
        appList.FloorLimit = 0
        appList.ContactlessTransLimit = 200000
        appList.ContactlessCVMLimit = 200000
        appList.ContactlessFloorLimit = 0
        appList.DynamicTransLimit = 200000
        return appList
    }

    override suspend fun loadCapk() {
        val emvCoreManager = getDefault()
        emvCoreManager.EmvDeleteCapk()
        var capk: PosEmvCapk?
        capk = addCapk(
            "A000000003", "01",
            "C696034213D7D8546984579D1D0F0EA519CFF8DEFFC429354CF3A871A6F7183F1228DA5C7470C055387100CB935A712C4E2864DF5D64BA93FE7E63E71F25B1E5F5298575EBE1C63AA617706917911DC2A75AC28B251C7EF40F2365912490B939BCA2124A30A28F54402C34AECA331AB67E1E79B285DD5771B5D9FF79EA630B75",
            "03", "D34A6A776011C7E7CE3AEC5F03AD2F8CFC5503CC"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000003", "07",
            "A89F25A56FA6DA258C8CA8B40427D927B4A1EB4D7EA326BBB12F97DED70AE5E4480FC9C5E8A972177110A1CC318D06D2F8F5C4844AC5FA79A4DC470BB11ED635699C17081B90F1B984F12E92C1C529276D8AF8EC7F28492097D8CD5BECEA16FE4088F6CFAB4A1B42328A1B996F9278B0B7E3311CA5EF856C2F888474B83612A82E4E00D0CD4069A6783140433D50725F",
            "03", "B4BC56CC4E88324932CBC643D6898F6FE593B172"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000003", "08",
            "D9FD6ED75D51D0E30664BD157023EAA1FFA871E4DA65672B863D255E81E137A51DE4F72BCC9E44ACE12127F87E263D3AF9DD9CF35CA4A7B01E907000BA85D24954C2FCA3074825DDD4C0C8F186CB020F683E02F2DEAD3969133F06F7845166ACEB57CA0FC2603445469811D293BFEFBAFAB57631B3DD91E796BF850A25012F1AE38F05AA5C4D6D03B1DC2E568612785938BBC9B3CD3A910C1DA55A5A9218ACE0F7A21287752682F15832A678D6E1ED0B",
            "03", "20D213126955DE205ADC2FD2822BD22DE21CF9A8"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000003", "09",
            "9D912248DE0A4E39C1A7DDE3F6D2588992C1A4095AFBD1824D1BA74847F2BC4926D2EFD904B4B54954CD189A54C5D1179654F8F9B0D2AB5F0357EB642FEDA95D3912C6576945FAB897E7062CAA44A4AA06B8FE6E3DBA18AF6AE3738E30429EE9BE03427C9D64F695FA8CAB4BFE376853EA34AD1D76BFCAD15908C077FFE6DC5521ECEF5D278A96E26F57359FFAEDA19434B937F1AD999DC5C41EB11935B44C18100E857F431A4A5A6BB65114F174C2D7B59FDF237D6BB1DD0916E644D709DED56481477C75D95CDD68254615F7740EC07F330AC5D67BCD75BF23D28A140826C026DBDE971A37CD3EF9B8DF644AC385010501EFC6509D7A41",
            "03", "1FF80A40173F52D7D27E0F26A146A1C8CCB29046"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000003", "57",
            "942B7F2BA5EA307312B63DF77C5243618ACC2002BD7ECB74D821FE7BDC78BF28F49F74190AD9B23B9713B140FFEC1FB429D93F56BDC7ADE4AC075D75532C1E590B21874C7952F29B8C0F0C1CE3AEEDC8DA25343123E71DCF86C6998E15F756E3",
            "010001", "251A5F5DE61CF28B5C6E2B5807C0644A01D46FF5"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000003", "92",
            "996AF56F569187D09293C14810450ED8EE3357397B18A2458EFAA92DA3B6DF6514EC060195318FD43BE9B8F0CC669E3F844057CBDDF8BDA191BB64473BC8DC9A730DB8F6B4EDE3924186FFD9B8C7735789C23A36BA0B8AF65372EB57EA5D89E7D14E9C7B6B557460F10885DA16AC923F15AF3758F0F03EBD3C5C2C949CBA306DB44E6A2C076C5F67E281D7EF56785DC4D75945E491F01918800A9E2DC66F60080566CE0DAF8D17EAD46AD8E30A247C9F",
            "03", "429C954A3859CEF91295F663C963E582ED6EB253"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000003", "95",
            "BE9E1FA5E9A803852999C4AB432DB28600DCD9DAB76DFAAA47355A0FE37B1508AC6BF38860D3C6C2E5B12A3CAAF2A7005A7241EBAA7771112C74CF9A0634652FBCA0E5980C54A64761EA101A114E0F0B5572ADD57D010B7C9C887E104CA4EE1272DA66D997B9A90B5A6D624AB6C57E73C8F919000EB5F684898EF8C3DBEFB330C62660BED88EA78E909AFF05F6DA627B",
            "03", "EE1511CEC71020A9B90443B37B1D5F6E703030F6"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000003", "94",
            "ACD2B12302EE644F3F835ABD1FC7A6F62CCE48FFEC622AA8EF062BEF6FB8BA8BC68BBF6AB5870EED579BC3973E121303D34841A796D6DCBC41DBF9E52C4609795C0CCF7EE86FA1D5CB041071ED2C51D2202F63F1156C58A92D38BC60BDF424E1776E2BC9648078A03B36FB554375FC53D57C73F5160EA59F3AFC5398EC7B67758D65C9BFF7828B6B82D4BE124A416AB7301914311EA462C19F771F31B3B57336000DFF732D3B83DE07052D730354D297BEC72871DCCF0E193F171ABA27EE464C6A97690943D59BDABB2A27EB71CEEBDAFA1176046478FD62FEC452D5CA393296530AA3F41927ADFE434A2DF2AE3054F8840657A26E0FC617",
            "03", "C4A3C43CCF87327D136B804160E47D43B60E6E0F"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000003", "99",
            "AB79FCC9520896967E776E64444E5DCDD6E13611874F3985722520425295EEA4BD0C2781DE7F31CD3D041F565F747306EED62954B17EDABA3A6C5B85A1DE1BEB9A34141AF38FCF8279C9DEA0D5A6710D08DB4124F041945587E20359BAB47B7575AD94262D4B25F264AF33DEDCF28E09615E937DE32EDC03C54445FE7E382777",
            "03", "4ABFFD6B1C51212D05552E431C5B17007D2F5E6D"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000333", "02",
            "A3767ABD1B6AA69D7F3FBF28C092DE9ED1E658BA5F0909AF7A1CCD907373B7210FDEB16287BA8E78E1529F443976FD27F991EC67D95E5F4E96B127CAB2396A94D6E45CDA44CA4C4867570D6B07542F8D4BF9FF97975DB9891515E66F525D2B3CBEB6D662BFB6C3F338E93B02142BFC44173A3764C56AADD202075B26DC2F9F7D7AE74BD7D00FD05EE430032663D27A57",
            "03", "03BB335A8549A03B87AB089D006F60852E4B8060"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000333", "03",
            "B0627DEE87864F9C18C13B9A1F025448BF13C58380C91F4CEBA9F9BCB214FF8414E9B59D6ABA10F941C7331768F47B2127907D857FA39AAF8CE02045DD01619D689EE731C551159BE7EB2D51A372FF56B556E5CB2FDE36E23073A44CA215D6C26CA68847B388E39520E0026E62294B557D6470440CA0AEFC9438C923AEC9B2098D6D3A1AF5E8B1DE36F4B53040109D89B77CAFAF70C26C601ABDF59EEC0FDC8A99089140CD2E817E335175B03B7AA33D",
            "03", "87F0CD7C0E86F38F89A66F8C47071A8B88586F26"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000333", "04",
            "BC853E6B5365E89E7EE9317C94B02D0ABB0DBD91C05A224A2554AA29ED9FCB9D86EB9CCBB322A57811F86188AAC7351C72BD9EF196C5A01ACEF7A4EB0D2AD63D9E6AC2E7836547CB1595C68BCBAFD0F6728760F3A7CA7B97301B7E0220184EFC4F653008D93CE098C0D93B45201096D1ADFF4CF1F9FC02AF759DA27CD6DFD6D789B099F16F378B6100334E63F3D35F3251A5EC78693731F5233519CDB380F5AB8C0F02728E91D469ABD0EAE0D93B1CC66CE127B29C7D77441A49D09FCA5D6D9762FC74C31BB506C8BAE3C79AD6C2578775B95956B5370D1D0519E37906B384736233251E8F09AD79DFBE2C6ABFADAC8E4D8624318C27DAF1",
            "03", "F527081CF371DD7E1FD4FA414A665036E0F5E6E5"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000333", "08",
            "B61645EDFD5498FB246444037A0FA18C0F101EBD8EFA54573CE6E6A7FBF63ED21D66340852B0211CF5EEF6A1CD989F66AF21A8EB19DBD8DBC3706D135363A0D683D046304F5A836BC1BC632821AFE7A2F75DA3C50AC74C545A754562204137169663CFCC0B06E67E2109EBA41BC67FF20CC8AC80D7B6EE1A95465B3B2657533EA56D92D539E5064360EA4850FED2D1BF",
            "03", "EE23B616C95C02652AD18860E48787C079E8E85A"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000333", "09",
            "EB374DFC5A96B71D2863875EDA2EAFB96B1B439D3ECE0B1826A2672EEEFA7990286776F8BD989A15141A75C384DFC14FEF9243AAB32707659BE9E4797A247C2F0B6D99372F384AF62FE23BC54BCDC57A9ACD1D5585C303F201EF4E8B806AFB809DB1A3DB1CD112AC884F164A67B99C7D6E5A8A6DF1D3CAE6D7ED3D5BE725B2DE4ADE23FA679BF4EB15A93D8A6E29C7FFA1A70DE2E54F593D908A3BF9EBBD760BBFDC8DB8B54497E6C5BE0E4A4DAC29E5",
            "03", "A075306EAB0045BAF72CDD33B3B678779DE1F527"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000333", "0B",
            "CF9FDF46B356378E9AF311B0F981B21A1F22F250FB11F55C958709E3C7241918293483289EAE688A094C02C344E2999F315A72841F489E24B1BA0056CFAB3B479D0E826452375DCDBB67E97EC2AA66F4601D774FEAEF775ACCC621BFEB65FB0053FC5F392AA5E1D4C41A4DE9FFDFDF1327C4BB874F1F63A599EE3902FE95E729FD78D4234DC7E6CF1ABABAA3F6DB29B7F05D1D901D2E76A606A8CBFFFFECBD918FA2D278BDB43B0434F5D45134BE1C2781D157D501FF43E5F1C470967CD57CE53B64D82974C8275937C5D8502A1252A8A5D6088A259B694F98648D9AF2CB0EFD9D943C69F896D49FA39702162ACB5AF29B90BADE005BC157",
            "03", "BD331F9996A490B33C13441066A09AD3FEB5F66C"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000333", "80",
            "CCDBA686E2EFB84CE2EA01209EEB53BEF21AB6D353274FF8391D7035D76E2156CAEDD07510E07DAFCACABB7CCB0950BA2F0A3CEC313C52EE6CD09EF00401A3D6CC5F68CA5FCD0AC6132141FAFD1CFA36A2692D02DDC27EDA4CD5BEA6FF21913B513CE78BF33E6877AA5B605BC69A534F3777CBED6376BA649C72516A7E16AF85",
            "010001", "A5E44BB0E1FA4F96A11709186670D0835057D35E"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000004", "00",
            "9C6BE5ADB10B4BE3DCE2099B4B210672B89656EBA091204F613ECC623BEDC9C6D77B660E8BAEEA7F7CE30F1B153879A4E36459343D1FE47ACDBD41FCD710030C2BA1D9461597982C6E1BDD08554B726F5EFF7913CE59E79E357295C321E26D0B8BE270A9442345C753E2AA2ACFC9D30850602FE6CAC00C6DDF6B8D9D9B4879B2826B042A07F0E5AE526A3D3C4D22C72B9EAA52EED8893866F866387AC05A1399",
            "03", "EC0A59D35D19F031E9E8CBEC56DB80E22B1DE130"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000004", "02",
            "A99A6D3E071889ED9E3A0C391C69B0B804FC160B2B4BDD570C92DD5A0F45F53E8621F7C96C40224266735E1EE1B3C06238AE35046320FD8E81F8CEB3F8B4C97B940930A3AC5E790086DAD41A6A4F5117BA1CE2438A51AC053EB002AED866D2C458FD73359021A12029A0C043045C11664FE0219EC63C10BF2155BB2784609A106421D45163799738C1C30909BB6C6FE52BBB76397B9740CE064A613FF8411185F08842A423EAD20EDFFBFF1CD6C3FE0C9821479199C26D8572CC8AFFF087A9C3",
            "03", "33408B96C814742AD73536C72F0926E4471E8E47"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000004", "03",
            "C2490747FE17EB0584C88D47B1602704150ADC88C5B998BD59CE043EDEBF0FFEE3093AC7956AD3B6AD4554C6DE19A178D6DA295BE15D5220645E3C8131666FA4BE5B84FE131EA44B039307638B9E74A8C42564F892A64DF1CB15712B736E3374F1BBB6819371602D8970E97B900793C7C2A89A4A1649A59BE680574DD0B60145",
            "03", "5ADDF21D09278661141179CBEFF272EA384B13BB"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000004", "04",
            "A6DA428387A502D7DDFB7A74D3F412BE762627197B25435B7A81716A700157DDD06F7CC99D6CA28C2470527E2C03616B9C59217357C2674F583B3BA5C7DCF2838692D023E3562420B4615C439CA97C44DC9A249CFCE7B3BFB22F68228C3AF13329AA4A613CF8DD853502373D62E49AB256D2BC17120E54AEDCED6D96A4287ACC5C04677D4A5A320DB8BEE2F775E5FEC5",
            "03", "381A035DA58B482EE2AF75F4C3F2CA469BA4AA6C"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000004", "05",
            "B8048ABC30C90D976336543E3FD7091C8FE4800DF820ED55E7E94813ED00555B573FECA3D84AF6131A651D66CFF4284FB13B635EDD0EE40176D8BF04B7FD1C7BACF9AC7327DFAA8AA72D10DB3B8E70B2DDD811CB4196525EA386ACC33C0D9D4575916469C4E4F53E8E1C912CC618CB22DDE7C3568E90022E6BBA770202E4522A2DD623D180E215BD1D1507FE3DC90CA310D27B3EFCCD8F83DE3052CAD1E48938C68D095AAC91B5F37E28BB49EC7ED597",
            "03", "EBFA0D5D06D8CE702DA3EAE890701D45E274C845"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000004", "06",
            "CB26FC830B43785B2BCE37C81ED334622F9622F4C89AAE641046B2353433883F307FB7C974162DA72F7A4EC75D9D657336865B8D3023D3D645667625C9A07A6B7A137CF0C64198AE38FC238006FB2603F41F4F3BB9DA1347270F2F5D8C606E420958C5F7D50A71DE30142F70DE468889B5E3A08695B938A50FC980393A9CBCE44AD2D64F630BB33AD3F5F5FD495D31F37818C1D94071342E07F1BEC2194F6035BA5DED3936500EB82DFDA6E8AFB655B1EF3D0D7EBF86B66DD9F29F6B1D324FE8B26CE38AB2013DD13F611E7A594D675C4432350EA244CC34F3873CBA06592987A1D7E852ADC22EF5A2EE28132031E48F74037E3B34AB747F",
            "03", "F910A1504D5FFB793D94F3B500765E1ABCAD72D9"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000004", "EF",
            "A191CB87473F29349B5D60A88B3EAEE0973AA6F1A082F358D849FDDFF9C091F899EDA9792CAF09EF28F5D22404B88A2293EEBBC1949C43BEA4D60CFD879A1539544E09E0F09F60F065B2BF2A13ECC705F3D468B9D33AE77AD9D3F19CA40F23DCF5EB7C04DC8F69EBA565B1EBCB4686CD274785530FF6F6E9EE43AA43FDB02CE00DAEC15C7B8FD6A9B394BABA419D3F6DC85E16569BE8E76989688EFEA2DF22FF7D35C043338DEAA982A02B866DE5328519EBBCD6F03CDD686673847F84DB651AB86C28CF1462562C577B853564A290C8556D818531268D25CC98A4CC6A0BDFFFDA2DCCA3A94C998559E307FDDF915006D9A987B07DDAEB3B",
            "03", "21766EBB0EE122AFB65D7845B73DB46BAB65427A"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000004", "F1",
            "A0DCF4BDE19C3546B4B6F0414D174DDE294AABBB828C5A834D73AAE27C99B0B053A90278007239B6459FF0BBCD7B4B9C6C50AC02CE91368DA1BD21AAEADBC65347337D89B68F5C99A09D05BE02DD1F8C5BA20E2F13FB2A27C41D3F85CAD5CF6668E75851EC66EDBF98851FD4E42C44C1D59F5984703B27D5B9F21B8FA0D93279FBBF69E090642909C9EA27F898959541AA6757F5F624104F6E1D3A9532F2A6E51515AEAD1B43B3D7835088A2FAFA7BE7",
            "03", "D8E68DA167AB5A85D8C3D55ECB9B0517A1A5B4BB"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000004", "F3",
            "98F0C770F23864C2E766DF02D1E833DFF4FFE92D696E1642F0A88C5694C6479D16DB1537BFE29E4FDC6E6E8AFD1B0EB7EA0124723C333179BF19E93F10658B2F776E829E87DAEDA9C94A8B3382199A350C077977C97AFF08FD11310AC950A72C3CA5002EF513FCCC286E646E3C5387535D509514B3B326E1234F9CB48C36DDD44B416D23654034A66F403BA511C5EFA3",
            "03", "A69AC7603DAF566E972DEDC2CB433E07E8B01A9A"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000004", "F4",
            "9E2F74BF4AB521019735BFC7E4CBC56B6F64AFF1ED7B79998EE5B3DFFE23DFC8E2DD0025575AF94DE814264528AF6F8005A538B3D6AE881B350F89595588E51F7423E711109DEC169FDD560602D80EF46E582C8C546C8930394BD534412A88CC9FF4DFC08AE716A595EF1AF7C32EDFCF996433EB3C36BCE093E44E0BDE228E0299A0E358BF28308DB4739815DD09F1E89654CC7CC193E2AC17C4DA335D904B8EC06ACFBDE083F76933C969672E9AFEA3",
            "03", "BF6B5B9C47134E494571732A4903C935874682B9"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000004", "F5",
            "A6E6FB72179506F860CCCA8C27F99CECD94C7D4F3191D303BBEE37481C7AA15F233BA755E9E4376345A9A67E7994BDC1C680BB3522D8C93EB0CCC91AD31AD450DA30D337662D19AC03E2B4EF5F6EC18282D491E19767D7B24542DFDEFF6F62185503532069BBB369E3BB9FB19AC6F1C30B97D249EEE764E0BAC97F25C873D973953E5153A42064BBFABFD06A4BB486860BF6637406C9FC36813A4A75F75C31CCA9F69F8DE59ADECEF6BDE7E07800FCBE035D3176AF8473E23E9AA3DFEE221196D1148302677C720CFE2544A03DB553E7F1B8427BA1CC72B0F29B12DFEF4C081D076D353E71880AADFF386352AF0AB7B28ED49E1E672D11F9",
            "010001", "C2239804C8098170BE52D6D5D4159E81CE8466BF"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000004", "F6",
            "A25A6BD783A5EF6B8FB6F83055C260F5F99EA16678F3B9053E0F6498E82C3F5D1E8C38F13588017E2B12B3D8FF6F50167F46442910729E9E4D1B3739E5067C0AC7A1F4487E35F675BC16E233315165CB142BFDB25E301A632A54A3371EBAB6572DEEBAF370F337F057EE73B4AE46D1A8BC4DA853EC3CC12C8CBC2DA18322D68530C70B22BDAC351DD36068AE321E11ABF264F4D3569BB71214545005558DE26083C735DB776368172FE8C2F5C85E8B5B890CC682911D2DE71FA626B8817FCCC08922B703869F3BAEAC1459D77CD85376BC36182F4238314D6C4212FBDD7F23D3",
            "03", "502909ED545E3C8DBD00EA582D0617FEE9F6F684"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000004", "F7",
            "94EA62F6D58320E354C022ADDCF0559D8CF206CD92E869564905CE21D720F971B7AEA374830EBE1757115A85E088D41C6B77CF5EC821F30B1D890417BF2FA31E5908DED5FA677F8C7B184AD09028FDDE96B6A6109850AA800175EABCDBBB684A96C2EB6379DFEA08D32FE2331FE103233AD58DCDB1E6E077CB9F24EAEC5C25AF",
            "010001", "EEB0DD9B2477BEE3209A914CDBA94C1C4A9BDED9"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000004", "F8",
            "A1F5E1C9BD8650BD43AB6EE56B891EF7459C0A24FA84F9127D1A6C79D4930F6DB1852E2510F18B61CD354DB83A356BD190B88AB8DF04284D02A4204A7B6CB7C5551977A9B36379CA3DE1A08E69F301C95CC1C20506959275F41723DD5D2925290579E5A95B0DF6323FC8E9273D6F849198C4996209166D9BFC973C361CC826E1",
            "03", "F06ECC6D2AAEBF259B7E755A38D9A9B24E2FF3DD"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000004", "F9",
            "A99A6D3E071889ED9E3A0C391C69B0B804FC160B2B4BDD570C92DD5A0F45F53E8621F7C96C40224266735E1EE1B3C06238AE35046320FD8E81F8CEB3F8B4C97B940930A3AC5E790086DAD41A6A4F5117BA1CE2438A51AC053EB002AED866D2C458FD73359021A12029A0C043045C11664FE0219EC63C10BF2155BB2784609A106421D45163799738C1C30909BB6C6FE52BBB76397B9740CE064A613FF8411185F08842A423EAD20EDFFBFF1CD6C3FE0C9821479199C26D8572CC8AFFF087A9C3",
            "03", "336712DCC28554809C6AA9B02358DE6F755164DB"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000004", "FA",
            "A90FCD55AA2D5D9963E35ED0F440177699832F49C6BAB15CDAE5794BE93F934D4462D5D12762E48C38BA83D8445DEAA74195A301A102B2F114EADA0D180EE5E7A5C73E0C4E11F67A43DDAB5D55683B1474CC0627F44B8D3088A492FFAADAD4F42422D0E7013536C3C49AD3D0FAE96459B0F6B1B6056538A3D6D44640F94467B108867DEC40FAAECD740C00E2B7A8852D",
            "03", "5BED4068D96EA16D2D77E03D6036FC7A160EA99C"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000004", "FE",
            "A653EAC1C0F786C8724F737F172997D63D1C3251C44402049B865BAE877D0F398CBFBE8A6035E24AFA086BEFDE9351E54B95708EE672F0968BCD50DCE40F783322B2ABA04EF137EF18ABF03C7DBC5813AEAEF3AA7797BA15DF7D5BA1CBAF7FD520B5A482D8D3FEE105077871113E23A49AF3926554A70FE10ED728CF793B62A1",
            "03", "9A295B05FB390EF7923F57618A9FDA2941FC34E0"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000152", "5A",
            "EDD8252468A705614B4D07DE3211B30031AEDB6D33A4315F2CFF7C97DB918993C2DC02E79E2FF8A2683D5BBD0F614BC9AB360A448283EF8B9CF6731D71D6BE939B7C5D0B0452D660CF24C21C47CAC8E26948C8EED8E3D00C016828D642816E658DC2CFC61E7E7D7740633BEFE34107C1FB55DEA7FAAEA2B25E85BED948893D07",
            "03", "CC9585E8E637191C10FCECB32B5AE1B9D410B52D"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000152", "5B",
            "D3F45D065D4D900F68B2129AFA38F549AB9AE4619E5545814E468F382049A0B9776620DA60D62537F0705A2C926DBEAD4CA7CB43F0F0DD809584E9F7EFBDA3778747BC9E25C5606526FAB5E491646D4DD28278691C25956C8FED5E452F2442E25EDC6B0C1AA4B2E9EC4AD9B25A1B836295B823EDDC5EB6E1E0A3F41B28DB8C3B7E3E9B5979CD7E079EF024095A1D19DD",
            "03", "4DC5C6CAB6AE96974D9DC8B2435E21F526BC7A60"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000152", "5C",
            "833F275FCF5CA4CB6F1BF880E54DCFEB721A316692CAFEB28B698CAECAFA2B2D2AD8517B1EFB59DDEFC39F9C3B33DDEE40E7A63C03E90A4DD261BC0F28B42EA6E7A1F307178E2D63FA1649155C3A5F926B4C7D7C258BCA98EF90C7F4117C205E8E32C45D10E3D494059D2F2933891B979CE4A831B301B0550CDAE9B67064B31D8B481B85A5B046BE8FFA7BDB58DC0D7032525297F26FF619AF7F15BCEC0C92BCDCBC4FB207D115AA65CD04C1CF982191",
            "03", "60154098CBBA350F5F486CA31083D1FC474E31F8"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000152", "5D",
            "AD938EA9888E5155F8CD272749172B3A8C504C17460EFA0BED7CBC5FD32C4A80FD810312281B5A35562800CDC325358A9639C501A537B7AE43DF263E6D232B811ACDB6DDE979D55D6C911173483993A423A0A5B1E1A70237885A241B8EEBB5571E2D32B41F9CC5514DF83F0D69270E109AF1422F985A52CCE04F3DF269B795155A68AD2D6B660DDCD759F0A5DA7B64104D22C2771ECE7A5FFD40C774E441379D1132FAF04CDF55B9504C6DCE9F61776D81C7C45F19B9EFB3749AC7D486A5AD2E781FA9D082FB2677665B99FA5F1553135A1FD2A2A9FBF625CA84A7D736521431178F13100A2516F9A43CE095B032B886C7A6AB126E203BE7",
            "03", "B51EC5F7DE9BB6D8BCE8FB5F69BA57A04221F39B"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000025", "03",
            "B0C2C6E2A6386933CD17C239496BF48C57E389164F2A96BFF133439AE8A77B20498BD4DC6959AB0C2D05D0723AF3668901937B674E5A2FA92DDD5E78EA9D75D79620173CC269B35F463B3D4AAFF2794F92E6C7A3FB95325D8AB95960C3066BE548087BCB6CE12688144A8B4A66228AE4659C634C99E36011584C095082A3A3E3",
            "03", "8708A3E3BBC1BB0BE73EBD8D19D4E5D20166BF6C"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000025", "04",
            "D0F543F03F2517133EF2BA4A1104486758630DCFE3A883C77B4E4844E39A9BD6360D23E6644E1E071F196DDF2E4A68B4A3D93D14268D7240F6A14F0D714C17827D279D192E88931AF7300727AE9DA80A3F0E366AEBA61778171737989E1EE309",
            "03", "FDD7139EC7E0C33167FD61AD3CADBD68D66E91C5"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000025", "0E",
            "AA94A8C6DAD24F9BA56A27C09B01020819568B81A026BE9FD0A3416CA9A71166ED5084ED91CED47DD457DB7E6CBCD53E560BC5DF48ABC380993B6D549F5196CFA77DFB20A0296188E969A2772E8C4141665F8BB2516BA2C7B5FC91F8DA04E8D512EB0F6411516FB86FC021CE7E969DA94D33937909A53A57F907C40C22009DA7532CB3BE509AE173B39AD6A01BA5BB85",
            "03", "A7266ABAE64B42A3668851191D49856E17F8FBCD"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000025", "0F",
            "C8D5AC27A5E1FB89978C7C6479AF993AB3800EB243996FBB2AE26B67B23AC482C4B746005A51AFA7D2D83E894F591A2357B30F85B85627FF15DA12290F70F05766552BA11AD34B7109FA49DE29DCB0109670875A17EA95549E92347B948AA1F045756DE56B707E3863E59A6CBE99C1272EF65FB66CBB4CFF070F36029DD76218B21242645B51CA752AF37E70BE1A84FF31079DC0048E928883EC4FADD497A719385C2BBBEBC5A66AA5E5655D18034EC5",
            "03", "A73472B3AB557493A9BC2179CC8014053B12BAB4"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000025", "10",
            "CF98DFEDB3D3727965EE7797723355E0751C81D2D3DF4D18EBAB9FB9D49F38C8C4A826B99DC9DEA3F01043D4BF22AC3550E2962A59639B1332156422F788B9C16D40135EFD1BA94147750575E636B6EBC618734C91C1D1BF3EDC2A46A43901668E0FFC136774080E888044F6A1E65DC9AAA8928DACBEB0DB55EA3514686C6A732CEF55EE27CF877F110652694A0E3484C855D882AE191674E25C296205BBB599455176FDD7BBC549F27BA5FE35336F7E29E68D783973199436633C67EE5A680F05160ED12D1665EC83D1997F10FD05BBDBF9433E8F797AEE3E9F02A34228ACE927ABE62B8B9281AD08D3DF5C7379685045D7BA5FCDE58637",
            "03", "C729CF2FD262394ABC4CC173506502446AA9B9FD"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000025", "65",
            "E53EB41F839DDFB474F272CD0CBE373D5468EB3F50F39C95BDF4D39FA82B98DABC9476B6EA350C0DCE1CD92075D8C44D1E57283190F96B3537D9E632C461815EBD2BAF36891DF6BFB1D30FA0B752C43DCA0257D35DFF4CCFC98F84198D5152EC61D7B5F74BD09383BD0E2AA42298FFB02F0D79ADB70D72243EE537F75536A8A8DF962582E9E6812F3A0BE02A4365400D",
            "03", "894C5D08D4EA28BB79DC46CEAD998B877322F416"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000025", "97",
            "E178FFE834B4B767AF3C9A511F973D8E8505C5FCB2D3768075AB7CC946A955789955879AAF737407151521996DFA43C58E6B130EB1D863B85DC9FFB4050947A2676AA6A061A4A7AE1EDB0E36A697E87E037517EB8923136875BA2CA1087CBA7EC7653E5E28A0C261A033AF27E3A67B64BBA26956307EC47E674E3F8B722B3AE0498DB16C7985310D9F3D117300D32B09",
            "03", "EBDA522B631B3EB4F4CBFC0679C450139D2B69CD"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000025", "98",
            "D31A7094FB221CBA6660FB975AAFEA80DB7BB7EAFD7351E748827AB62D4AEECCFC1787FD47A04699A02DB00D7C382E80E804B35C59434C602389D691B9CCD51ED06BE67A276119C4C10E2E40FC4EDDF9DF39B9B0BDEE8D076E2A012E8A292AF8EFE18553470639C1A032252E0E5748B25A3F9BA4CFCEE073038B061837F2AC1B04C279640F5BD110A9DC665ED2FA6828BD5D0FE810A892DEE6B0E74CE8863BDE08FD5FD61A0F11FA0D14978D8CED7DD3",
            "03", "D4DBA428CF11D45BAEB0A35CAEA8007AD8BA8D71"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000025", "99",
            "E1740074229FA0D228A9623581D7A322903FB89BA7686712E601FA8AB24A9789186F15B70CCBBE7421B1CB110D45361688135FFD0DB15A3F516BB291D4A123EBF5A06FBF7E1EE6311B737DABB289570A7959D532B25F1DA6758C84DDCCADC049BC764C05391ABD2CADEFFA7E242D5DD06E56001F0E68151E3388074BD9330D6AFA57CBF33946F531E51E0D4902EE235C756A905FB733940E6EC897B4944A5EDC765705E2ACF76C78EAD78DD9B066DF0B2C88750B8AEE00C9B4D4091FA7338449DA92DBFC908FA0781C0128C492DB993C88BA8BB7CADFE238D477F2517E0E7E3D2B11796A0318CE2AD4DA1DB8E54AB0D94F109DB9CAEEFBEF",
            "03", "94790D020F4F692D59289F36451872078005B63B"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000025", "C1",
            "E69E319C34D1B4FB43AED4BD8BBA6F7A8B763F2F6EE5DDF7C92579A984F89C4A9C15B27037764C58AC7E45EFBC34E138E56BA38F76E803129A8DDEB5E1CC8C6B30CF634A9C9C1224BF1F0A9A18D79ED41EBCF1BE78087AE8B7D2F896B1DE8B7E784161A138A0F2169AD33E146D1B16AB595F9D7D98BE671062D217F44EB68C68640C7D57465A063F6BAC776D3E2DAC61",
            "03", "DC79D6B5FC879362299BC5A637DAD2E0D99656B8"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000025", "C2",
            "B875002F38BA26D61167C5D440367604AD38DF2E93D8EE8DA0E8D9C0CF4CC5788D11DEA689E5F41D23A3DA3E0B1FA5875AE25620F5A6BCCEE098C1B35C691889D7D0EF670EB8312E7123FCC5DC7D2F0719CC80E1A93017F944D097330EDF945762FEE62B7B0BA0348228DBF38D4216E5A67A7EF74F5D3111C44AA31320F623CB3C53E60966D6920067C9E082B746117E48E4F00E110950CA54DA3E38E5453BD5544E3A6760E3A6A42766AD2284E0C9AF",
            "03", "8E748296359A7428F536ADDA8E2C037E2B697EF6"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000025", "C3",
            "B93182ABE343DFBF388C71C4D6747DCDEC60367FE63CFAA942D7D323E688D0832836548BF0EDFF1EDEEB882C75099FF81A93FA525C32425B36023EA02A8899B9BF7D7934E86F997891823006CEAA93091A73C1FDE18ABD4F87A22308640C064C8C027685F1B2DB7B741B67AB0DE05E870481C5F972508C17F57E4F833D63220F6EA2CFBB878728AA5887DE407D10C6B8F58D46779ECEC1E2155487D52C78A5C03897F2BB580E0A2BBDE8EA2E1C18F6AAF3EB3D04C3477DEAB88F150C8810FD1EF8EB0596866336FE2C1FBC6BEC22B4FE5D885647726DB59709A505F75C49E0D8D71BF51E4181212BE2142AB2A1E8C0D3B7136CD7B7708E4D",
            "03", "12F1790CB0273DC73C6E70784BC24C12E8DB71F6"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000025", "C7",
            "CD237E34E0299DE48F1A2C94F478FE972896011E1CA6AB462B68FE0F6109C9A97C2DBEEA65932CDE0625138B9F162B92979DAAB019D3B5561D31EB2D4F09F12F927EA8F740CE0E87154965505E2272F69042B15D57CCC7F771919123978283B3CCE524D9715207BF5F5AD369102176F0F7A78A6DEB2BFF0EDCE165F3B14F14D0035B2756861FE03C43396ED002C894A3",
            "03", "6221E0C726BAC8F8AC25F8F93B811D1FFD4C131C"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000025", "C8",
            "BF0CFCED708FB6B048E3014336EA24AA007D7967B8AA4E613D26D015C4FE7805D9DB131CED0D2A8ED504C3B5CCD48C33199E5A5BF644DA043B54DBF60276F05B1750FAB39098C7511D04BABC649482DDCF7CC42C8C435BAB8DD0EB1A620C31111D1AAAF9AF6571EEBD4CF5A08496D57E7ABDBB5180E0A42DA869AB95FB620EFF2641C3702AF3BE0B0C138EAEF202E21D",
            "03", "33BD7A059FAB094939B90A8F35845C9DC779BD50"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000025", "C9",
            "B362DB5733C15B8797B8ECEE55CB1A371F760E0BEDD3715BB270424FD4EA26062C38C3F4AAA3732A83D36EA8E9602F6683EECC6BAFF63DD2D49014BDE4D6D603CD744206B05B4BAD0C64C63AB3976B5C8CAAF8539549F5921C0B700D5B0F83C4E7E946068BAAAB5463544DB18C63801118F2182EFCC8A1E85E53C2A7AE839A5C6A3CABE73762B70D170AB64AFC6CA482944902611FB0061E09A67ACB77E493D998A0CCF93D81A4F6C0DC6B7DF22E62DB",
            "03", "8E8DFF443D78CD91DE88821D70C98F0638E51E49"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000025", "CA",
            "C23ECBD7119F479C2EE546C123A585D697A7D10B55C2D28BEF0D299C01DC65420A03FE5227ECDECB8025FBC86EEBC1935298C1753AB849936749719591758C315FA150400789BB14FADD6EAE2AD617DA38163199D1BAD5D3F8F6A7A20AEF420ADFE2404D30B219359C6A4952565CCCA6F11EC5BE564B49B0EA5BF5B3DC8C5C6401208D0029C3957A8C5922CBDE39D3A564C6DEBB6BD2AEF91FC27BB3D3892BEB9646DCE2E1EF8581EFFA712158AAEC541C0BBB4B3E279D7DA54E45A0ACC3570E712C9F7CDF985CFAFD382AE13A3B214A9E8E1E71AB1EA707895112ABC3A97D0FCB0AE2EE5C85492B6CFD54885CDD6337E895CC70FB3255E3",
            "03", "6BDA32B1AA171444C7E8F88075A74FBFE845765F"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000065", "08",
            "B74670DAD1DC8983652000E5A7F2F8B35DFD083EE593E5BA895C95729F2BADE9C8ABF3DD9CE240C451C6CEFFC768D83CBAC76ABB8FEA58F013C647007CFF7617BAC2AE3981816F25CC7E5238EF34C4F02D0B01C24F80C2C65E7E7743A4FA8E23206A23ECE290C26EA56DB085C5C5EAE26292451FC8292F9957BE8FF20FAD53E5",
            "03", "DD36D5896228C8C4900742F107E2F91FE50BC7EE"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000065", "09",
            "B72A8FEF5B27F2B550398FDCC256F714BAD497FF56094B7408328CB626AA6F0E6A9DF8388EB9887BC930170BCC1213E90FC070D52C8DCD0FF9E10FAD36801FE93FC998A721705091F18BC7C98241CADC15A2B9DA7FB963142C0AB640D5D0135E77EBAE95AF1B4FEFADCF9C012366BDDA0455C1564A68810D7127676D493890BD",
            "03", "4410C6D51C2F83ADFD92528FA6E38A32DF048D0A"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000065", "0F",
            "9EFBADDE4071D4EF98C969EB32AF854864602E515D6501FDE576B310964A4F7C2CE842ABEFAFC5DC9E26A619BCF2614FE07375B9249BEFA09CFEE70232E75FFD647571280C76FFCA87511AD255B98A6B577591AF01D003BD6BF7E1FCE4DFD20D0D0297ED5ECA25DE261F37EFE9E175FB5F12D2503D8CFB060A63138511FE0E125CF3A643AFD7D66DCF9682BD246DDEA1",
            "03", "2A1B82DE00F5F0C401760ADF528228D3EDE0F403"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000065", "10",
            "99B63464EE0B4957E4FD23BF923D12B61469B8FFF8814346B2ED6A780F8988EA9CF0433BC1E655F05EFA66D0C98098F25B659D7A25B8478A36E489760D071F54CDF7416948ED733D816349DA2AADDA227EE45936203CBF628CD033AABA5E5A6E4AE37FBACB4611B4113ED427529C636F6C3304F8ABDD6D9AD660516AE87F7F2DDF1D2FA44C164727E56BBC9BA23C0285",
            "03", "C75E5210CBE6E8F0594A0F1911B07418CADB5BAB"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000065", "11",
            "A2583AA40746E3A63C22478F576D1EFC5FB046135A6FC739E82B55035F71B09BEB566EDB9968DD649B94B6DEDC033899884E908C27BE1CD291E5436F762553297763DAA3B890D778C0F01E3344CECDFB3BA70D7E055B8C760D0179A403D6B55F2B3B083912B183ADB7927441BED3395A199EEFE0DEBD1F5FC3264033DA856F4A8B93916885BD42F9C1F456AAB8CFA83AC574833EB5E87BB9D4C006A4B5346BD9E17E139AB6552D9C58BC041195336485",
            "03", "D9FD62C9DD4E6DE7741E9A17FB1FF2C5DB948BCB"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000065", "12",
            "ADF05CD4C5B490B087C3467B0F3043750438848461288BFEFD6198DD576DC3AD7A7CFA07DBA128C247A8EAB30DC3A30B02FCD7F1C8167965463626FEFF8AB1AA61A4B9AEF09EE12B009842A1ABA01ADB4A2B170668781EC92B60F605FD12B2B2A6F1FE734BE510F60DC5D189E401451B62B4E06851EC20EBFF4522AACC2E9CDC89BC5D8CDE5D633CFD77220FF6BBD4A9B441473CC3C6FEFC8D13E57C3DE97E1269FA19F655215B23563ED1D1860D8681",
            "03", "874B379B7F607DC1CAF87A19E400B6A9E25163E8"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000065", "13",
            "A3270868367E6E29349FC2743EE545AC53BD3029782488997650108524FD051E3B6EACA6A9A6C1441D28889A5F46413C8F62F3645AAEB30A1521EEF41FD4F3445BFA1AB29F9AC1A74D9A16B93293296CB09162B149BAC22F88AD8F322D684D6B49A12413FC1B6AC70EDEDB18EC1585519A89B50B3D03E14063C2CA58B7C2BA7FB22799A33BCDE6AFCBEB4A7D64911D08D18C47F9BD14A9FAD8805A15DE5A38945A97919B7AB88EFA11A88C0CD92C6EE7DC352AB0746ABF13585913C8A4E04464B77909C6BD94341A8976C4769EA6C0D30A60F4EE8FA19E767B170DF4FA80312DBA61DB645D5D1560873E2674E1F620083F30180BD96CA589",
            "03", "54CFAE617150DFA09D3F901C9123524523EBEDF3"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000065", "14",
            "AEED55B9EE00E1ECEB045F61D2DA9A66AB637B43FB5CDBDB22A2FBB25BE061E937E38244EE5132F530144A3F268907D8FD648863F5A96FED7E42089E93457ADC0E1BC89C58A0DB72675FBC47FEE9FF33C16ADE6D341936B06B6A6F5EF6F66A4EDD981DF75DA8399C3053F430ECA342437C23AF423A211AC9F58EAF09B0F837DE9D86C7109DB1646561AA5AF0289AF5514AC64BC2D9D36A179BB8A7971E2BFA03A9E4B847FD3D63524D43A0E8003547B94A8A75E519DF3177D0A60BC0B4BAB1EA59A2CBB4D2D62354E926E9C7D3BE4181E81BA60F8285A896D17DA8C3242481B6C405769A39D547C74ED9FF95A70A796046B5EFF36682DC29",
            "03", "C0D15F6CD957E491DB56DCDD1CA87A03EBE06B7B"
        )
        emvCoreManager.EmvSetCapk(capk)


        // verve capk
        capk = addCapk(
            "A000000371", "03",
            "d06238b856cf2c8890a7f668ca17c19247498d193a7c11e7105dedeee6a873e8189e50493e9b17547c42ea4fa88bbef30bb6bc2409246ccc95f36622a7f4d92d46444f20b1b24bf63c5b28395d8ef18c23205c2119dfe5fba2fbfc311b2fe8a6a75b35a7dab72d421792a500cdfd8133b8a97d84a49c0bd22d52d06ea5e0ef3e471d47d8370c37aa48b564689d0035d9",
            "03", "319F3C608B67F1118C729B0E1516EAB07CB290C8"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000371", "06",
            "D2DA0134B4DFC93A75EE8960C99896D50A91527B87BA7B16CDB77E5B6FDB750EB70B54026CADDA1D562C77A2C6DA541E94BC415D43E68489B16980F2E887C09E4CF90E2E639B179277BBA0E982CCD1F80521D1457209125B3ABCD309E1B92B5AEDA2EB1CBF933BEAD9CE7365E52B7D17FCB405AA28E5DE6AA3F08E764F745E70859ABCBA41E570A6E4367B3D6FECE723B73ABF3EB53DCDE3816E8A813460447021509D0DFDF2EEEE74CC35485FB55C26836EB3BF9C7DEBEE6C0B77B7BE059233801CF76B321FCA25FB1E63117AE1865E23161EC39D7B1FB84256C2BE72BF8EC771548DB9F00BEF77C509FADA15E2B53FF950D383F96211D3",
            "03", "F5BAB84ECE5F8BD45511E5CA861B80C7E6C51F55"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000371", "04",
            "D13CD5E1B921E4E0F0D40E2DE14CCE73E3A34ED2DCFA826531D8195641091E37C8474D19B686E8243F089A69F7B18D2D34CB4824F228F7750F96D1EFBDFF881F259A8C04DE64915A3A3D7CB846135F4083C93CDE755BC808886F600542DFF085558D5EA7F45CB15EC835064AA856D602A0A44CD021F54CF8EC0CC680B54B3665ABE74A7C43D02897FF84BB4CB98BC91D",
            "03", "8B36A3E3D814CE6C6EBEAAF27674BB7BC67275B1"
        )
        emvCoreManager.EmvSetCapk(capk)
        capk = addCapk(
            "A000000371", "05",
            "B036A8CAE0593A480976BFE84F8A67759E52B3D9F4A68CCC37FE720E594E5694CD1AE20E1B120D7A18FA5C70E044D3B12E932C9BBD9FDEA4BE11071EF8CA3AF48FF2B5DDB307FC752C5C73F5F274D4238A92B4FCE66FC93DA18E6C1CC1AA3CFAFCB071B67DAACE96D9314DB494982F5C967F698A05E1A8A69DA931B8E566270F04EAB575F5967104118E4F12ABFF9DEC92379CD955A10675282FE1B60CAD13F9BB80C272A40B6A344EA699FB9EFA6867",
            "03", "676822D335AB0D2C3848418CB546DF7B6A6C32C0"
        )
        emvCoreManager.EmvSetCapk(capk)

        // Afrigo capk
        capk = addCapk(
            "A000000891", "90",
            "E2C471DA374BF87116AEFDEF9A8101A454E4BFB4352380609AC2B0C163AA7A5F8366A6AFB5D138A4B5AFC2D4F10CF68F8881B299890CEAA1AF4FA3C08597903FF35E789755A10DE1CA78680219CF5A7510BB4554D3CB7F0D8694401D865CA1074AF65D3A5F31FF84E82A956005CE3A2B477FB00BCF8AD041632DC9528EF11AAE7B441D27A08F6BAE65C314C02EE8CAF3CA245DCFFBEAB6E3FDECC8855DAFADD03BB7613EEEC14CCD6EB616545E29454DA1C4E97100112DB0C5B35EEE57786F9E9CB18634E17A13CBA3D70EF41D76A1ED57BF0DCE150C530D117026289A87576737233C1E10840647CA059EC1C632A0F699F109BB4DA2BCB7",
            "03", "D9ECCD2EA52CC41C0D16F923BD15B76042C66FA7"
        )
        emvCoreManager.EmvSetCapk(capk)
    }

    private fun addCapk(
        rid: String,
        capkIndex: String,
        modules: String,
        exponents: String,
        checksum: String
    ): PosEmvCapk? {
        val capk = PosEmvCapk()
        capk.RID = PosUtils.hexStringToBytes(rid)
        capk.CapkIndex = PosUtils.hexStringToBytes(capkIndex)[0]
        capk.Module = PosUtils.hexStringToBytes(modules)
        capk.Exponent = PosUtils.hexStringToBytes(exponents)
        capk.Checksum = PosUtils.hexStringToBytes(checksum)
        capk.AlgorithmInd = PosEmvCapk.ALGO_IND_RSA.toByte()
        capk.HashInd = PosEmvCapk.HASH_IND_SHA1.toByte()
        return capk
    }

    override suspend fun loadExceptionFile() {
        val emvCoreManager = getDefault()
        emvCoreManager.EmvDeleteExceptionFile()
        var exceptionFile = PosEmvExceptionFile()
        exceptionFile.PAN = PosUtils.hexStringToBytes("5413339123401596")
        exceptionFile.SerialNo = PosUtils.hexStringToBytes("00")
        emvCoreManager.EmvSetExceptionFile(exceptionFile)
        exceptionFile = PosEmvExceptionFile()
        exceptionFile.PAN = PosUtils.hexStringToBytes("5413339123401196")
        exceptionFile.SerialNo = PosUtils.hexStringToBytes("01")
        emvCoreManager.EmvSetExceptionFile(exceptionFile)
    }

    override suspend fun loadRevocationIPK() {
        val emvCoreManager = getDefault()
        emvCoreManager.EmvDeleteRevocationIPK()
        var revocationIPK = PosEmvRevocationIPK()
        revocationIPK.RID = PosUtils.hexStringToBytes("A000000124")
        revocationIPK.SerialNo = PosUtils.hexStringToBytes("001000")
        revocationIPK.CapkIndex = 0xF8.toByte()
        emvCoreManager.EmvSetRevocationIPK(revocationIPK)
        revocationIPK = PosEmvRevocationIPK()
        revocationIPK.RID = PosUtils.hexStringToBytes("A000000224")
        revocationIPK.SerialNo = PosUtils.hexStringToBytes("001000")
        revocationIPK.CapkIndex = 0xF8.toByte()
        emvCoreManager.EmvSetRevocationIPK(revocationIPK)
    }

    override suspend fun loadVisa() {
        val bundle = Bundle()
        val tlvBuilder = BerTlvBuilder()
        val qvsdcParameter = QvsdcParameter()
        val entryPoint = EntryPoint()
        entryPoint.CTLCheck = true
        entryPoint.CVMCheck = true
        entryPoint.CFLCheck = true
        entryPoint.StatusCheck = true
        entryPoint.ZeroAmountCheck = true
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_VISA_SET_ENTRY_POINT),
                entryPointToData(entryPoint)
            )
        )
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_VISA_SET_STATUS_ZERO_AMOUNT),
                HexUtil.parseHex("01")
            )
        )
        val kernelConfig = ByteArray(1)
        if (qvsdcParameter.SupportDRL) {
            kernelConfig[0] = kernelConfig[0] or 0x01
        }
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_VISA_SET_KERNEL_CONFIG),
                kernelConfig
            )
        )
        val qualifiers = ByteArray(4)
        if (qvsdcParameter.SupportMagstripe) {
            qualifiers[0] = qualifiers[0] or 0x80.toByte()
        }
        if (qvsdcParameter.SupportQVSDC) {
            qualifiers[0] = qualifiers[0] or 0x20
        }
        if (qvsdcParameter.SupportContact) {
            qualifiers[0] = qualifiers[0] or 0x10
        }
        if (qvsdcParameter.OfflineOnly) {
            qualifiers[0] = qualifiers[0] or 0x08
        }
        if (qvsdcParameter.SupportOnlinePIN) {
            qualifiers[0] = qualifiers[0] or 0x04
        }
        if (qvsdcParameter.SupportSignature) {
            qualifiers[0] = qualifiers[0] or 0x02
        }
        if (qvsdcParameter.SupportOnlineODA) {
            qualifiers[0] = qualifiers[0] or 0x01
        }
        if (qvsdcParameter.SupportIssuerScriptUpdate) {
            qualifiers[2] = qualifiers[2] or 0x80.toByte()
        }
        if (qvsdcParameter.SupportCDCVM) {
            qualifiers[2] = qualifiers[2] or 0x40
        }
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_VISA_SET_QUALIFIERS),
                qualifiers
            )
        )
        bundle.putByteArray(EmvTerminalConstraints.CONFIG, tlvBuilder.buildArray())
        getDefault().EmvSetTerminal(EmvTerminalConstraints.TYPE_VISA, bundle)
    }

    override suspend fun loadUnionPay() {
        val bundle = Bundle()
        val tlvBuilder = BerTlvBuilder()
        val unionPayParameter = UnionPayParameter()
        val entryPoint = EntryPoint()
        entryPoint.CTLCheck = true
        entryPoint.CVMCheck = true
        entryPoint.CFLCheck = true
        entryPoint.StatusCheck = true
        entryPoint.ZeroAmountCheck = true
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_UNIONPAY_SET_ENTRY_POINT),
                entryPointToData(entryPoint)
            )
        )
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_UNIONPAY_SET_STATUS_ZERO_AMOUNT),
                HexUtil.parseHex("01")
            )
        )
        val qualifiers = ByteArray(4)
        if (unionPayParameter.SupportEMV) {
            qualifiers[0] = qualifiers[0] or 0x20
        }
        if (unionPayParameter.SupportContact) {
            qualifiers[0] = qualifiers[0] or 0x10
        }
        if (unionPayParameter.OfflineOnly) {
            qualifiers[0] = qualifiers[0] or 0x08
        }
        if (unionPayParameter.SupportOnlinePIN) {
            qualifiers[0] = qualifiers[0] or 0x04
        }
        if (unionPayParameter.SupportSignature) {
            qualifiers[0] = qualifiers[0] or 0x02
        }
        if (unionPayParameter.SupportCDCVM) {
            qualifiers[2] = qualifiers[2] or 0x40
        }
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_UNIONPAY_SET_QUALIFIERS),
                qualifiers
            )
        )
        bundle.putByteArray(EmvTerminalConstraints.CONFIG, tlvBuilder.buildArray())
        getDefault().EmvSetTerminal(EmvTerminalConstraints.TYPE_UNIONPAY, bundle)
    }

    override suspend fun loadMasterCard() {
        val bundle = Bundle()
        val tlvBuilder = BerTlvBuilder()
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_CVM_CAPABILITIES),
                HexUtil.parseHex("60")
            )
        )
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_NO_CVM_CAPABILITIES),
                HexUtil.parseHex("08")
            )
        )
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_MAGSTRIPE_CVM_CAPABILITIES),
                HexUtil.parseHex("10")
            )
        )
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_MAGSTRIPE_NO_CVM_CAPABILITIES),
                HexUtil.parseHex("00")
            )
        )
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_DEFAULT_UDOL),
                HexUtil.parseHex("9F6A04")
            )
        )
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_MASTERCARD_SET_KERNEL_CONFIG),
                HexUtil.parseHex("30")
            )
        )
        bundle.putByteArray(EmvTerminalConstraints.CONFIG, tlvBuilder.buildArray())
        getDefault().EmvSetTerminal(EmvTerminalConstraints.TYPE_MASTERCARD, bundle)
    }

    override suspend fun loadDiscover() {
        val bundle = Bundle()
        val tlvBuilder = BerTlvBuilder()
        val discoverParameter = DiscoverParameter()
        val entryPoint = EntryPoint()
        entryPoint.CTLCheck = true
        entryPoint.CVMCheck = true
        entryPoint.CFLCheck = true
        entryPoint.StatusCheck = true
        entryPoint.ZeroAmountCheck = true
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_DISCOVER_SET_ENTRY_POINT),
                entryPointToData(entryPoint)
            )
        )
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_DISCOVER_SET_STATUS_ZERO_AMOUNT),
                HexUtil.parseHex("01")
            )
        )
        val qualifiers = ByteArray(4)
        if (discoverParameter.SupportMagstripe) {
            qualifiers[0] = qualifiers[0] or 0x80.toByte()
        }
        if (discoverParameter.SupportEMV) {
            qualifiers[0] = qualifiers[0] or 0x20
        }
        if (discoverParameter.SupportContact) {
            qualifiers[0] = qualifiers[0] or 0x10
        }
        if (discoverParameter.OfflineOnly) {
            qualifiers[0] = qualifiers[0] or 0x08
        }
        if (discoverParameter.SupportOnlinePIN) {
            qualifiers[0] = qualifiers[0] or 0x04
        }
        if (discoverParameter.SupportSignature) {
            qualifiers[0] = qualifiers[0] or 0x02
        }
        if (discoverParameter.SupportIssuerScriptUpdate) {
            qualifiers[2] = qualifiers[2] or 0x80.toByte()
        }
        if (discoverParameter.SupportCDCVM) {
            qualifiers[2] = qualifiers[2] or 0x40
        }
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_DISCOVER_SET_QUALIFIERS),
                qualifiers
            )
        )
        bundle.putByteArray(EmvTerminalConstraints.CONFIG, tlvBuilder.buildArray())
        getDefault().EmvSetTerminal(EmvTerminalConstraints.TYPE_DISCOVER, bundle)
    }

    override suspend fun loadAmex() {
        val bundle = Bundle()
        val tlvBuilder = BerTlvBuilder()
        val amexParameter = AmexParameter()
        val entryPoint = EntryPoint()
        entryPoint.CTLCheck = true
        entryPoint.CVMCheck = true
        entryPoint.CFLCheck = true
        entryPoint.StatusCheck = true
        entryPoint.ZeroAmountCheck = true
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_AMEX_SET_ENTRY_POINT),
                entryPointToData(entryPoint)
            )
        )
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_AMEX_SET_STATUS_ZERO_AMOUNT),
                HexUtil.parseHex("01")
            )
        )
        val kernelConfig = ByteArray(1)
        if (amexParameter.SupportDRL) {
            kernelConfig[0] = kernelConfig[0] or 0x01
        }
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_AMEX_SET_KERNEL_CONFIG),
                kernelConfig
            )
        )
        val qualifiers = ByteArray(4)
        if (amexParameter.SupportContact) {
            qualifiers[0] = qualifiers[0] or 0x80.toByte()
        }
        if (amexParameter.SupportMagstripe) {
            qualifiers[0] = qualifiers[0] or 0x40
        }
        if (amexParameter.TryAnotherInterface) {
            qualifiers[0] = qualifiers[0] or 0x04
        }
        if (amexParameter.SupportCDCVM) {
            qualifiers[1] = qualifiers[1] or 0x80.toByte()
        }
        if (amexParameter.SupportOnlinePIN) {
            qualifiers[1] = qualifiers[1] or 0x40
        }
        if (amexParameter.SupportSignature) {
            qualifiers[1] = qualifiers[1] or 0x20
        }
        if (amexParameter.OfflineOnly) {
            qualifiers[2] = qualifiers[2] or 0x80.toByte()
        }
        if (amexParameter.ExemptNoCVMCheck) {
            qualifiers[3] = qualifiers[3] or 0x80.toByte()
        }
        if (amexParameter.DelayedAuthorization) {
            qualifiers[3] = qualifiers[3] or 0x40
        }
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_AMEX_SET_QUALIFIERS),
                qualifiers
            )
        )
        bundle.putByteArray(EmvTerminalConstraints.CONFIG, tlvBuilder.buildArray())
        getDefault().EmvSetTerminal(EmvTerminalConstraints.TYPE_AMEX, bundle)
    }

    override suspend fun loadMir() {
        val bundle = Bundle()
        val tlvBuilder = BerTlvBuilder()
        val mirParameter = MirParameter()
        val entryPoint = EntryPoint()
        entryPoint.CTLCheck = true
        entryPoint.CVMCheck = true
        entryPoint.CFLCheck = true
        entryPoint.StatusCheck = true
        entryPoint.ZeroAmountCheck = true
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_MIR_SET_ENTRY_POINT),
                entryPointToData(entryPoint)
            )
        )
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_MIR_SET_STATUS_ZERO_AMOUNT),
                HexUtil.parseHex("01")
            )
        )
        val qualifiers = ByteArray(4)
        if (mirParameter.SupportOnlinePIN) {
            qualifiers[0] = qualifiers[0] or 0x80.toByte()
        }
        if (mirParameter.SupportSignature) {
            qualifiers[0] = qualifiers[0] or 0x40
        }
        if (mirParameter.SupportCDCVM) {
            qualifiers[0] = qualifiers[0] or 0x20
        }
        if (mirParameter.UnableOnline) {
            qualifiers[0] = qualifiers[0] or 0x10
        }
        if (mirParameter.SupportContact) {
            qualifiers[0] = qualifiers[0] or 0x08
        }
        if (mirParameter.OfflineOnly) {
            qualifiers[0] = qualifiers[0] or 0x04
        }
        if (mirParameter.DelayedAuthorization) {
            qualifiers[0] = qualifiers[0] or 0x02
        }
        if (mirParameter.ATM) {
            qualifiers[0] = qualifiers[0] or 0x01
        }
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvTerminalConstraints.TAG_MIR_SET_QUALIFIERS),
                qualifiers
            )
        )
        bundle.putByteArray(EmvTerminalConstraints.CONFIG, tlvBuilder.buildArray())
        getDefault().EmvSetTerminal(EmvTerminalConstraints.TYPE_MIR, bundle)
    }

    override suspend fun loadVisaDRL() {
        val bundle = Bundle()
        val tlvBuilder = BerTlvBuilder()
        val entryPoint = EntryPoint()
        entryPoint.CTLCheck = true
        entryPoint.CVMCheck = true
        entryPoint.CFLCheck = true
        val limit = DynamicReaderLimit()
        limit.ProgramID = "01"
        limit.TransLimit = 200000
        limit.CVMLimit = 10000
        limit.FloorLimit = 10000
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvDrlConstraints.TAG_DRL_SET_DELIMITER),
                drlToData(limit, entryPoint)
            )
        )
        limit.ProgramID = "010203"
        limit.TransLimit = 200000
        limit.CVMLimit = 10000
        limit.FloorLimit = 10000
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvDrlConstraints.TAG_DRL_SET_DELIMITER),
                drlToData(limit, entryPoint)
            )
        )
        limit.ProgramID = "0102030405"
        limit.TransLimit = 200000
        limit.CVMLimit = 10000
        limit.FloorLimit = 10000
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvDrlConstraints.TAG_DRL_SET_DELIMITER),
                drlToData(limit, entryPoint)
            )
        )
        bundle.putByteArray(EmvDrlConstraints.CONFIG, tlvBuilder.buildArray())
        getDefault().EmvSetDRL(EmvDrlConstraints.TYPE_VISA, bundle)
    }

    override suspend fun loadAmexDRL() {
        val bundle = Bundle()
        val tlvBuilder = BerTlvBuilder()
        val entryPoint = EntryPoint()
        entryPoint.CTLCheck = true
        entryPoint.CVMCheck = true
        entryPoint.CFLCheck = true
        val limits = arrayOfNulls<DynamicReaderLimit>(17)
        for (i in limits.indices) {
            limits[i] = DynamicReaderLimit()
            when (i) {
                0, 2, 15 -> limits[i]!!.ProgramID = "1"
                else -> {
                }
            }
            limits[i]!!.TransLimit = 200000
            limits[i]!!.CVMLimit = 10000
            limits[i]!!.FloorLimit = 10000
        }
        for (limit in limits) {
            if (limit!!.ProgramID == null) {
                tlvBuilder.addEmpty(BerTag(EmvDrlConstraints.TAG_DRL_SET_DELIMITER))
                continue
            }
            limit.ProgramID = null
            tlvBuilder.addBerTlv(
                BerTlv(
                    BerTag(EmvDrlConstraints.TAG_DRL_SET_DELIMITER),
                    drlToData(limit, entryPoint)
                )
            )
        }
        bundle.putByteArray(EmvDrlConstraints.CONFIG, tlvBuilder.buildArray())
        getDefault().EmvSetDRL(EmvDrlConstraints.TYPE_AMEX, bundle)
    }

    override suspend fun loadService() {
        val bundle = Bundle()
        val tlvBuilder = BerTlvBuilder()
        var service = Service()
        service.ServiceID = PosUtils.hexStringToBytes("1010")
        service.Priority = 60
        service.ServiceManage = PosUtils.hexStringToBytes("B500")
        service.ServiceData = PosUtils.hexStringToBytes(
            "0106053005010111000002000102010206000A0101030201020304050607081122330102010215050110101000000000100001000000010203000102040100000000000000000000000000000000000000000000000000000000000000000000"
        )
        service.PRMiss = PosUtils.hexStringToBytes("13131313131313131313131313131313")
        service.PRMacq = ArrayList()
        var acq = PRMacq()
        acq.Index = 1
        acq.Key = PosUtils.hexStringToBytes("D694705EDF0DFBB52023C134CEB954E5")
        acq.Kcv = PosUtils.hexStringToBytes("A32BBA")
        (service.PRMacq as ArrayList<PRMacq>).add(acq)
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvServiceConstraints.TAG_SERVICE_SET_DELIMITER),
                serviceToData(service)
            )
        )
        service = Service()
        service.ServiceID = PosUtils.hexStringToBytes("1011")
        service.Priority = 60
        service.ServiceManage = PosUtils.hexStringToBytes("B500")
        service.ServiceData = PosUtils.hexStringToBytes(
            "0106053005010111000002000102010206000A0101030201020304050607081122330102010215050110101000000000100001000000010203000102040100000000000000000000000000000000000000000000000000000000000000000000"
        )
        service.PRMiss = PosUtils.hexStringToBytes("13131313131313131313131313131313")
        service.PRMacq = ArrayList()
        acq = PRMacq()
        acq.Key = PosUtils.hexStringToBytes("D694705EDF0DFBB52023C134CEB954E5")
        acq.Kcv = PosUtils.hexStringToBytes("A32BBA")
        (service.PRMacq as ArrayList<PRMacq>).add(acq)
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvServiceConstraints.TAG_SERVICE_SET_DELIMITER),
                serviceToData(service)
            )
        )
        bundle.putByteArray(EmvServiceConstraints.CONFIG, tlvBuilder.buildArray())
        getDefault().EmvSetService(bundle)
    }

    private fun entryPointToData(entryPoint: EntryPoint): ByteArray? {
        val data = ByteArray(1)
        if (entryPoint.CVMCheck) {
            data[0] = data[0] or  0x08
        }
        if (entryPoint.CFLCheck) {
            data[0] = data[0] or 0x10
        }
        if (entryPoint.CTLCheck) {
            data[0] = data[0] or 0x20
        }
        if (entryPoint.StatusCheck) {
            data[0] = data[0] or 0x80.toByte()
        }
        if (entryPoint.ZeroAmountCheck) {
            data[0] = data[0] ?: 0x40
        }
        return data
    }

    private fun drlToData(limit: DynamicReaderLimit?, entryPoint: EntryPoint): ByteArray? {
        val tlvBuilder = BerTlvBuilder()
        if (limit!!.ProgramID != null) {
            tlvBuilder.addBerTlv(
                BerTlv(
                    BerTag(EmvDrlConstraints.TAG_DRL_SET_PROGRAM_ID),
                    limit.ProgramID?.toByteArray()
                )
            )
        }
        tlvBuilder.addIntAsHex(
            BerTag(EmvDrlConstraints.TAG_DRL_SET_TRANSACTION_LIMIT),
            limit.TransLimit
        )
        tlvBuilder.addIntAsHex(
            BerTag(EmvDrlConstraints.TAG_DRL_SET_CVM_REQUIRED_LIMIT),
            limit.CVMLimit
        )
        tlvBuilder.addIntAsHex(BerTag(EmvDrlConstraints.TAG_DRL_SET_FLOOR_LIMIT), limit.FloorLimit)
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvDrlConstraints.TAG_DRL_SET_ENTRY_POINT),
                entryPointToData(entryPoint)
            )
        )
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvDrlConstraints.TAG_DRL_SET_STATUS_ZERO_AMOUNT),
                HexUtil.parseHex("01")
            )
        )
        return tlvBuilder.buildArray()
    }

    private fun prmacqToData(prmacq: PRMacq): ByteArray? {
        val tlvBuilder = BerTlvBuilder()
        tlvBuilder.addByte(BerTag(EmvServiceConstraints.TAG_PRMACQ_SET_INDEX), prmacq.Index)
        tlvBuilder.addBerTlv(BerTlv(BerTag(EmvServiceConstraints.TAG_PRMACQ_SET_KEY), prmacq.Key))
        tlvBuilder.addBerTlv(BerTlv(BerTag(EmvServiceConstraints.TAG_PRMACQ_SET_KCV), prmacq.Kcv))
        return tlvBuilder.buildArray()
    }

    private fun serviceToData(service: Service): ByteArray? {
        val tlvBuilder = BerTlvBuilder()
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvServiceConstraints.TAG_SERVICE_SET_ID),
                service.ServiceID
            )
        )
        tlvBuilder.addByte(BerTag(EmvServiceConstraints.TAG_SERVICE_SET_PRIORITY), service.Priority)
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvServiceConstraints.TAG_SERVICE_SET_MANAGEMENT),
                service.ServiceManage
            )
        )
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvServiceConstraints.TAG_SERVICE_SET_DATA),
                service.ServiceData
            )
        )
        tlvBuilder.addBerTlv(
            BerTlv(
                BerTag(EmvServiceConstraints.TAG_SERVICE_SET_PRMISS),
                service.PRMiss
            )
        )
        if (service.PRMacq != null) {
            val acqTlvBuilder = BerTlvBuilder()
            for (prmacq in service.PRMacq!!) {
                if (prmacq == null) {
                    continue
                }
                if (prmacq.Key == null || !(prmacq.Key!!.size == 8 || prmacq.Key!!.size == 16)) {
                    continue
                }
                if (prmacq.Kcv == null || prmacq.Kcv!!.size != 3) {
                    continue
                }
                acqTlvBuilder.addBerTlv(
                    BerTlv(
                        BerTag(EmvServiceConstraints.TAG_PRMACQ_SET_DELIMITER),
                        prmacqToData(prmacq)
                    )
                )
            }
            tlvBuilder.addBerTlv(
                BerTlv(
                    BerTag(EmvServiceConstraints.TAG_SERVICE_SET_PRMACQ),
                    acqTlvBuilder.buildArray()
                )
            )
        }
        return tlvBuilder.buildArray()
    }

    internal class EntryPoint {
        var CTLCheck = false
        var CVMCheck = false
        var CFLCheck = false
        var StatusCheck = false
        var ZeroAmountCheck = false
    }

    internal class QvsdcParameter {
        var SupportMagstripe = false
        var SupportQVSDC = true
        var SupportContact = true
        var OfflineOnly = false
        var SupportOnlinePIN = true
        var SupportSignature = true
        var SupportOnlineODA = false
        var SupportIssuerScriptUpdate = false
        var SupportCDCVM = true
        var SupportDRL = false
    }

    internal class UnionPayParameter {
        var SupportEMV = true
        var SupportContact = true
        var OfflineOnly = false
        var SupportOnlinePIN = true
        var SupportSignature = true
        var SupportCDCVM = true
    }

    internal class DiscoverParameter {
        var SupportMagstripe = true
        var SupportEMV = true
        var SupportContact = true
        var OfflineOnly = false
        var SupportOnlinePIN = true
        var SupportSignature = true
        var SupportIssuerScriptUpdate = false
        var SupportCDCVM = true
    }

    internal class AmexParameter {
        var SupportContact = true
        var SupportMagstripe = true
        var TryAnotherInterface = true
        var SupportCDCVM = true
        var SupportOnlinePIN = true
        var SupportSignature = true
        var OfflineOnly = false
        var ExemptNoCVMCheck = false
        var DelayedAuthorization = false
        var SupportDRL = false
    }

    internal class MirParameter {
         var SupportOnlinePIN = true
         var SupportSignature = true
         var SupportCDCVM = true
         var UnableOnline = false
         var SupportContact = true
         var OfflineOnly = false
         var DelayedAuthorization = false
         var ATM = false
    }

    internal class DynamicReaderLimit {
        var ProgramID: String? = null
         var TransLimit = 0
         var CVMLimit = 0
         var FloorLimit = 0
    }

    internal class Service {
         var Priority: Byte = 0
         var ServiceID: ByteArray = byteArrayOf(0)
         var ServiceManage: ByteArray = byteArrayOf(0)
         var ServiceData: ByteArray = byteArrayOf(0)
         var PRMiss: ByteArray = byteArrayOf(0)
         var PRMacq: MutableList<PRMacq>? = null
    }

    internal class PRMacq {
         var Index: Byte = 0
         var Key: ByteArray? = byteArrayOf(0)
         var Kcv: ByteArray? = byteArrayOf(0)
    }
}
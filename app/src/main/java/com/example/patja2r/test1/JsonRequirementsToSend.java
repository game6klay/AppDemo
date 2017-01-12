package com.example.patja2r.test1;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;


/**
 * Created by patja2r on 6/1/2016.
 */
public class JsonRequirementsToSend extends Activity {

    // Device ID String
    String   imeiString = null;
    String   imsiString = null;

    {
        TelephonyManager telephonyManager;

        telephonyManager  =
                ( TelephonyManager )getSystemService( Context.TELEPHONY_SERVICE );

    /*
     * getDeviceId() function Returns the unique device ID.
     * for example,the IMEI for GSM and the MEID or ESN for CDMA phones.
     */
        imeiString = telephonyManager.getDeviceId();

    /*
     * getSubscriberId() function Returns the unique subscriber ID,
     * for example, the IMSI for a GSM phone.
     */
        imsiString = telephonyManager.getSubscriberId();
    }

    // Push ID String
}


package com.coderjj.phonedefend.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.coderjj.phonedefend.utils.ConstantValue;
import com.coderjj.phonedefend.utils.SpUtil;

/**
 * 添加重启手机广播权限
 */
public class BootReceiver extends BroadcastReceiver {
    public static final String TAG = "BootReceiverlalala";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");

        @SuppressLint("ServiceCast") TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELECOM_SERVICE);
        @SuppressLint("MissingPermission") String sim_num = tm.getSimSerialNumber();
        String simSeriaNumber = SpUtil.getString(context, ConstantValue.SIM, "");
        if (!simSeriaNumber.equals(sim_num)){
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage("5556",null,"iloveyou",null,null);
        }
    }
}

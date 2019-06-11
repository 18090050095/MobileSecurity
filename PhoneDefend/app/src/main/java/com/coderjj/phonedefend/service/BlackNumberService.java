package com.coderjj.phonedefend.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.coderjj.phonedefend.db.dao.BlackNumberDao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BlackNumberService extends Service {

    private InnerSmsReceiver mInnerSmsReceiver;
    private BlackNumberDao mDao;
    private TelephonyManager mTM;
    private MyPhoneStateListener mPhoneStateListener;
    private MyContentObserver mMyContentObserver;

    @Override
    public void onCreate() {
        Log.d("BlackNumberService", "onCreate: ");
        //拦截短信
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(1000);

        mInnerSmsReceiver = new InnerSmsReceiver();
        registerReceiver(mInnerSmsReceiver, intentFilter);

        //监听电话的状态
        //1.电话管理者对象
        mTM = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //2.监听电话的状态(添加权限)
        mPhoneStateListener = new MyPhoneStateListener();
        mTM.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        mDao = BlackNumberDao.getInstance(this);
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        //注销短信接收广播
        if (mInnerSmsReceiver != null) {
            unregisterReceiver(mInnerSmsReceiver);
        }
        //注销内容观察者
        if (mMyContentObserver != null) {
            getContentResolver().unregisterContentObserver(mMyContentObserver);
        }
        // 取消电话的监听
        if (mPhoneStateListener != null) {
            mTM.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        super.onDestroy();
    }

    class InnerSmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取短信内容，获取发送短信的电话号码，如果号码在黑名单中，且状态为1或者3，则拦截
            //获取短信内容
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            //循环短信对象的基本信息
            for (Object object : objects) {
                //获取短信对象
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
                //获取短信对象的基本信息
                String originatingAddress = sms.getOriginatingAddress();
                String messageBody = sms.getMessageBody();

                int mode = mDao.getMode(originatingAddress);

                Log.d("BlackNumberService", "onReceive: " + mode);
                if (mode == 1 || mode == 3) {
                    //拦截短信
                    abortBroadcast();
                }
            }
        }
    }

    class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    //空闲状态
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //摘机状态
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //响铃状态___挂断操作
                    endCall(incomingNumber);
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    /**
     * 利用aidl和反射的手段获取到系统未开放给开发者的 ITelephony 的 endCall()
     * 方法来实现拦截电话的目的
     *
     * @param incomingNumber 来电号码
     */
    private void endCall(String incomingNumber) {
        int mode = mDao.getMode(incomingNumber);

        Log.d("BlackNumberService", "onReceive: " + mode);
        if (mode == 2 || mode == 3) {
            //拦截电话
            //ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
            //ServiceManager对开发者隐藏，不能直接调用，只能反射调用
            //1.获取ServiceManager字节码文件
            try {
                Class<?> clazz = Class.forName("android.os.ServiceManager");
                //2.获取方法
                Method method = clazz.getMethod("getService", String.class);
                //3.反射调用此方法
                IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
                //4.获取aidl文件对象方法
                ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
                //5.为所欲为__调用隐藏方法
                iTelephony.endCall();
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*删除通话记录(添加权限)
              本次还没有插入就进行删除所以造成本次未删除成功
            getContentResolver().delete(Uri.parse("content://call_log/calls"),
                    "number = ?",new String[]{incomingNumber});*/
            //通过内容观察者，观察数据库的变化
            mMyContentObserver = new MyContentObserver(new Handler(), incomingNumber);
            getContentResolver().registerContentObserver(
                    Uri.parse("content://call_log/calls"), true, mMyContentObserver);
        }
    }

    class MyContentObserver extends ContentObserver {

        private String mPhone;

        public MyContentObserver(Handler handler, String phone) {
            super(handler);
            mPhone = phone;
        }

        @Override
        public void onChange(boolean selfChange) {
            //观察到数据变化后再删除
            getContentResolver().delete(Uri.parse("content://call_log/calls"),
                    "number = ?", new String[]{mPhone});
            super.onChange(selfChange);
        }
    }
}

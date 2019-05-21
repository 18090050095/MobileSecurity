package com.coderjj.phonedefend.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Movie;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.coderjj.phonedefend.R;
import com.coderjj.phonedefend.engine.AddressDao;
import com.coderjj.phonedefend.utils.ConstantValue;
import com.coderjj.phonedefend.utils.SpUtil;

public class AddressService extends Service {

    private TelephonyManager mTM;
    private MyPhoneStateListener mPhoneStateListener;
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private WindowManager mWM;
    private String mAddress;
    private TextView tv_toast;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_toast.setText(mAddress);
        }
    };
    private View mView;
    private int[] mBgArray;
    private int mScreenWidth;
    private int mScreenHeight;
    private InnerOutCallReceiver mInnerOutCallReceiver;

    @Override
    public void onCreate() {
        //管理吐司的显示
        //电话状态监听
        //1.电话管理者对象
        mTM = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //2.监听电话的状态(添加权限)
        mPhoneStateListener = new MyPhoneStateListener();
        mTM.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = mWM.getDefaultDisplay().getWidth();
        mScreenHeight = mWM.getDefaultDisplay().getHeight();

        mBgArray = new int[]{
                R.drawable.toast_style_transparent,
                R.drawable.toast_style_orange,
                R.drawable.toast_style_blue,
                R.drawable.toast_style_gray,
                R.drawable.toast_style_green,
        };

        //监听去电广播(权限)
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        mInnerOutCallReceiver = new InnerOutCallReceiver();
        registerReceiver(mInnerOutCallReceiver, intentFilter);
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        //取消电话状态监听
        if (mPhoneStateListener != null && mTM != null) {
            mTM.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        //注销广播监听
        if (mInnerOutCallReceiver != null) {
            unregisterReceiver(mInnerOutCallReceiver);
        }
        super.onDestroy();
    }

    class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    //空闲状态
                    Log.d("onCallStateChanged", "onCallStateChanged: " + "CALL_STATE_IDLE");
                    if (mWM != null && mView != null) {
                        mWM.removeView(mView);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //摘机状态
                    Log.d("onCallStateChanged", "onCallStateChanged: " + "CALL_STATE_OFFHOOK");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //响铃状态
                    Log.d("onCallStateChanged", "onCallStateChanged: " + "CALL_STATE_RINGING");
                    showToast(incomingNumber);
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    private void showToast(String incomingNumber) {

        final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        /*params.windowAnimations = com.android.internal.R.style.Animation_Toast;
        设置与响铃时一致的type*/
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                /*| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;*/

        params.gravity = Gravity.LEFT + Gravity.TOP;
        //获取吐司需要显示的位置
        params.x = SpUtil.getInt(getApplication(), ConstantValue.LOCATION_X, 0);
        params.y = SpUtil.getInt(getApplication(), ConstantValue.LOCATION_Y, 0);
        //吐司显示效果(吐司布局文件)，xml-->view，将吐司挂载到windowManger窗体上
        mView = View.inflate(getApplicationContext(), R.layout.toast_view, null);
        mWM.addView(mView, mParams);
        tv_toast = mView.findViewById(R.id.tv_toast);
        int resIndex = SpUtil.getInt(this, ConstantValue.TOAST_STYLE_INDEX, 0);
        tv_toast.setBackgroundResource(mBgArray[resIndex]);
        queryAdress(incomingNumber);

        //初始化吐司拖拽监听
        mView.setOnTouchListener(new View.OnTouchListener() {
            private int startY;
            private int startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();

                        int disX = moveX - startX;
                        int disY = moveY - startY;

                        params.x = params.x + disX;
                        params.y = params.y + disY;

                        //超出边界处理
                        if (params.x < 0) {
                            params.x = 0;
                        }
                        if (params.y < 0) {
                            params.y = 0;
                        }
                        if (params.x > mScreenWidth - mView.getWidth()) {
                            params.x = mScreenWidth - mView.getWidth();
                        }
                        if (params.y > mScreenHeight - mView.getHeight() - 22) {
                            params.y = mScreenHeight - mView.getHeight() - 22;
                        }

                        mWM.updateViewLayout(mView, params);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //存储移动到的位置
                        SpUtil.putInt(getApplication(), ConstantValue.LOCATION_X, params.x);
                        SpUtil.putInt(getApplication(), ConstantValue.LOCATION_Y, params.y);
                        break;
                }
                return true;
            }
        });
    }


    private void queryAdress(final String incomingNumber) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAddress = AddressDao.getAddress(incomingNumber);
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private class InnerOutCallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String phone = getResultData();
            showToast(phone);
        }
    }
}

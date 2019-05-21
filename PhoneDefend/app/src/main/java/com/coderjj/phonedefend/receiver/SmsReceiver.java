package com.coderjj.phonedefend.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.coderjj.phonedefend.R;
import com.coderjj.phonedefend.service.LocationService;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //获取短信内容
        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        //循环短信对象的基本信息
        for (Object object: objects){
            //获取短信对象
            SmsMessage sms = SmsMessage.createFromPdu((byte[])object);
            //获取短信对象的基本信息
            String originatingAddress = sms.getOriginatingAddress();
            String messageBody = sms.getMessageBody();

            //判断是否含有音乐关键字
            /*if(messageBody.contains("#*alarm*#")){
                //播放音乐
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }*/
            //判断是否含有定位关键字
            if(messageBody.contains("#*location*#")){
                //开启获取位置的服务
                context.startService(new Intent(context, LocationService.class));
            }
        }
    }
}

package com.coderjj.phonedefend.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2019/5/16.
 */

public class ServiceUtil {
    /**
     * 判断服务是否正在运行
     *
     * @param serviceName 服务名称
     * @param context
     * @return true 正在运行 false 没在运行
     */
    public static boolean isRunning(Context context, String serviceName) {
        ActivityManager mAM = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = mAM.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : runningServices) {
            if (info.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }
}

package com.coderjj.phonedefend.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.coderjj.phonedefend.bean.AppInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/5/29.
 */

public class AppInfoProvider {
    /**
     * 获取安装在手机上应用相关信息集合方法
     * @param context
     * @return
     */
    public static List<AppInfoBean> getAppInfoList(Context context){
        List<AppInfoBean> appInfoBeans = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        //0:安装在手机上的所有应用信息
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo:installedPackages){
            AppInfoBean appInfo= new AppInfoBean();
            appInfo.packageName = packageInfo.packageName;
            appInfo.name = packageInfo.applicationInfo.loadLabel(pm).toString();
            appInfo.icon = packageInfo.applicationInfo.loadIcon(pm);
            //是否为系统应用
            if ((packageInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.
                    FLAG_SYSTEM){
                //系统应用
                appInfo.isSystem = true;
            }else{
                //非系统应用
                appInfo.isSystem = false;
            }
            //是否为sd应用
            if ((packageInfo.applicationInfo.flags& ApplicationInfo.FLAG_EXTERNAL_STORAGE)==ApplicationInfo.
                    FLAG_EXTERNAL_STORAGE){
                //系统应用
                appInfo.isSdcard = true;
            }else{
                //非系统应用
                appInfo.isSdcard = false;
            }
            appInfoBeans.add(appInfo);
        }
        return appInfoBeans;
    }
}

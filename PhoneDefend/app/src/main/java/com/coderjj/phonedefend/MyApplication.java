package com.coderjj.phonedefend;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2019/5/7.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(false); //是否输出debug日志，开启debug会影响性能。
    }
}

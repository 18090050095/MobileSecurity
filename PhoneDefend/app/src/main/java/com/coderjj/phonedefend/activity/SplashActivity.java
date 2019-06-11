package com.coderjj.phonedefend.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coderjj.phonedefend.R;
import com.coderjj.phonedefend.utils.ConstantValue;
import com.coderjj.phonedefend.utils.SpUtil;
import com.coderjj.phonedefend.utils.StreamUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final int UPDATE_VERSION = 100;
    private static final int ENTER_HOME = 101;
    private TextView tvVersionName;
    private int mLocalVersionCode;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_VERSION:
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    enterHome();
                    break;
                default:
                    break;
            }
        }
    };
    private String mVersionDes;
    private String mDownloadUrl;
    private RelativeLayout rl_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
        initData();
        initAnimation();

        //初始化数据库
        initDB();
    }

    private void initDB() {
        //1.归属地数据拷贝
        initAddressDB("address.db");
        //2.常用电话号码数据拷贝
        initAddressDB("commonnum.db");
    }

    /**
     * 拷贝数据库到Files文件夹下
     *
     * @param dbName
     */
    private void initAddressDB(String dbName) {
//        getCacheDir();
//        Environment.getExternalStorageDirectory().getAbsolutePath();
        //在files文件夹下创建同名dbName文件过程
        File files = getFilesDir();
        File file = new File(files, dbName);
        if (file.exists()) {
            return;
        }
        //2.输入流读取第三方资源目录下的文件
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            //获取输入流->被复制的文件、输出流->目标文件
            inputStream = getAssets().open(dbName);
            fos = new FileOutputStream(file);
            //4.每次的读取内容大小
            byte[] bs = new byte[1024];
            int temp = -1;
            while ((temp = inputStream.read(bs)) != -1) {
                fos.write(bs, 0, temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null && fos != null){
                try {
                    inputStream.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 添加淡入动画效果
     */
    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);
        rl_root.startAnimation(alphaAnimation);
    }

    /**
     * 初始化UI方法
     */
    private void initView() {
        tvVersionName = findViewById(R.id.tv_version_name);
        rl_root = findViewById(R.id.rl_root);
    }

    /**
     * 获取数据方法
     */
    private void initData() {
        //1.应用版本名称
        tvVersionName.setText(getVersionName());
        //2.检测更新
        //2.1获取本地版本号
        mLocalVersionCode = getVersionCode();
        //2.2获取服务器版本号
        //json中内容包含：
        /*
         * 更新版本的版本名称
         * 新版本的描述信息
         * 服务器版本号
         * 新版apk下载地址
         */
        if (SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false)) {
            checkVersion();
        } else {
            //直接进入应用程序
//            enterHome();
            mHandler.sendEmptyMessageDelayed(ENTER_HOME, 4000);
        }


    }

    /**
     * 弹出对话框，提示用户更新
     */
    private void showUpdateDialog() {
        //对话框是依赖于activity存在的
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置左上角图标
        builder.setIcon(R.drawable.ic_launcher_background);
        builder.setTitle("版本更新");
        //设置描述内容
        builder.setMessage(mVersionDes);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadApk();
            }
        });
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消对话框，进入主界面
                enterHome();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void downloadApk() {
        //apk下载链接地址，放置apk的所在路径
        //1.判断sd是否可用，是否挂载上
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //2.获取sd路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myapp/mobilesafe74.apk";
            //3.发送请求，获取apk，并且放置在指定路径
            RequestParams params = new RequestParams(mDownloadUrl);
            params.setAutoRename(true);
            params.setSaveFilePath(path);
            //发送请求
            x.http().post(params, new Callback.ProgressCallback<File>() {
                @Override
                public void onSuccess(File result) {
                    Log.i(TAG, "onSuccess");
                    installApk(result);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.i(TAG, "onError");
                }

                @Override
                public void onCancelled(CancelledException cex) {
                }

                @Override
                public void onFinished() {
                    Log.i(TAG, "onFinished");
                }

                //网络请求之前回调
                @Override
                public void onWaiting() {
                }

                //网络请求开始的时候回调
                @Override
                public void onStarted() {
                    Log.i(TAG, "onStarted");
                }

                //下载的时候不断回调的方法
                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    //当前进度和文件总大小
                    Log.i(TAG, "onLoading_______" + "current：" + current + "，total：" + total);
                }
            });
        }
    }

    /**
     * 安装apk
     *
     * @param result
     */
    private void installApk(File result) {
        //系统界面，源码，安装apk入口
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        Uri contentUri = FileProvider.getUriForFile(
                this
                , "com.coderjj.phonedefend.fileprovider"
                , result);
        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

    /**
     * 进入主界面
     */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

        finish();
    }

    /**
     * 检测版本号
     */
    private void checkVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //172.22.140.1为电脑IP 测试阶段不是最优
//                new URL("http://172.22.140.1:8080/update74.json");
                //仅限于模拟器访问电脑tomcat
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    URL url = new URL("http://10.0.2.2/update74.json");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(2000);
                    connection.setReadTimeout(2000);
                    //默认get
//                    connection.setRequestMethod("GET");
                    if (connection.getResponseCode() == 200) {
                        InputStream is = connection.getInputStream();
                        String json = StreamUtil.streamToString(is);
                        Log.d(TAG, json);
                        //7.json解析
                        JSONObject jsonObject = new JSONObject(json);
                        String versionName = jsonObject.getString("versionName");
                        mVersionDes = jsonObject.getString("versionDes");
                        String versionCode = jsonObject.getString("versionCode");
                        mDownloadUrl = jsonObject.getString("downloadUrl");
                        Log.d(TAG, versionName + mVersionDes + versionCode + mDownloadUrl);

                        //8.比对版本号(服务器版本号>本地版本号，提示用户更新)
                        if (mLocalVersionCode < Integer.parseInt(versionCode)) {
                            //提示用户更新，弹出对话框(UI)，消息机制
                            msg.what = UPDATE_VERSION;
                        } else {
                            msg.what = ENTER_HOME;
                            //进入主界面
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    long endTime = System.currentTimeMillis();
                    if (endTime - startTime < 4000) {
                        try {
                            Thread.sleep(4000 - (endTime - startTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    /**
     * 获取版本号
     *
     * @return
     */
    private int getVersionCode() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本名称
     *
     * @return 应用版本名称 返回null代表异常
     */
    private String getVersionName() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

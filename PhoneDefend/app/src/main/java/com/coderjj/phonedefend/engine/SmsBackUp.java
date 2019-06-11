package com.coderjj.phonedefend.engine;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 短信备份工具类
 * Created by Administrator on 2019/5/22.
 */

public class SmsBackUp {

    private static int index = 0;

    public static void backUp(Context context, String path, CallBack callBack) {
        FileOutputStream fileOutputStream = null;
        Cursor cursor = null;
        try {
            //1.获取备份短信写入的文件
            File file = new File(path);
            //2.获取内容解析器，短信数据库中的数据
            cursor = context.getContentResolver().query(Uri.parse("content://sms/"),
                    new String[]{"address", "date", "type", "body"}, null, null, null);
            //3.文件相应的输出流
            fileOutputStream = new FileOutputStream(file);
            //4.序列化数据库中读取的数据，放到xml中
            XmlSerializer newSerializer = Xml.newSerializer();
            //5.设置xml
            newSerializer.setOutput(fileOutputStream, "utf-8");

            newSerializer.startDocument("utf-8", true);
            newSerializer.startTag(null, "smss");

            //6.获取短信总数
//            pd.setMax(cursor.getCount());
            if (callBack != null) {
                callBack.setMax(cursor.getCount());
            }

            //7.读取读取数据库数据到xml中
            while (cursor.moveToNext()) {
                newSerializer.startTag(null, "sms");

                newSerializer.startTag(null, "address");
                newSerializer.text(cursor.getString(0));
                newSerializer.endTag(null, "address");

                newSerializer.startTag(null, "date");
                newSerializer.text(cursor.getString(1));
                newSerializer.endTag(null, "date");

                newSerializer.startTag(null, "type");
                newSerializer.text(cursor.getString(2));
                newSerializer.endTag(null, "type");

                newSerializer.startTag(null, "body");
                newSerializer.text(cursor.getString(3));
                newSerializer.endTag(null, "body");

                newSerializer.endTag(null, "sms");

                //8.每循环一次更新进度条一次
                index++;
                Thread.sleep(500);
                //progressDialog可以在子线程更新进度条
//                pd.setProgress(index);
                if (callBack != null) {
                    callBack.updateProgress(index);
                }
            }

            newSerializer.endTag(null, "smss");
            newSerializer.endDocument();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cursor != null && fileOutputStream != null) {
                    fileOutputStream.close();
                    cursor.close();
                }
                index = 0;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface CallBack {
        void setMax(int max);

        void updateProgress(int progress);
    }
}

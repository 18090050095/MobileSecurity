package com.coderjj.phonedefend.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2019/5/15.
 */

public class AddressDao {
    //1.指定访问数据库路径
    public static String path = "data/data/com.coderjj.phonedefend/files/address.db";
    private static String mAddressStr;


    /**
     * 传递一个电话号码，开启数据库连接，进行访问，返回一个归属地
     *
     * @param phone
     */
    public static String getAddress(String phone) {
        mAddressStr = "no_match";
        //正则表达式，匹配电话号码
        boolean matches = phone.matches("^1[3-8]\\d{9}");
        if (matches) {
            phone = phone.substring(0, 7);
            //只读的方式开启数据库连接，进行访问
            SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursorId = db.query("data1", new String[]{"outkey"}, "id = ?",
                    new String[]{phone}, null, null, null);
            //查到即可 确定只有一条数据
            if (cursorId.moveToNext()) {
                String outkey = cursorId.getString(0);
                Cursor cursorAddress = db.query("data2", new String[]{"location"}, "id = ?",
                        new String[]{outkey}, null, null, null);
                if (cursorAddress.moveToNext()) {
                    mAddressStr = cursorAddress.getString(0);
                }
                cursorAddress.close();
            }
            cursorId.close();
        }else{
            mAddressStr = "unknown_number";
        }
        return mAddressStr;
    }
}

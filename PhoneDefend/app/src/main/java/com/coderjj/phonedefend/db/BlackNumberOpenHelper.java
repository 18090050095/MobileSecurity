package com.coderjj.phonedefend.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.coderjj.phonedefend.utils.ConstantValue;

/**
 * Created by Administrator on 2019/5/20.
 */

public class BlackNumberOpenHelper extends SQLiteOpenHelper {

    public BlackNumberOpenHelper(Context context) {
        super(context, ConstantValue.BLACK_NUMBER, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table blacknumber (" +
                "_id integer primary key autoincrement , " +
                "phone varchar (20), " +
                "mode varchar(5));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

package com.coderjj.phonedefend.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coderjj.phonedefend.db.BlackNumberOpenHelper;
import com.coderjj.phonedefend.db.domain.BlackNumberInfo;
import com.coderjj.phonedefend.utils.ConstantValue;

import java.util.ArrayList;

/**
 * Created by Administrator on 2019/5/20.
 */

public class BlackNumberDao {
    private static BlackNumberDao mBlackNumberDao;
    private final BlackNumberOpenHelper mBlackNumberOpenHelper;

    private BlackNumberDao(Context context) {
        mBlackNumberOpenHelper = new BlackNumberOpenHelper(context);
    }

    public static BlackNumberDao getInstance(Context context){
        if (mBlackNumberDao == null) {
            mBlackNumberDao = new BlackNumberDao(context);
        }
        return mBlackNumberDao;
    }

    public void insert (String phone,String mode){
        SQLiteDatabase db = mBlackNumberOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone",phone);
        contentValues.put("mode",mode);
        db.insert(ConstantValue.BLACK_NUMBER,null,contentValues);

        db.close();
    }

    public void delete(String phone){
        SQLiteDatabase db = mBlackNumberOpenHelper.getWritableDatabase();
        db.delete(ConstantValue.BLACK_NUMBER,"phone = ?",new String[]{phone});

        db.close();
    }

    public void update(String phone,String mode){
        SQLiteDatabase db = mBlackNumberOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mode",mode);
        db.update(ConstantValue.BLACK_NUMBER,contentValues,"phone = ?",new String[]{phone});

        db.close();
    }

    public ArrayList<BlackNumberInfo> findAll(){
        SQLiteDatabase db = mBlackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(ConstantValue.BLACK_NUMBER,new String[]{"phone","mode"},
                null,null,null,null,"_id desc");
        ArrayList<BlackNumberInfo> blackNumberInfos =new ArrayList<>();
        while (cursor.moveToNext()){
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            String phone = cursor.getString(0);
            String mode = cursor.getString(1);
            blackNumberInfo.setPhone(phone);
            blackNumberInfo.setMode(mode);

            blackNumberInfos.add(blackNumberInfo);
        }

        db.close();
        return blackNumberInfos;
    }

    /**
     * 每次查询20条数据
     * @param index 查询的索引值
     */
    public ArrayList<BlackNumberInfo> find(int index){
        SQLiteDatabase db = mBlackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select phone,mode from blacknumber order by _id desc limit ?,20;",new String[]{index+""});
        ArrayList<BlackNumberInfo> blackNumberInfos =new ArrayList<>();
        while (cursor.moveToNext()){
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            String phone = cursor.getString(0);
            String mode = cursor.getString(1);
            blackNumberInfo.setPhone(phone);
            blackNumberInfo.setMode(mode);

            blackNumberInfos.add(blackNumberInfo);
        }

        db.close();
        return blackNumberInfos;
    }

    /**
     * 查询数据表总条目数
     * @return 数据的总条目
     */
    public int getCount(){
        SQLiteDatabase db = mBlackNumberOpenHelper.getWritableDatabase();
        int count = 0;
        Cursor cursor = db.rawQuery("select count(*) from blacknumber ;",null);
        if (cursor.moveToNext()){
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    /**
     * phone作为查询条件的电话号码
     * @param originatingAddress
     * @return 传入电话号码的模式 1:短信 2:电话 3:所有 0:无数据
     */
    public int getMode(String originatingAddress) {
        SQLiteDatabase db = mBlackNumberOpenHelper.getWritableDatabase();
        int mode = 0;
        Cursor cursor = db.query(ConstantValue.BLACK_NUMBER,new String[]{"mode"},"phone = ?"
                ,new String[]{originatingAddress},null,null,null);
        if (cursor.moveToNext()){
            mode = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return mode;
    }
}

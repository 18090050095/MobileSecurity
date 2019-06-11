package com.coderjj.phonedefend.engine;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/5/15.
 */

public class CommonnumDao {
    //1.指定访问数据库路径
    public String path = "data/data/com.coderjj.phonedefend/files/commonnum.db";

    //2.开启数据(组)
    public List<Group> getGroup() {
        SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = database.query("classlist", new String[]{"name", "idx"}, null, null, null, null, null);
        List<Group> groupList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Group group = new Group();
            group.name = cursor.getString(0);
            group.idx = cursor.getString(1);
            group.childList = getChild(group.idx);
            groupList.add(group);
        }
        cursor.close();
        database.close();
        return groupList;
    }

    //获取每个组中的号码数据
    public List<Child> getChild(String idx) {
        SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = database.rawQuery("select * from table" + idx + ";", null);
        List<Child> childList = new ArrayList<>();
        while (cursor.moveToNext()){
            Child child = new Child();
            child._id = cursor.getString(0);
            child.number = cursor.getString(1);
            child.name = cursor.getString(2);
            childList.add(child);
        }
        cursor.close();
        database.close();
        return childList;
    }

    public class Group {
        public String name;
        public String idx;
        public List<Child> childList;
    }

    public class Child{
        public String _id;
        public String number;
        public String name;
    }
}

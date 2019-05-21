package com.coderjj.phonedefend.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.coderjj.phonedefend.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactActivity extends AppCompatActivity {

    private ListView mLvContact;
    private List<Map<String,String>> contactList;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            new MyAdapter();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        initView();
        initData();
    }

    /**
     * 从系统数据库获取联系人信息
     */
    private void initData() {
        contactList = new ArrayList();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 两个表的uri
                Uri uriRawContacts = Uri.parse("content://com.android.contacts/raw_contacts");
                Uri uriData = Uri.parse("content://com.android.contacts/data");
                ContentResolver resolver = getContentResolver();

                Cursor contactCursor = resolver.query(uriRawContacts,
                        new String[]{"contact_id"},
                        null, null, null);
                while (contactCursor.moveToNext()) {
                    String id = contactCursor.getString(0);
                    Cursor indexCursor = resolver.query(uriData,
                            new String[]{"data1", "mimetype"},
                            "raw_contact_id = ?",
                            new String[]{id + ""},
                            null);
                    // map储存联系人的所有资料
                    Map map = new HashMap<String, String>();
                    while (indexCursor.moveToNext()) {
                        String data = indexCursor.getString(0);
                        String mimetype = indexCursor.getString(1);
                        // 如果Mimetype满足相应的类型就储存到Map
                        if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                            if (!TextUtils.isEmpty(data)) {
                                map.put("phone", data);
                            }
                        }
                        if (mimetype.equals("vnd.android.cursor.item/name")) {
                            if (!TextUtils.isEmpty(data)) {
                                map.put("name", data);
                            }
                        }
                    }
                    indexCursor.close();
                    contactList.add(map);
                }
                contactCursor.close();
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void initView() {
        mLvContact = findViewById(R.id.lv_contact);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public Object getItem(int position) {
            return contactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                // 如果没有缓存则重新填充view
                view = View.inflate(ContactActivity.this, R.layout.item_contact,
                        null);
                // 优化将找到的组件封装到Pocket类里
                TextView tv_name = view.findViewById(R.id.tv_item_contactName);
                TextView tv_phone = view.findViewById(R.id.tv_item_contactPhone);
                Pocket pocket = new Pocket(tv_name, tv_phone);
                // 设置进去下次就不用find直接可以拿出来用
                view.setTag(pocket);
            } else {
                // 如果有缓存则直接使用缓存
                view = convertView;
            }
            // 优化，将找好的组件拿出来，直接设置效果，
            Pocket pocket = (Pocket) view.getTag();
            pocket.tv_name.setText(contactList.get(position).get("name"));
            pocket.tv_phone.setText(contactList.get(position).get("phone"));
            // 返回View 显示到Listview
            return view;
        }
    }

    /**
     * 专门来装item组件，下次就不用再find节约资源
     */
    private class Pocket {
        TextView tv_name;
        TextView tv_phone;

        Pocket(TextView tv_name, TextView tv_phone) {
            this.tv_name = tv_name;
            this.tv_phone = tv_phone;
        }
    }
}

package com.coderjj.phonedefend.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.coderjj.phonedefend.R;
import com.coderjj.phonedefend.engine.CommonnumDao;

import java.security.acl.Group;
import java.util.List;

public class CommonNumberQueryActivity extends AppCompatActivity {

    private ExpandableListView elv_common_number;
    private List<CommonnumDao.Group> mGroupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_number_query);
        initUI();
        initData();
    }

    private void initData() {
        CommonnumDao commonnumDao = new CommonnumDao();
        mGroupList = commonnumDao.getGroup();
        final MyAdapter myAdapter = new MyAdapter();
        elv_common_number.setAdapter(myAdapter);
        elv_common_number.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {
                startCall(myAdapter.getChild(groupPosition,childPosition).number);
                return false;
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void startCall(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+number));
        startActivity(intent);
    }

    private void initUI() {
        elv_common_number = findViewById(R.id.elv_common_number);
    }

    class MyAdapter extends BaseExpandableListAdapter{

        @Override
        public int getGroupCount() {
            return mGroupList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mGroupList.get(groupPosition).childList.size();
        }

        @Override
        public CommonnumDao.Group getGroup(int groupPosition) {
            return mGroupList.get(groupPosition);
        }

        @Override
        public CommonnumDao.Child getChild(int groupPosition, int childPosition) {

            mGroupList.size();
            mGroupList.get(groupPosition).childList.size();
            return mGroupList.get(groupPosition).childList.get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            TextView textView = new TextView(getApplicationContext());
            textView.setText("       "+getGroup(groupPosition).name);
            textView.setTextColor(Color.RED);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
            return textView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View view = View.inflate(CommonNumberQueryActivity.this, R.layout.elv_child_item, null);
            TextView tv_name = view.findViewById(R.id.tv_name);
            TextView tv_number = view.findViewById(R.id.tv_number);
            tv_name.setText(getChild(groupPosition,childPosition).name);
            tv_number.setText(getChild(groupPosition,childPosition).number);
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}

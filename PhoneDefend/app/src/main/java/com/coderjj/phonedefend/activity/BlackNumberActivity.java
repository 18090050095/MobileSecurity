package com.coderjj.phonedefend.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.coderjj.phonedefend.R;
import com.coderjj.phonedefend.db.dao.BlackNumberDao;
import com.coderjj.phonedefend.db.domain.BlackNumberInfo;

import java.util.ArrayList;
import java.util.Random;

/**
 * listView优化：
 * 1.复用convertView
 * 2.使用viewHolder复用findViewById
 * 3.将ViewHolder定义成静态，不会去创建多个对象
 * 4.listView分页算法，如每次加载20条，逆序返回
 */
public class BlackNumberActivity extends Activity {

    private Button mBtAddBn;
    private ListView mListView;
    private ArrayList<BlackNumberInfo> mNumberInfos;
    private MyAdapter mMyAdapter;
    private int mode = 1;
    private boolean mIsLoad = false;//防止重复加载

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mMyAdapter == null) {
                mMyAdapter = new MyAdapter();
                mListView.setAdapter(mMyAdapter);
            }else {
                mMyAdapter.notifyDataSetChanged();
            }
            super.handleMessage(msg);
        }
    };
    private int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_number);

//        testCase();
        initView();
        initData();
    }

    private void testCase() {
        for (int i = 0; i < 100; i++) {
            if (i < 10) {
                BlackNumberDao.getInstance(getApplicationContext())
                        .insert("1860000000" + i, 1 + new Random().nextInt(3) + "");
            } else {
                BlackNumberDao.getInstance(getApplicationContext())
                        .insert("186000000" + i, 1 + new Random().nextInt(3) + "");
            }
        }
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mNumberInfos = BlackNumberDao.getInstance(getApplicationContext()).find(0);
                mCount = BlackNumberDao.getInstance(getApplicationContext()).getCount();
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void initView() {
        mBtAddBn = findViewById(R.id.bt_add_bn);
        mListView = findViewById(R.id.lv_black_num);

        mBtAddBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        //监听滚动状态
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (mNumberInfos != null) {
                    //条件1:滚动到停止;
                    // 条件2:最后一个条目可见
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                            && mListView.getLastVisiblePosition() >= mNumberInfos.size() - 1
                            && !mIsLoad) {
                        //mIsLoad防止重复加载的变量
                        //如果当前正在加载mIsLoad就会变成true，本次加载完毕后，再将mIsLoad改为false，
                        if (mCount > mNumberInfos.size()) {
                            //加载下一页数据
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayList<BlackNumberInfo> moreData = BlackNumberDao.getInstance(getApplicationContext()).find(mNumberInfos.size());
                                    mNumberInfos.addAll(moreData);
                                    mHandler.sendEmptyMessage(0);
                                }
                            }).start();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(getApplicationContext(), R.layout.dialog_add_blacknumber, null);
        dialog.setView(view, 0, 0, 0, 0);

        final EditText et_phone = view.findViewById(R.id.et_phone);
        RadioGroup rg_group = view.findViewById(R.id.rg_group);
        Button bt_sumbmit = view.findViewById(R.id.bt_submit);
        Button bt_cancel = view.findViewById(R.id.bt_cancel);

        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_sms:
                        mode = 1;
                        break;
                    case R.id.rb_phone:
                        mode = 2;
                        break;
                    case R.id.rb_all:
                        mode = 3;
                        break;
                }
            }
        });

        bt_sumbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    BlackNumberDao.getInstance(getApplicationContext()).insert(phone, mode + "");

                    //更新显示页面
                    BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
                    blackNumberInfo.setPhone(phone);
                    blackNumberInfo.setMode(mode + "");
                    mNumberInfos.add(0, blackNumberInfo);

                    mMyAdapter.notifyDataSetChanged();
                } else {

                }
                dialog.dismiss();
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mNumberInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return mNumberInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.listview_blacknumber_item, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_phone = convertView.findViewById(R.id.tv_phone);
                viewHolder.tv_mode = convertView.findViewById(R.id.tv_mode);
                viewHolder.iv_delete = convertView.findViewById(R.id.iv_delete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_phone.setText(mNumberInfos.get(position).getPhone());
            int mode = Integer.parseInt(mNumberInfos.get(position).getMode());
            switch (mode) {
                case 1:
                    viewHolder.tv_mode.setText("拦截短信");
                    break;
                case 2:
                    viewHolder.tv_mode.setText("拦截电话");
                    break;
                case 3:
                    viewHolder.tv_mode.setText("拦截全部");
                    break;
                default:
                    break;
            }

            viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BlackNumberDao.getInstance(getApplicationContext()).delete(mNumberInfos.get(position).getPhone());
                    mNumberInfos.remove(position);
                    mMyAdapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tv_phone;
        public TextView tv_mode;
        public ImageView iv_delete;
    }
}

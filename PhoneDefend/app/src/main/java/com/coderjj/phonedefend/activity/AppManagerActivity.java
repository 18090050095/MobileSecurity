package com.coderjj.phonedefend.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.coderjj.phonedefend.R;
import com.coderjj.phonedefend.bean.AppInfoBean;
import com.coderjj.phonedefend.engine.AppInfoProvider;

import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends AppCompatActivity {

    private ListView lv_app_list;
    private List<AppInfoBean> mAppInfoList;
    private MyAdapter mMyAdapter;
    private List<AppInfoBean> mSystemList;
    private List<AppInfoBean> mCustomerList;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mMyAdapter = new MyAdapter();
            lv_app_list.setAdapter(mMyAdapter);
        }
    };
    private TextView tv_title_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);

        initTitle();
        initListView();
    }

    private void initListView() {
        tv_title_type = findViewById(R.id.tv_title_type);
        lv_app_list = findViewById(R.id.lv_app_list);
        new Thread() {
            @Override
            public void run() {
                mAppInfoList = AppInfoProvider.getAppInfoList(AppManagerActivity.this);
                mSystemList = new ArrayList<>();
                mCustomerList = new ArrayList<>();

                for (AppInfoBean appinfo : mAppInfoList) {
                    if (appinfo.isSystem) {
                        mSystemList.add(appinfo);
                    } else {
                        mCustomerList.add(appinfo);
                    }
                }

                mHandler.sendEmptyMessage(0);
            }
        }.start();

        lv_app_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        lv_app_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mSystemList != null && mCustomerList != null) {
                    if (firstVisibleItem >= mCustomerList.size() + 1) {
                        tv_title_type.setText("系统应用" + "(" + mSystemList.size() + ")");
                    } else {
                        tv_title_type.setText("用户应用" + "(" + mCustomerList.size() + ")");
                    }
                }

            }
        });
    }

    private void initTitle() {
        TextView tv_memory = findViewById(R.id.tv_memory);
        TextView tv_sd_memory = findViewById(R.id.tv_sd_memory);
        //1.获取磁盘(内存)可用大小
        String path = Environment.getDataDirectory().getAbsolutePath();
        //2.获取SD卡可用大小
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        //3.获取以上两个路径下文件夹的可用大小
        long space = getAvailSpace(path);
        long sdSpace = getAvailSpace(sdPath);
        //对bytes为单位的数值格式化
        String strSpace = Formatter.formatFileSize(this, space);
        String strSdSpace = Formatter.formatFileSize(this, sdSpace);
        tv_memory.setText("磁盘可用:" + strSpace);
        tv_sd_memory.setText("SD卡可用:" + strSdSpace);

    }

    private long getAvailSpace(String path) {
        //获取可用磁盘大小类
        StatFs statFs = new StatFs(path);
        //获取可用区块的个数
        long count = statFs.getAvailableBlocks();
        //获取区块的大小
        long size = statFs.getBlockSize();
        //区块的大小*可用区块个数=可用空间大小
        return count * size;
    }

    class MyAdapter extends BaseAdapter {
        //在listView中多添加一种类型条目
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == mCustomerList.size() + 1) {
                //纯文本
                return 0;
            } else {
                //图文
                return 1;
            }
//            return super.getItemViewType(position);
        }

        @Override
        public int getCount() {
            return mCustomerList.size() + mSystemList.size() + 2;
        }

        @Override
        public AppInfoBean getItem(int position) {
//            return mAppInfoList.get(position);
            if (position == 0 || position == mCustomerList.size() + 1) {
                //纯文本手动维护
                return null;
            } else {
                //图文条目
                if (position < mCustomerList.size() + 1) {
                    return mCustomerList.get(position - 1);
                } else {
                    return mSystemList.get(position - mCustomerList.size() - 2);
                }
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //判断当前索引指向的条目状态码
            int itemViewType = getItemViewType(position);
            if (itemViewType == 0) {
                //纯文本
                ViewTitleHolder titleHolder;
                if (convertView == null) {
                    titleHolder = new ViewTitleHolder();
                    convertView = View.inflate(AppManagerActivity.this, R.layout.list_app_title_item, null);
                    titleHolder.title = convertView.findViewById(R.id.tv_title);
                    convertView.setTag(titleHolder);
                } else {
                    titleHolder = (ViewTitleHolder) convertView.getTag();
                }

                if (position == 0) {
                    titleHolder.title.setText("用户应用");
                } else {
                    titleHolder.title.setText("系统应用");
                }
                return convertView;
            } else {
                //图文
                ViewHolder holder;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = View.inflate(AppManagerActivity.this, R.layout.list_app_item, null);
                    holder.icon = convertView.findViewById(R.id.iv_icon);
                    holder.appName = convertView.findViewById(R.id.tv_app_name);
                    holder.appType = convertView.findViewById(R.id.tv_app_location);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.icon.setBackgroundDrawable(getItem(position).icon);
                holder.appName.setText(getItem(position).name);
                if (getItem(position).isSdcard) {
                    holder.appType.setText("SD卡应用");
                } else {
                    holder.appType.setText("内存应用");
                }
                return convertView;
            }
        }
    }

    static class ViewHolder {
        public ImageView icon;
        public TextView appName;
        public TextView appType;
    }

    static class ViewTitleHolder {
        public TextView title;
    }
}

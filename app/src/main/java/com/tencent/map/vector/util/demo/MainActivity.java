package com.tencent.map.vector.util.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final int PERMISSIONS_REQUEST_CODE = 0;

    ListView lvDemoList;
    private List<DemoInfo> mDemos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDemoInfo();

        lvDemoList = findViewById(R.id.lv_demos);

        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(
                        new String[permissions.size()]), PERMISSIONS_REQUEST_CODE);
            }
        }
        DemoListAdapter adapter = new DemoListAdapter();
        lvDemoList.setAdapter(adapter);
        lvDemoList.setOnItemClickListener(this);
    }

    private void getDemoInfo() {
        mDemos = new ArrayList<>(16);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_ACTIVITIES | PackageManager.GET_META_DATA);
            for (int i = 0; i < packageInfo.activities.length; i++) {
                ActivityInfo activityInfo = packageInfo.activities[i];
                if (activityInfo.metaData != null) {
                    mDemos.add(new DemoInfo(
                            activityInfo.name,
                            getString(activityInfo.labelRes),
                            activityInfo.metaData.getString("description")));
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;
        try {
            intent = new Intent(this, Class.forName(mDemos.get(position).name));
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    class DemoListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDemos.size();
        }

        @Override
        public Object getItem(int position) {
            return mDemos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this,
                        R.layout.demo_list_item, null);
                holder = new ViewHolder();
                holder.tvLable = (TextView) convertView.findViewById(R.id.label);
                holder.tvDesc = (TextView) convertView.findViewById(R.id.desc);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvLable.setText(mDemos.get(position).getLabel());
            holder.tvDesc.setText(mDemos.get(position).getDescription());

            return convertView;
        }


        class ViewHolder {
            TextView tvLable;
            TextView tvDesc;
        }
    }

    public class DemoInfo {
        private String name;
        private String label;
        private String description;

        public DemoInfo(String name, String label, String description) {

            this.name = name;
            this.label = label;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getLabel() {
            return label;
        }

        public String getDescription() {
            return description;
        }
    }
}

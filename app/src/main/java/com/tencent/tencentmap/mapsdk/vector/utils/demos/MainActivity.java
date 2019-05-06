package com.tencent.tencentmap.mapsdk.vector.utils.demos;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tencent.tencentmap.mapsdk.vector.utils.demos.animation.MarkerTranslateActivity;

public class MainActivity extends ListActivity {

    private final DemoInfo[] demos = {
            new DemoInfo(R.string.demo_label_clustering,
                    R.string.demo_desc_clustering, ClusteringActivity.class),
            new DemoInfo(R.string.demo_label_large_number_clustering,
                    R.string.demo_desc_large_number_clustering, LargeNumberClusteringActivity.class),
            new DemoInfo(R.string.demo_label_custom_clustering,
                    R.string.demo_desc_custom_clustering, CustomClusteringActivity.class),
            new DemoInfo(R.string.demo_static_bmp_projection_label,R.string.demo_static_bmp_projection_desc,StaticBmpProjectionActivity.class),
            new DemoInfo(R.string.demo_label_marker_translation,
                    R.string.demo_desc_marker_translation, MarkerTranslateActivity.class)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DemoListAdapter adapter = new DemoListAdapter();
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(this, demos[position].demoActivityClass);
        startActivity(intent);
    }

    class DemoListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return demos.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return demos[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this,
                        R.layout.demo_list_item, null);
                holder = new ViewHolder();
                holder.tvLable = (TextView)convertView.findViewById(R.id.label);
                holder.tvDesc = (TextView)convertView.findViewById(R.id.desc);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.tvLable.setText(demos[position].lable);
            holder.tvDesc.setText(demos[position].desc);

            return convertView;
        }


        class ViewHolder {
            TextView tvLable;
            TextView tvDesc;
        }
    }

    class DemoInfo {
        private final int lable;
        private final int desc;
        private final Class<? extends Activity> demoActivityClass;

        public DemoInfo(int lable, int desc, Class<? extends Activity> demoActivityClass) {
            // TODO Auto-generated constructor stub
            this.lable = lable;
            this.desc = desc;
            this.demoActivityClass = demoActivityClass;
        };
    }
}

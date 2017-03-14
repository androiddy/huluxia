package com.hlxyjqd.yjqd.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hlxyjqd.yjqd.Bean.UserAllInfo;
import com.hlxyjqd.yjqd.R;
import com.hlxyjqd.yjqd.View.Impl.MainActivity;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by zhangzhongping on 17/1/1.
 */

public class RunRankAdapter extends BaseAdapter {
    ArrayList<UserAllInfo> ls;
    MainActivity mContext;
    View view;
    private LayoutInflater v;

    public RunRankAdapter(MainActivity context,
                          ArrayList<UserAllInfo> list) {
        this.v = LayoutInflater.from(context);
        ls = list;
        mContext = new WeakReference<MainActivity>(context).get();
    }

    public ArrayList getArrayList() {
        return ls;
    }

    @Override
    public int getCount() {
        return ls.size();
    }

    @Override
    public Object getItem(int position) {
        return ls.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UserAllInfo appUnit = ls.get(position);
        RunRankAdapter.ViewHolder1 holder = null;
        view = convertView;
        if (view == null) {
            holder = new RunRankAdapter.ViewHolder1();
            view = v.inflate(R.layout.itme_, null);
            holder.time1 = (ImageView) view.findViewById(R.id.imageView);
            holder.time = (TextView) view.findViewById(R.id.textView);
            holder.name = (TextView) view.findViewById(R.id.textView2);
            view.setTag(holder);
        } else {
            holder = (RunRankAdapter.ViewHolder1) view.getTag();
        }
        Picasso.with(mContext).load(appUnit.getIcon()).into(holder.time1);
        if (appUnit.getSignintext() == null) {
            appUnit.setSignintext("初始化中");
            mContext.mPresenter.LoadState(appUnit.getSignincheckurl(), appUnit, holder);
        }
        holder.name.setText("状态：" + appUnit.getSignintext());
        holder.time.setText(appUnit.getTitle());
        return view;
    }

    public class ViewHolder1 {
        public TextView time;
        public TextView name;
        public ImageView time1;

    }
}

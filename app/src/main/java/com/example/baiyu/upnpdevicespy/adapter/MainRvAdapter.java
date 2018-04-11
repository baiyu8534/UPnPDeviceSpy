package com.example.baiyu.upnpdevicespy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baiyu.upnpdevicespy.R;
import com.example.baiyu.upnpdevicespy.activity.DeviceDetailActivity;
import com.example.baiyu.upnpdevicespy.bean.DeviceSimpleDetailBean;

import java.util.List;

/**
 * 文件名：
 * 描述：
 * 作者：白煜
 * 时间：2018/4/9 0009
 * 版权：
 */

public class MainRvAdapter extends RecyclerView.Adapter<MainRvAdapter.ViewHolder> {

    private Context mContext;
    private List<DeviceSimpleDetailBean> mList;

    public MainRvAdapter(Context context, List<DeviceSimpleDetailBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_main_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_device_name.setText(mList.get(position).getName());
        holder.tv_device_ip.setText(mList.get(position).getDeviceType() + ":" + mList.get(position).getIp());
        holder.tv_device_urn.setText(mList.get(position).getURN());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DeviceDetailActivity.class);
                intent.putExtra("device uuid", mList.get(position).getUuid());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_device_name;
        public TextView tv_device_ip;
        public TextView tv_device_urn;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_device_name = itemView.findViewById(R.id.tv_device_name);
            tv_device_ip = itemView.findViewById(R.id.tv_device_ip);
            tv_device_urn = itemView.findViewById(R.id.tv_device_urn);
        }
    }
}

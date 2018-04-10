package com.example.baiyu.upnpdevicespy;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baiyu.upnpdevicespy.adapter.MainRvAdapter;
import com.example.baiyu.upnpdevicespy.bean.DeviceRvItemBean;
import com.example.baiyu.upnpdevicespy.upnp_core.UpnpManager;
import com.example.baiyu.upnpdevicespy.upnp_core.UpnpService;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private List<DeviceRvItemBean> mList = new ArrayList<>();
    private MainRvAdapter mAdapter;
    private TextView tv_refresh;
    private MyHandler mHandler;

    private static final int DEVICE_ADDED = 1001;
    private static final int DEVICE_REMOVED = 1002;

    private static class MyHandler extends Handler {
        private WeakReference<MainActivity> activityWeakReference;

        public MyHandler(MainActivity activity) {
            activityWeakReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = activityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case DEVICE_ADDED:
                        activity.notifyDeviceAdded((Device) msg.obj);
                        break;
                    case DEVICE_REMOVED:
                        activity.notifyDeviceRemove((Device) msg.obj);
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new MyHandler(this);
        initView();
        bindService(new Intent(this, UpnpService.class), conn, Context.BIND_AUTO_CREATE);
    }

    private void initView() {
        rv_device = (RecyclerView) findViewById(R.id.rv_device);
        tv_refresh = (TextView) findViewById(R.id.tv_refresh);
        tv_refresh.setOnClickListener(this);
        rv_device.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MainRvAdapter(this, mList);
        rv_device.setAdapter(mAdapter);
    }

    private RecyclerView rv_device;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UpnpService.LocalBinder binder = (UpnpService.LocalBinder) service;
            UpnpManager.getInstance().setUpnpService(binder.getService());
            UpnpManager.getInstance().getRegistry().addListener(mRegistryListener);
            UpnpManager.getInstance().searchAllDevices();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    private RegistryListener mRegistryListener = new RegistryListener() {
        @Override
        public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice remoteDevice) {
        }

        @Override
        public void remoteDeviceDiscoveryFailed(Registry registry, RemoteDevice remoteDevice, Exception e) {
        }

        @Override
        public void remoteDeviceAdded(Registry registry, RemoteDevice remoteDevice) {
            Message message = new Message();
            message.what = DEVICE_ADDED;
            message.obj = remoteDevice;
            mHandler.sendMessage(message);
        }

        @Override
        public void remoteDeviceUpdated(Registry registry, RemoteDevice remoteDevice) {
        }

        @Override
        public void remoteDeviceRemoved(Registry registry, RemoteDevice remoteDevice) {
            Message message = new Message();
            message.what = DEVICE_REMOVED;
            message.obj = remoteDevice;
            mHandler.sendMessage(message);
        }

        @Override
        public void localDeviceAdded(Registry registry, LocalDevice localDevice) {
            Message message = new Message();
            message.what = DEVICE_ADDED;
            message.obj = localDevice;
            mHandler.sendMessage(message);
        }

        @Override
        public void localDeviceRemoved(Registry registry, LocalDevice localDevice) {
            Message message = new Message();
            message.what = DEVICE_REMOVED;
            message.obj = localDevice;
            mHandler.sendMessage(message);
        }

        @Override
        public void beforeShutdown(Registry registry) {
        }

        @Override
        public void afterShutdown() {
            System.out.println("Shutdown of registry complete!");
        }
    };

    private void notifyDeviceAdded(Device device) {
        DeviceRvItemBean bean = new DeviceRvItemBean();
        bean.setName(device.getDetails().getFriendlyName() + "(" + device.getDisplayString() + ")");
        // TODO: 2018/4/9 0009 ip 应该用Descriptor:
        // http://10.0.2.12:53020/upnp/dev/0c6e14de-4509-35e0-9e85-2728293218c7/desc, 判断是否是远程设备也有其他的判断
        bean.setIp(device.getDetails().getBaseURL() == null ? "" : device.getDetails().getBaseURL().getHost());
        bean.setURN(device.getType().getNamespace() + ":" + device.getType().getType() + ":" + device
                .getType().getVersion());
        bean.setUuid(device.getIdentity().getUdn().toString());
        bean.setDeviceType(device.getClass().getSimpleName());

        mList.add(bean);
        mAdapter.notifyDataSetChanged();
    }

    private void notifyDeviceRemove(Device device) {
        String uuid = device.getIdentity().getUdn().toString();
        for (DeviceRvItemBean bean : mList) {
            if (bean.getUuid().equals(uuid)) {
                mList.remove(bean);
            }
        }
        mAdapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "(" + device.getClass().getSimpleName() + ")" + device.getDetails()
                .getFriendlyName() + "已经移除", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_refresh:
                mList.clear();
                mAdapter.notifyDataSetChanged();
                UpnpManager.getInstance().getRegistry().removeAllLocalDevices();
                UpnpManager.getInstance().getRegistry().removeAllRemoteDevices();
                UpnpManager.getInstance().searchAllDevices();
                break;
        }
    }
}

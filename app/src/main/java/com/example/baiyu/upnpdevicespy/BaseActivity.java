package com.example.baiyu.upnpdevicespy;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.baiyu.upnpdevicespy.bean.DeviceSimpleDetailBean;
import com.example.baiyu.upnpdevicespy.upnp_core.UpnpManager;

import java.lang.ref.WeakReference;

/**
 * 文件名：
 * 描述：
 * 作者：白煜
 * 时间：2018/4/10 0010
 * 版权：
 */
// TODO: 2018/4/10 0010 把网络状态监听封装一下，到时候可以方便使用
public class BaseActivity extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();
    public static BaseActivity activity;
    protected Context mContext;
    protected BaseHandler baseHandler;

    public static final int WHAT_DEVICE_REMOVED = 2001;

    public static class BaseHandler extends Handler {
        private WeakReference<BaseActivity> activityWeakReference;

        public BaseHandler(BaseActivity activity) {
            activityWeakReference = new WeakReference<BaseActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseActivity activity = activityWeakReference.get();
            if (activity != null) {
                switch (msg.what){
                    case WHAT_DEVICE_REMOVED:
                        activity.deviceRemoved((DeviceSimpleDetailBean) msg.obj);
                        break;
                }
            }
        }
    }




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        activity = this;
        mContext = this;
        //设置底部导航栏为透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        baseHandler = new BaseHandler(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        MyApplication.getInstance().setCurrentActivity(activity);
    }

    @Override
    protected void onPause() {
        super.onPause();
        activity = null;
        MyApplication.getInstance().setCurrentActivity(activity);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    //可以在执行一些需要设备在线的操作时，去判断设备是否在线
    protected boolean chickDeviceIsOnline(String uuid){
        return UpnpManager.getInstance().checkDeviceIsOnline(uuid);
    }


//    如果需要通知用户 这个页面正在查看或操作的device已经离线 就复写这个方法，用离线的uuid去对比这个页面的device的UUID，一样即以离线
    protected void deviceRemoved(DeviceSimpleDetailBean deviceSimpleDetail){

    }
}

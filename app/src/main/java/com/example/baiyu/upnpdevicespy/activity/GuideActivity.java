package com.example.baiyu.upnpdevicespy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.example.baiyu.upnpdevicespy.BaseActivity;
import com.example.baiyu.upnpdevicespy.MainActivity;
import com.example.baiyu.upnpdevicespy.R;

import java.lang.ref.WeakReference;

/**
 * 文件名：GuideActivity
 * 描述：GuideActivity
 * 作者：白煜
 * 时间：2018/4/10 0010
 * 版权：
 */

public class GuideActivity extends BaseActivity {

    private static class MyHandler extends Handler {
        private WeakReference<GuideActivity> activityWeakReference;

        public MyHandler(GuideActivity activity) {
            activityWeakReference = new WeakReference<GuideActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            GuideActivity activity = activityWeakReference.get();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        MyHandler myHandler = new MyHandler(this);

        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                GuideActivity.this.finish();
            }
        }, 3000);
    }


}

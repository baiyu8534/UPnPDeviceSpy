package com.example.baiyu.upnpdevicespy.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.baiyu.upnpdevicespy.BaseActivity;
import com.example.baiyu.upnpdevicespy.R;
import com.example.baiyu.upnpdevicespy.bean.DeviceSimpleDetailBean;
import com.example.baiyu.upnpdevicespy.upnp_core.UpnpManager;
import com.example.baiyu.upnpdevicespy.utils.SnackbarUtil;

import org.fourthline.cling.model.meta.Action;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDN;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件名：
 * 描述：
 * 作者：白煜
 * 时间：2018/4/11 0011
 * 版权：
 */

public class DeviceDetailActivity extends BaseActivity {


    @BindView(R.id.tv_base_url)
    TextView mTvBaseUrl;
    @BindView(R.id.tv_device_icon)
    TextView mTvDeviceIcon;
    @BindView(R.id.tv_device_urn)
    TextView mTvDeviceUrn;
    @BindView(R.id.tv_timeout)
    TextView mTvTimeout;
    @BindView(R.id.tv_friendly_name)
    TextView mTvFriendlyName;
    @BindView(R.id.tv_interface_host)
    TextView mTvInterfaceHost;
    @BindView(R.id.tv_service_count)
    TextView mTvServiceCount;
    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.tv_udn)
    TextView mTvUdn;
    @BindView(R.id.ll_root)
    LinearLayout mLlRoot;
    @BindView(R.id.ll_services)
    LinearLayout mLlServices;
    private String uuid;

    private boolean deviceIsOnline = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        ButterKnife.bind(this);
        uuid = getIntent().getStringExtra("device uuid");
        deviceIsOnline = chickDeviceIsOnline(uuid);

        if (deviceIsOnline) {
            Device device = UpnpManager.getInstance().getRegistry().getDevice(new UDN(uuid), true);
            if (device != null) {
                showdata(device);
            }
        }

    }

    private void showdata(Device device) {
        mTvBaseUrl.setText(device.getDetails().getBaseURL() == null ? "" : device.getDetails().getBaseURL().toString());
        mTvDeviceIcon.setText(Arrays.asList(device.getIcons()).toString());
        mTvDeviceUrn.setText(device.getType().getNamespace() + ":" + device.getType().getType() + ":" + device
                .getType().getVersion());
        mTvTimeout.setText(device.getIdentity().getMaxAgeSeconds() + "");
        mTvFriendlyName.setText(device.getDetails().getFriendlyName());
        mTvInterfaceHost.setText(device.getDetails().getBaseURL() == null ? "" : (device.getDetails().getBaseURL().getHost() +
                ":" + device.getDetails().getBaseURL().getPort()));

        mTvServiceCount.setText(device.getServices().length + "");
        mTvVersion.setText(device.getVersion().getMajor() + "");
        mTvUdn.setText(device.getIdentity().getUdn().getIdentifierString());

        for (Service service : Arrays.asList(device.getServices())) {
            View serviceView = LayoutInflater.from(mContext).inflate(R.layout.item_service, null);
            TextView tv_service_name = serviceView.findViewById(R.id.tv_service_name);
            final LinearLayout ll_hid = serviceView.findViewById(R.id.ll_hid);
            final ImageView iv_show_or_hid = serviceView.findViewById(R.id.iv_show_or_hid);

            //在测量高度之前去添加
            for (Action action : Arrays.asList(service.getActions())) {
                View actionView = LayoutInflater.from(mContext).inflate(R.layout.item_action, null);
                TextView tv_action_name = actionView.findViewById(R.id.tv_action_name);
                tv_action_name.setText(action.getName());
                ll_hid.addView(actionView);
            }

            final int hidLinearLayoutHeight;
            ll_hid.measure(0, 0);
            hidLinearLayoutHeight = ll_hid.getMeasuredHeight();
            System.out.println("hidLinearLayoutHeight:" + hidLinearLayoutHeight);

            iv_show_or_hid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ll_hid.getVisibility() == View.GONE) {

                        //打开
                        animateOpen(ll_hid, hidLinearLayoutHeight);
                        animationIvOpen(iv_show_or_hid);
                    } else {
                        //关闭
                        animateClose(ll_hid);
                        animationIvClose(iv_show_or_hid);
                    }
//                notifyDataSetChanged();
                }
            });

            tv_service_name.setText(service.getServiceId().getId());
            mLlServices.addView(serviceView);
        }

    }

    @Override
    protected void deviceRemoved(DeviceSimpleDetailBean deviceSimpleDetail) {
        if (this.uuid.equals(deviceSimpleDetail.getUuid())) {
            SnackbarUtil.snackShort(mLlRoot, "设备：" + deviceSimpleDetail.getName() + "已离线");
            deviceIsOnline = false;
        }
    }

    private void animateOpen(View v, int hidLinearLayoutHeight) {
        v.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(v, 0,
                hidLinearLayoutHeight);
        animator.start();

    }

    private void animationIvOpen(ImageView mIv) {
        RotateAnimation animation = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setFillAfter(true);
        animation.setDuration(100);
        mIv.startAnimation(animation);
    }

    private void animationIvClose(ImageView mIv) {
        mIv.setImageResource(R.mipmap.btn_expand);
        RotateAnimation animation = new RotateAnimation(180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setFillAfter(true);
        animation.setDuration(100);
        mIv.startAnimation(animation);
    }

    private void animateClose(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

        });
        animator.start();
    }

    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);

            }
        });
        return animator;
    }
}

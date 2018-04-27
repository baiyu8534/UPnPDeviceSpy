package com.example.baiyu.upnpdevicespy.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.baiyu.upnpdevicespy.BaseActivity;
import com.example.baiyu.upnpdevicespy.R;
import com.example.baiyu.upnpdevicespy.bean.DeviceSimpleDetailBean;
import com.example.baiyu.upnpdevicespy.upnp_core.UpnpManager;
import com.example.baiyu.upnpdevicespy.upnp_core.UpnpOrderManager;
import com.example.baiyu.upnpdevicespy.utils.SnackbarUtil;

import org.fourthline.cling.model.action.ActionArgumentValue;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Action;
import org.fourthline.cling.model.meta.ActionArgument;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private void showdata(final Device device) {
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
            final LinearLayout ll_show_or_hid = serviceView.findViewById(R.id.ll_show_or_hid);
            final ImageView iv_show_or_hid = serviceView.findViewById(R.id.iv_show_or_hid);

            //在测量高度之前去添加
            for (final Action action : Arrays.asList(service.getActions())) {
                View actionView = LayoutInflater.from(mContext).inflate(R.layout.item_action, null);
                TextView tv_action_name = actionView.findViewById(R.id.tv_action_name);
                tv_action_name.setText(action.getName());
                ll_hid.addView(actionView);
                actionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        View action_dialog = LayoutInflater.from(mContext).inflate(R.layout.action_dialog, null);
                        LinearLayout ll_content = action_dialog.findViewById(R.id.ll_content);
                        final TextView tv_result = action_dialog.findViewById(R.id.tv_result);


                        List<ActionArgument> actionArguments = Arrays.asList(action.getArguments());
                        final List<EditText> arg_in_edittexts = new ArrayList<>();
                        final List<TextView> arg_out_textviews = new ArrayList<>();
                        for (ActionArgument actionArgument : actionArguments) {
                            System.out.println(actionArgument.getAliases().toString());
                            System.out.println(actionArgument.getDatatype().getDisplayString());
                            System.out.println(actionArgument.getRelatedStateVariableName());
                            System.out.println(actionArgument.getName());
                            System.out.println(actionArgument.getDatatype().getDisplayString());
                            System.out.println(actionArgument.getDirection().name());

                            if ("OUT".equals(actionArgument.getDirection().name())) {
                                //是get的方法 获取信息 有返回值的方法

                                View item_action_arguments = LayoutInflater.from(mContext).inflate(R.layout.item_action_arguments_out, null);
                                TextView tv_arg_name = item_action_arguments.findViewById(R.id.tv_arg_name);
                                TextView et_arg_out = item_action_arguments.findViewById(R.id.et_arg_out);
                                tv_arg_name.setText(actionArgument.getName());
                                et_arg_out.setTag(actionArgument.getName());
                                arg_out_textviews.add(et_arg_out);
                                ll_content.addView(item_action_arguments);
                            } else if ("IN".equals(actionArgument.getDirection().name())) {
                                //是set方法 设置方法 传入参数的方法

                                View item_action_arguments = LayoutInflater.from(mContext).inflate(R.layout.item_action_arguments_in, null);
                                TextView tv_arg_name = item_action_arguments.findViewById(R.id.tv_arg_name);
                                EditText et_arg_in = item_action_arguments.findViewById(R.id.et_arg_in);
                                tv_arg_name.setText(actionArgument.getName());
                                arg_in_edittexts.add(et_arg_in);
                                ll_content.addView(item_action_arguments);
                            }
                        }

                        AlertDialog dialog = new AlertDialog.Builder(DeviceDetailActivity.this).setTitle("action:" + action.getName())
                                .setView(action_dialog)
                                .setPositiveButton("发送", null)
                                .setNegativeButton("取消", null)
                                .create();
                        dialog.show();

                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                List<Object> args = new ArrayList<>();
                                for (EditText arg_edittext : arg_in_edittexts) {
                                    args.add(arg_edittext.getText().toString());
                                }
                                UpnpOrderManager.sendOrder(
                                        UpnpManager.getInstance().getControlPoint()
                                        , action, args,
                                        new UpnpOrderManager.ResultCallBacks() {
                                            @Override
                                            public void success(ActionInvocation invocation, String uid) {

                                                for (ActionArgumentValue argumentValue : invocation.getOutput()) {
                                                    System.out.println("argumentValue.getValue()::" + argumentValue.getValue());
                                                    System.out.println("argumentValue.getArgument().getName()::" + argumentValue.getArgument().getName());
                                                }

                                                final ActionArgumentValue[] argumentValues = invocation.getOutput();
                                                // 必须放到ui线程去，要不更新不了ui
                                                DeviceDetailActivity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        for (int i = 0; i < argumentValues.length; i++) {
                                                            for (TextView textView : arg_out_textviews) {
                                                                if ((textView.getTag()).equals(argumentValues[i].getArgument().getName())) {
                                                                    if (argumentValues[i].getValue() != null) {
                                                                        textView.setText(argumentValues[i].getValue().toString());
                                                                    } else {
                                                                        textView.setText("null");
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        tv_result.setText("成功");
                                                    }
                                                });
                                            }

                                            @Override
                                            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                                                tv_result.setText("失败");
                                            }
                                        });
                            }
                        });

                    }
                });
            }

            // 高度
            final int hidLinearLayoutHeight;
            ll_hid.measure(0, 0);
            hidLinearLayoutHeight = ll_hid.getMeasuredHeight();

            ll_show_or_hid.setOnClickListener(new View.OnClickListener() {
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

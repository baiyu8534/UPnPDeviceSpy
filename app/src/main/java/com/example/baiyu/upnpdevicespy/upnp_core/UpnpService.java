package com.example.baiyu.upnpdevicespy.upnp_core;

import android.content.Intent;
import android.os.IBinder;

import org.fourthline.cling.UpnpServiceConfiguration;
import org.fourthline.cling.android.AndroidUpnpServiceConfiguration;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.registry.Registry;

public class UpnpService extends AndroidUpnpServiceImpl {

    private android.os.Binder mBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new LocalBinder();
    }

    public Registry getRegistry() {
        return upnpService.getRegistry();
    }

    public ControlPoint getControlPoint() {
        return upnpService.getControlPoint();
    }

    public class LocalBinder extends Binder {
        public UpnpService getService() {
            return UpnpService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * android缺省配置是每3秒 sleep一次,可以自定义
     *
     * @return
     */
    @Override
    protected UpnpServiceConfiguration createConfiguration() {
        return new AndroidUpnpServiceConfiguration() {
            @Override
            public int getRegistryMaintenanceIntervalMillis() {
                return 7000;
            }

            @Override
            public ServiceType[] getExclusiveServiceTypes() {
                // 默认是发现所有的设备和服务
                return super.getExclusiveServiceTypes();
                // 只发现你想发现的设备
//				return new ServiceType[]{
//						new UDAServiceType("SwitchPower")
//				};

//				完全禁用设备发现
//				If you are not writing a control point but a server application, you can return null in the getExclusiveServiceTypes() method. This will disable discovery completely, now all device and service advertisements are dropped as soon as they are received.
//				return null;
            }
        };
    }
}

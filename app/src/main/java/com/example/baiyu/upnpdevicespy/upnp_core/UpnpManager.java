package com.example.baiyu.upnpdevicespy.upnp_core;


import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;
import org.fourthline.cling.registry.Registry;

import java.util.Collection;


public class UpnpManager {

    private DeviceType light = new UDADeviceType("BinaryLight", 1);
    //urn:www-wistron-com:device:WiClassStation:1

    private static UpnpManager INSTANCE = null;
    //Service
    private UpnpService mUpnpService;

    private UpnpManager() {
    }

    public static UpnpManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UpnpManager();
        }
        return INSTANCE;
    }

    public void setUpnpService(UpnpService upnpService) {
        mUpnpService = upnpService;
    }

    public void searchAllDevices() {
        mUpnpService.getControlPoint().search();
    }

    public Collection<Device> getLight() {
        return mUpnpService.getRegistry().getDevices(light);
    }

    public ControlPoint getControlPoint() {
        return mUpnpService.getControlPoint();
    }

    public Registry getRegistry() {
        return mUpnpService.getRegistry();
    }

    public boolean checkDeviceIsOnline(String uuid) {
        UDN udn = new UDN(uuid);
        Device device = INSTANCE.getRegistry().getDevice(udn,true);
        if (device != null) {
            return true;
        }
        return false;
    }

//    先不存了
//    public static DeviceDetailBean deviceToDeviceDetailBean(Device device){
//        DeviceDetailBean bean = new DeviceDetailBean();
//
//        return bean;
//    }
}

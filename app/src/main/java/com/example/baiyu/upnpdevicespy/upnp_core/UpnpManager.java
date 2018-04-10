package com.example.baiyu.upnpdevicespy.upnp_core;


import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.registry.Registry;

import java.util.Collection;


public class UpnpManager {

	private DeviceType light = new UDADeviceType("BinaryLight",1);
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

}

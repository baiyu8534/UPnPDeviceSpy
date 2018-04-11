package com.example.baiyu.upnpdevicespy.bean;

/**
 * 文件名：
 * 描述：
 * 作者：白煜
 * 时间：2018/4/10 0010
 * 版权：
 */

public class DeviceSimpleDetailBean {
    private String name;
    private String URN;
    private String ip;
    private String uuid;

    /**
     * 远程 或 本地设备
     */
    private String deviceType;

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setURN(String URN) {
        this.URN = URN;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public String getURN() {
        return URN;
    }

    public String getIp() {
        return ip;
    }

    public String getUuid() {
        return uuid;
    }
}

package com.example.baiyu.upnpdevicespy.upnp_core.custom_device;


import org.fourthline.cling.model.types.DeviceType;

/**
 * Created by WH1509016 on 2016/4/5.
 * 获取指定NameSpace的设备schemas-upnp-org，自定义的设备
 */
public class UDADeviceType_wistron extends DeviceType {

    public static final String DEFAULT_NAMESPACE = "www-wistron-com";

    // This pattern also accepts decimal versions, not only integers (as would be required by UDA), but cuts off fractions
//    public static final Pattern PATTERN =
//            Pattern.compile("urn:" + DEFAULT_NAMESPACE + ":device:(" + SyncStateContract.Constants.REGEX_TYPE + "):([0-9]+).*");

    public UDADeviceType_wistron(String type) {
        super(DEFAULT_NAMESPACE, type, 1);
    }

    public UDADeviceType_wistron(String type, int version) {
        super(DEFAULT_NAMESPACE, type, version);
    }

//    public static UDADeviceType valueOf(String s) throws InvalidValueException {
//        Matcher matcher = PATTERN.matcher(s);
//
//        try {
//            if (matcher.matches())
//                return new UDADeviceType(matcher.group(1), Integer.valueOf(matcher.group(2)));
//        } catch(RuntimeException e) {
//            throw new InvalidValueException(String.format(
//                    "Can't parse UDA device type string (namespace/type/version) '%s': %s", s, e.toString()
//            ));
//        }
//        throw new InvalidValueException("Can't parse UDA device type string (namespace/type/version): " + s);
//    }
}

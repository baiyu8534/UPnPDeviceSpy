package com.example.baiyu.upnpdevicespy.upnp_service;


import org.fourthline.cling.binding.annotations.UpnpAction;
import org.fourthline.cling.binding.annotations.UpnpInputArgument;
import org.fourthline.cling.binding.annotations.UpnpOutputArgument;
import org.fourthline.cling.binding.annotations.UpnpService;
import org.fourthline.cling.binding.annotations.UpnpServiceId;
import org.fourthline.cling.binding.annotations.UpnpServiceType;
import org.fourthline.cling.binding.annotations.UpnpStateVariable;

import java.beans.PropertyChangeSupport;

// TODO: 2018/4/29 关于属性的注释问题可以参考 4.1.1. Mapping state variables映射状态参数。

@UpnpService(serviceId = @UpnpServiceId("SwitchPower"), serviceType = @UpnpServiceType(value = "SwitchPower", version = 1))
public class SwitchPower {

    private final PropertyChangeSupport propertyChangeSupport;

    public SwitchPower() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    //sendEvents 为false时 控制端的SubscriptionCallback 接接收不到 这个属性的信息
    @UpnpStateVariable(defaultValue = "0", sendEvents = false)
    private int target = 0; //这些实际属性的类型可以自己定 还是4.1.1

    @UpnpStateVariable(defaultValue = "0")
    private int status = 0;

    @UpnpStateVariable(defaultValue = "0")
    private int test = 2;


    //调用action时会自动根据调用的方法名 去获取具体upnp属性，不存在是运行时就崩了setTarget() 就必须有一个 target的属性
    //你可以不用存在一个实际的类的属性叫target 但是注解要申明有这样一个属性 像下面那样

    @UpnpAction
    public void setTarget(
            @UpnpInputArgument(name = "NewTargetValue") int newTargetValue) {
        int targetOldValue = target;
        target = newTargetValue;
        int statusOldValue = status;
        status = newTargetValue;


        // These have no effect on the UPnP monitoring but it's JavaBean compliant
//		getPropertyChangeSupport().firePropertyChange("target", targetOldValue, target);
//		getPropertyChangeSupport().firePropertyChange("status", statusOldValue, status);


        // This will send a UPnP event, it's the name of a state variable that sends events
        getPropertyChangeSupport().firePropertyChange("status", statusOldValue, status);
        System.out.println("Switch is: " + status);
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "RetTargetValue"))
    public int getTarget() {
        return target;
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "ResultStatus"))
    public int getStatus() {
        return status;
    }


    @UpnpAction(out = {@UpnpOutputArgument(name = "Retest")})
    public int getTest(@UpnpInputArgument(name = "testValue_length_testtttttttttttt") int newTargetValue,
                       @UpnpInputArgument(name = "test2Value") int test2Value) {

        return test * test2Value * newTargetValue;
    }

    // FIXME: 2018/4/28 输入输出的参数的类型到底怎么规定
    // FIXME: 2018/4/28 还有注解上的到底什么意思，方法取名必须和属性名有关吗
    // FIXME: 2018/4/28 返回两个参数的不行，要看下
//    @UpnpAction(out = {@UpnpOutputArgument(name = "Retest"), @UpnpOutputArgument(name = "rrr")})
//    public List<Integer> getTest(@UpnpInputArgument(name = "testValue") int newTargetValue) {
//        List<Integer> list = new ArrayList<>();
//        list.add(test * newTargetValue);
//        list.add(test * newTargetValue * 2);
//        return list;
//    }
}
/**
@UpnpService(
        serviceId = @UpnpServiceId("SwitchPower"),
        serviceType = @UpnpServiceType(value = "SwitchPower", version = 1)


)
@UpnpStateVariables(
        {
                @UpnpStateVariable(
                        name = "Target",
                        defaultValue = "0",
                        sendEvents = false
                ),
                @UpnpStateVariable(
                        name = "Status",
                        defaultValue = "0"
                )
        }
)


public class SwitchPowerAnnotatedClass {


    private boolean power;


    @UpnpAction
    public void setTarget(@UpnpInputArgument(name = "NewTargetValue")
                                  boolean newTargetValue) {
        power = newTargetValue;
        System.out.println("Switch is: " + power);
    }


    @UpnpAction(out = @UpnpOutputArgument(name = "RetTargetValue"))
    public boolean getTarget() {
        return power;
    }


    @UpnpAction(out = @UpnpOutputArgument(name = "ResultStatus"))
    public boolean getStatus() {
        return power;
    }
}*/

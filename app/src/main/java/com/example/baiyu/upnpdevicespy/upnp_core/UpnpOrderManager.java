package com.example.baiyu.upnpdevicespy.upnp_core;

import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.UnsupportedDataException;
import org.fourthline.cling.model.action.ActionArgumentValue;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.gena.RemoteGENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Action;
import org.fourthline.cling.model.meta.ActionArgument;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.state.StateVariableValue;

import java.util.List;
import java.util.Map;

public class UpnpOrderManager {

    public interface ResultCallBacks {
        public void success(ActionInvocation invocation, String uid);

        public void failure(ActionInvocation invocation,
                            UpnpResponse operation, String defaultMsg);
    }

    public static void sendOrder(ControlPoint controlPoint, Action action, List<Object> args, final ResultCallBacks resultCallBacks) {
        ActionInvocation actionInvocation = new ActionInvocation(action);
        ActionArgument[] arguments = action.getInputArguments();
        for (int i = 0; i < arguments.length; i++) {
            actionInvocation.setInput(arguments[i].getName(), args.get(i));
        }
        //UPnP规范提供了一个基于发布/订约的通用事件通知架构GENA。你的控制点去服务订约以便接收事件。
        // 当服务的状态变化，一个事件消息将发送给你的控制点回调。....
        // 如果不用了 调用 callback.end()
        controlPoint.execute(new SubscriptionCallback(action.getService(), 600) {
            @Override
            public void established(GENASubscription sub) {
                System.out.println("Established: " + sub.getSubscriptionId());
            }


            @Override
            protected void failed(GENASubscription subscription,
                                  UpnpResponse responseStatus,
                                  Exception exception,
                                  String defaultMsg) {
                System.err.println(defaultMsg);
            }


            @Override
            public void ended(GENASubscription sub,
                              CancelReason reason,
                              UpnpResponse response) {
            }


            @Override
            public void eventReceived(GENASubscription sub) {
                //每次调用action都会获取设备的这个service中所有属性（sendEvents 不为false）的值
                System.out.println("Event: " + sub.getCurrentSequence().getValue());


                Map<String, StateVariableValue> values = sub.getCurrentValues();//这个Map是一个什么东西？
                System.out.println("values map size --- " + values.size());
                for (Map.Entry<String, StateVariableValue> entry : values.entrySet()) {
                    System.out.println(entry.getKey() + "-----" + entry.getValue());
                }

            }


            @Override
            public void eventsMissed(GENASubscription sub, int numberOfMissedEvents) {
                System.out.println("Missed events: " + numberOfMissedEvents);
            }


            @Override
            protected void invalidMessage(RemoteGENASubscription sub,
                                          UnsupportedDataException ex) {
                // Log/send an error report?
            }
        });

        // 调用这个action
        controlPoint
                .execute(new ActionCallback(actionInvocation, controlPoint) {
                    // 不是线程安全的，不是在主线程中，修改ui要回主线程
                    @Override
                    public void success(ActionInvocation invocation) {
                        if (resultCallBacks != null)
                            resultCallBacks.success(invocation, null);
                    }

                    @Override
                    public void failure(ActionInvocation invocation,
                                        UpnpResponse operation, String defaultMsg) {
                        if (resultCallBacks != null)
                            resultCallBacks.failure(invocation, operation, defaultMsg);
                    }
                });

    }

    public static void sendOrder(Device device, ControlPoint controlPoint,
                                 String ModuleName, String APIName, String Parameter,
                                 final ResultCallBacks callBacks) {

        ActionInvocation actionInvocation = new ActionInvocation(
                device.getServices()[0].getActions()[0]);

        actionInvocation.setInput("ModuleName", ModuleName);
        actionInvocation.setInput("APIName", APIName);
        actionInvocation.setInput("Parameter", Parameter);

        controlPoint
                .execute(new ActionCallback(actionInvocation, controlPoint) {
                    @Override
                    public void success(ActionInvocation invocation) {
                        ActionArgumentValue output = invocation
                                .getOutput("Result");
                        String value = (String) output.getValue();
                        if (callBacks != null)
                            callBacks.success(invocation, null);
                    }

                    @Override
                    public void failure(ActionInvocation invocation,
                                        UpnpResponse operation, String defaultMsg) {
                        if (callBacks != null)
                            callBacks
                                    .failure(invocation, operation, defaultMsg);
                    }
                });
    }

    public static void sendOrder(Device device, ControlPoint controlPoint,
                                 String ModuleName, String APIName, String Parameter,
                                 final ResultCallBacks callBacks, final String uid) {

        ActionInvocation actionInvocation = new ActionInvocation(
                device.getServices()[0].getActions()[0]);

        actionInvocation.setInput("ModuleName", ModuleName);
        actionInvocation.setInput("APIName", APIName);
        actionInvocation.setInput("Parameter", Parameter);

        controlPoint
                .execute(new ActionCallback(actionInvocation, controlPoint) {
                    @Override
                    public void success(ActionInvocation invocation) {
                        ActionArgumentValue output = invocation
                                .getOutput("Result");
                        String value = (String) output.getValue();
                        if (callBacks != null)
                            callBacks.success(invocation, uid);
                    }

                    @Override
                    public void failure(ActionInvocation invocation,
                                        UpnpResponse operation, String defaultMsg) {
                        if (callBacks != null)
                            callBacks
                                    .failure(invocation, operation, defaultMsg);
                    }
                });
    }

}

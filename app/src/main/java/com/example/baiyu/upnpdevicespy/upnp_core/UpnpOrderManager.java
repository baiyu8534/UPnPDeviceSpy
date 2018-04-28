package com.example.baiyu.upnpdevicespy.upnp_core;

import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.action.ActionArgumentValue;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Action;
import org.fourthline.cling.model.meta.ActionArgument;
import org.fourthline.cling.model.meta.Device;

import java.util.List;

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

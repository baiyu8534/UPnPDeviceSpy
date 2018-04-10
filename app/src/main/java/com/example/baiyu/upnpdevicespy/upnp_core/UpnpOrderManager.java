package com.example.baiyu.upnpdevicespy.upnp_core;

import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.action.ActionArgumentValue;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;

public class UpnpOrderManager {

	public interface ResultCallBacks {
		public void success(String result, String uid);

		public void failure(ActionInvocation invocation,
                            UpnpResponse operation, String defaultMsg);
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
							callBacks.success(value, null);
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
							callBacks.success(value, uid);
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

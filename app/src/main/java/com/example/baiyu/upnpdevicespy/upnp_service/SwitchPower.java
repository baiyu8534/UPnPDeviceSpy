package com.example.baiyu.upnpdevicespy.upnp_service;

import org.fourthline.cling.binding.annotations.UpnpAction;
import org.fourthline.cling.binding.annotations.UpnpInputArgument;
import org.fourthline.cling.binding.annotations.UpnpOutputArgument;
import org.fourthline.cling.binding.annotations.UpnpService;
import org.fourthline.cling.binding.annotations.UpnpServiceId;
import org.fourthline.cling.binding.annotations.UpnpServiceType;
import org.fourthline.cling.binding.annotations.UpnpStateVariable;

@UpnpService(serviceId = @UpnpServiceId("SwitchPower"), serviceType = @UpnpServiceType(value = "SwitchPower", version = 1))
public class SwitchPower {
	@UpnpStateVariable(defaultValue = "0", sendEvents = false)
	private int target = 0;

	@UpnpStateVariable(defaultValue = "0")
	private int status = 0;

	@UpnpAction
	public void setTarget(
			@UpnpInputArgument(name = "NewTargetValue") int newTargetValue) {
		target = newTargetValue;
		status = newTargetValue;
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
}

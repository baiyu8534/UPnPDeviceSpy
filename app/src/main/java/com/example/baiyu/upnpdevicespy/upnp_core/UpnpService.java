package com.example.baiyu.upnpdevicespy.upnp_core;

import android.content.Intent;
import android.os.IBinder;

import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.controlpoint.ControlPoint;
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

}

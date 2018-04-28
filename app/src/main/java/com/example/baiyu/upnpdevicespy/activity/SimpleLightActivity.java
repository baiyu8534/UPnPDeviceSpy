package com.example.baiyu.upnpdevicespy.activity;

// TODO: 2018/4/28 这是一个添加localDevice的avtivity 可以放到其他app中
/**
public class SimpleLightActivity extends AppCompatActivity implements PropertyChangeListener {

    private UDN mUdn;
    private View v_light;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v_light = findViewById(R.id.v_light);

        Intent intent = new Intent(this, UpnpService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        LocalService<SwitchPower> switchPowerService = getSwitchPowerService();
//        if (switchPowerService != null)
//            switchPowerService.getManager().getImplementation().getPropertyChangeSupport()
//                    .removePropertyChangeListener(this);

        unbindService(conn);
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            UpnpService.LocalBinder binder = (UpnpService.LocalBinder) iBinder;
            UpnpService service = binder.getService();
            UpnpManager.getInstance().setUpnpService(service);
            UpnpManager.getInstance().searchAllDevices();

            //Create LocalDevice
            LocalService<SwitchPower> localService = new AnnotationLocalServiceBinder().read(SwitchPower.class);
            localService.setManager(new DefaultServiceManager<>(
                    localService, SwitchPower.class));

            String device_name = "simple_light";
            //Generate UUID by MAC address
            mUdn = UDN.valueOf(UUID.nameUUIDFromBytes(device_name.getBytes()).toString());
            DeviceIdentity deviceIdentity = new DeviceIdentity(mUdn);
            DeviceType type = new UDADeviceType("BinaryLight", 1);
            //这个可以生成指定命名空间的设备 别人搜还是搜的到的，不过可以用这个来标识你的设备，到时候可以从所有搜到的
            //设备中只拿你要控制的设备类型
//            DeviceType type = new DeviceType("ssssss","BinaryLight", 1);
            DeviceDetails details =
                    new DeviceDetails(
                            "Friendly Binary Light",
                            new ManufacturerDetails("ACME"),
                            new ModelDetails(
                                    "BinLight2000",
                                    "A demo light with on/off switch.",
                                    "v1"
                            )
                    );
            LocalDevice localDevice = null;
            try {
                localDevice = new LocalDevice(deviceIdentity, type, details, localService);
            } catch (ValidationException e) {
                e.printStackTrace();
                Toast.makeText(SimpleLightActivity.this, "can not create device!", Toast.LENGTH_LONG).show();
            }
            if (localDevice != null) {
                UpnpManager.getInstance().getRegistry().addDevice(localDevice);
            }

            // Obtain the state of the power switch and update the UI
            setLightbulb(localService.getManager().getImplementation().getStatus());


            // Start monitoring the power switch
            localService.getManager().getImplementation().getPropertyChangeSupport()
                    .addPropertyChangeListener(SimpleLightActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        System.out.println("propertyChange!!!!!!!!!!!");
        System.out.println("propertyChangeEvent.getPropertyName()!!!!!!!!!!!"+propertyChangeEvent.getPropertyName());
        if (propertyChangeEvent.getPropertyName().equals("status")) {
            System.out.println("Turning light: " + propertyChangeEvent.getNewValue());
            setLightbulb((Integer) propertyChangeEvent.getNewValue());
        }
    }

    protected void setLightbulb(final int liget_status) {
        runOnUiThread(new Runnable() {
            public void run() {
                v_light.setBackgroundColor(liget_status == 0 ? getResources().getColor(R.color.light_off) : getResources().getColor(R.color.light_on));
            }
        });
    }

    protected LocalService<SwitchPower> getSwitchPowerService() {
        if (UpnpManager.getInstance() == null)
            return null;


        LocalDevice binaryLightDevice;
        if ((binaryLightDevice = UpnpManager.getInstance().getRegistry().getLocalDevice(mUdn, true)) == null)
            return null;


        return (LocalService<SwitchPower>)
                binaryLightDevice.findService(new UDAServiceType("SwitchPower", 1));
    }


}
 */

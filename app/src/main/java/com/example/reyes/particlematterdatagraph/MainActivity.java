package com.example.reyes.particlematterdatagraph;

import android.bluetooth.BluetoothProfile;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.reyes.bluetooth.BluetoothDeviceAdapter;
import com.example.reyes.bluetooth.SearchBluetoothDevices;
import com.example.reyes.bluetooth.ConnectThread;

import com.example.reyes.data.DataPair;
import com.example.reyes.data.SensorData;
import com.example.reyes.graph.GraphObj;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.LineData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SearchBluetoothDevices bt;
    private String[] boundDeviceNames = null;
    private ArrayList<BluetoothDevice> discoveredDevices = new ArrayList<>();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDeviceAdapter mNewDevicesArrayAdapter;
    private BluetoothDevice device;
    private ConnectThread ct;
    private EditText editText_rawData;
    private static Handler mHandler;
    private SensorData sd;
    private GraphObj graphObj;
    private DataPair[] updatedDataPairs;
    private BroadcastReceiver blueToothReceiver;
    private int lastRadioButtonChecked = 0;
    RadioGroup radioGroupData;
    RadioButton radioButtonUgm3, radioButton_ugm3_aed, radioButton_umL, radioButton_AllData;


    LineChart chart;

    public MainActivity() {


        // find a way to set this to limit to 100ms; 1/10 of a second
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                sd = (SensorData) message.obj;
                String writeBuf = sd.ToString();

                if(graphObj == null) {
                    updatedDataPairs = sd.GetAllData();
                    graphObj = new GraphObj(updatedDataPairs);
                    lastRadioButtonChecked = radioGroupData.getCheckedRadioButtonId();

                } else if(radioGroupData.getCheckedRadioButtonId() == radioButton_AllData.getId()) {
                        updatedDataPairs = sd.GetAllData();

                } else if(radioGroupData.getCheckedRadioButtonId() == radioButtonUgm3.getId()) {
                    updatedDataPairs = sd.GetMicrogramPerMeterCubed();

                } else if(radioGroupData.getCheckedRadioButtonId() == radioButton_ugm3_aed.getId()) {
                    updatedDataPairs = sd.GetMicrogramPerMeterCubedStandPart();

                } else if(radioGroupData.getCheckedRadioButtonId() == radioButton_umL.getId()) {
                    updatedDataPairs = sd.GetMicrometerPerTenthAirLiter();
                }
                if(radioGroupData.getCheckedRadioButtonId() != lastRadioButtonChecked) {
                    graphObj = new GraphObj(updatedDataPairs);
                    lastRadioButtonChecked = radioGroupData.getCheckedRadioButtonId();

                }

                ChartThread();


                // AsyncTasks are configurable to run sequentially or in parallel, by inputting 'AsyncTask'.SERIAL_EXECUTOR will allow us to run sequentially.
                // meaning, each thread will not finish until the previous has finished first.
                // .THREAD_POOL_EXECUTOR will allow running in parallel
                //asyncGraph = new AsyncGraph();
                //asyncGraph.executeOnExecutor(
                //        AsyncGraph.SERIAL_EXECUTOR, chart
                //);

                switch(message.what) {
                    case 1:
                        editText_rawData.setText(writeBuf);
                        break;
                }
                return false;
            }
        });

        blueToothReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d("onReceive: ", "Attempt to get received data...");

                assert action != null;
                if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        discoveredDevices.add(device);

                    }
                } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                    mNewDevicesArrayAdapter.getCount();
                }
                mNewDevicesArrayAdapter = new BluetoothDeviceAdapter(MainActivity.this, discoveredDevices);
                //Attach the adapter to a ListView
                ListView btListView = findViewById(R.id.listView_discoveredDevices);
                btListView.setAdapter(mNewDevicesArrayAdapter);

            }
        };

    }

    private void RefreshChart() {
        // Get the chart;
        chart = findViewById(R.id.myChart);
        chart.setBackgroundColor(Color.WHITE);
        chart.setGridBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(true);
        chart.setDrawBorders(true);
        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);
        Legend l = chart.getLegend();
        l.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        radioGroupData = findViewById(R.id.radioGroup_data);
        radioButtonUgm3 = findViewById(R.id.radioButton_ugm3);
        radioButton_ugm3_aed = findViewById(R.id.radioButton_ugm3_aed);
        radioButton_umL = findViewById(R.id.radioButton_umL);
        radioButton_AllData = findViewById(R.id.radioButton_AllData);
        radioGroupData.check(radioButton_AllData.getId());


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        bt = new SearchBluetoothDevices(getApplicationContext());
        ct = null;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        editText_rawData = findViewById(R.id.editText_rawData);


        // Get the chart;
        RefreshChart();

        // Bug with library - https://stackoverflow.com/questions/38694119/negativearraysizeexception-adding-scatter-data-to-a-combinedchart
        // Most of the time the Negative ArraySizeException is caused by unsorted data in the new version
        // This is because the library relies on sorted data for performance optimizations and thus doesn't work
        // properly with unsorted data.
        //Collections.sort(lineEntries, new EntryXComparator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void update_onClick(View view) {
    }

    private void ChartThread() {
        new Thread(
            new Runnable() {
                public void run() {
                    graphObj.AddEntry(updatedDataPairs);
                    updateChart(graphObj.GetLineData());
                }
            }
        ).start();
    }

    private void updateChart(final LineData lineData) {
        chart.post(new Runnable() {
            /**
             * When an object implementing interface <code>Runnable</code> is used
             * to create a thread, starting the thread causes the object's
             * <code>run</code> method to be called in that separately executing
             * thread.
             * <p>
             * The general contract of the method <code>run</code> is that it may
             * take any action whatsoever.
             *
             * @see Thread#run()
             */
            @Override
            public void run() {
                chart.setData(lineData);
                chart.invalidate();
            }
        });
    }

    public void cancelReadBtData_onClick(View view) {
        ct.cancel();
    }

    public void search_onClick(View view) {
        ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>(bt.GetPairedBluetoothDevices());
        GetBluetoothDevices(bluetoothDevices);

        //populate pairedDevices


        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogStyle));
        builder.setTitle(R.string.title_dialog_pairedDevices)
                .setItems(boundDeviceNames, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which ) {
                        // The 'which' argument contains the index position
                        // Connect to the bluetooth device

                    }
                }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do something for ok.
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do something for cancel
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void populatePairedDevices() {

    }

    public void discover_onClick(View view) {
        // This code was needed for permissions, had issues trying to find devices via 'discovery'
        // reference - https://stackoverflow.com/questions/37638665/broadcastreceiver-for-bluetooth-device-discovery-works-on-one-device-but-not-on
        final int CODE = 5; // app defined constant used for onRequestPermissionsResult

        discoveredDevices.clear();

        String[] permissionsToRequest =
                {
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                };

        boolean allPermissionsGranted = true;

        for(String permission : permissionsToRequest)
        {
            allPermissionsGranted = allPermissionsGranted && (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED);
        }

        if(!allPermissionsGranted)
        {
            ActivityCompat.requestPermissions(this, permissionsToRequest, CODE);
        }

        Log.d("DiscBTDevices", "Attempting to discover devices...");

        //IntentFilter will match the action specified
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);

        //broadcast receiver for any matching filter
        if(mBluetoothAdapter.isDiscovering()) {
            // Bluetooth is already in discovery mode, we cancel to restart it again
            mBluetoothAdapter.cancelDiscovery();
            unregisterReceiver(blueToothReceiver);
        }
        //broadcast receiver for any matching filter
        this.registerReceiver(blueToothReceiver, filter);
        mBluetoothAdapter.startDiscovery();

        // Create the adapter to convert to array of views
        BluetoothDeviceAdapter btdAdapter = new BluetoothDeviceAdapter(this, discoveredDevices);

        //Attach the adapter to a ListView
        ListView btListView = findViewById(R.id.listView_discoveredDevices);
        btListView.setAdapter(btdAdapter);

        btListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                readBtData(i);

            }
        });
    }

    public static boolean isBluetoothConnected() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()
                && mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP) == BluetoothProfile.STATE_CONNECTED;
    }


    public void readBtData(int index) {

        device = discoveredDevices.get(index);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Device Name: " + device.getName() + "\n" + "Device Address: " + device.getAddress(), Toast.LENGTH_SHORT).show();
            }
        });
        ct = new ConnectThread(device, mBluetoothAdapter, mHandler);
        ct.start();

    }

    private void GetBluetoothDevices(ArrayList<BluetoothDevice> btDevices) {
        boundDeviceNames = new String[btDevices.size()];
        for(int i = 0; i < btDevices.size(); i++) {
            boundDeviceNames[i] = btDevices.get(i).getName() + " : " + btDevices.get(i).getAddress();
        }
    }
}
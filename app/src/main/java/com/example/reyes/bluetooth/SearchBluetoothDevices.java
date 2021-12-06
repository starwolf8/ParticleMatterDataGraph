package com.example.reyes.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;


public class SearchBluetoothDevices {
    private BluetoothAdapter mBluetoothAdapter;
    private Context _context;


    public SearchBluetoothDevices(Context context) {

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        _context = context;
    }

    public ArrayList<BluetoothDevice> GetPairedBluetoothDevices() {

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if(pairedDevices.size() > 0) {
            Toast.makeText(_context, "Finished with the discovery!", Toast.LENGTH_LONG).show();
            return new ArrayList<>(pairedDevices);
        } else {
            return null;
        }
    }
}
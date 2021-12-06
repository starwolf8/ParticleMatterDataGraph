package com.example.reyes.bluetooth;

import android.app.Notification;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import static android.content.ContentValues.TAG;

// replaced MY_UUID using this reference - https://stackoverflow.com/questions/21457175/android-bluetooth-uuid-connecting-app-to-android

public class ConnectThread extends Thread {
    private final BluetoothAdapter mBluetoothAdapter;
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private static Handler mHandler;
    private static ConnectedThread mConnectedThread;

    // Default UUID
    private UUID DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public ConnectThread(BluetoothDevice device, BluetoothAdapter adapter, Handler handler) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        mHandler = handler;
        mmDevice = device;
        mBluetoothAdapter = adapter;
        mConnectedThread = null;


        try {
            Log.i(TAG, "Device Name: " + mmDevice.getName());
            Log.i(TAG, "Device UUID: " + mmDevice.fetchUuidsWithSdp());//.getUuids()[0].getUuid());
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.

            tmp = mmDevice.createRfcommSocketToServiceRecord(mmDevice.getUuids()[0].getUuid());
        }
        catch (NullPointerException e)
        {
            Log.d(TAG, " UUID from device is null, Using Default UUID, Device name: " + mmDevice.getName());
            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(DEFAULT_UUID);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        catch (IOException e) { }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it otherwise slows down the connection.
        mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }
            return;
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        //manageMyConnectedSocket(mmSocket);
        mConnectedThread = new ConnectedThread(mmSocket, mHandler);
        mConnectedThread.start();
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }

}
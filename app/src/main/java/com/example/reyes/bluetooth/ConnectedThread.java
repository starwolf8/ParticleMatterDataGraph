package com.example.reyes.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.example.reyes.data.SensorData;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/***
 * Sending and Receiving Data via Bluetooth with and Android Device
 * Reference - https://www.egr.msu.edu/classes/ece480/capstone/spring14/group01/docs/appnote/Wirsing-SendingAndReceivingDataViaBluetoothWithAnAndroidDevice.pdf
 *
 */

public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private InputStream mmInStream;
    private final Handler mHandler;
    private SensorData sd;

    ConnectedThread(BluetoothSocket socket, Handler handler) {
        mmSocket = socket;
        mHandler = handler;
        mmInStream = null;
        sd = new SensorData();
        try {
            mmInStream = mmSocket.getInputStream();
        } catch (IOException ignored) { }
    }
    public void run() {
        int availableBytes;
        int bytes;
        while (true) {
            try {
                availableBytes = mmInStream.available();

                if(availableBytes == 32) {
                    byte[] buffer = new byte[availableBytes];
                    bytes = mmInStream.read(buffer);
                    sd.SetAllBytes(buffer);

                    Log.d("mmInStream.read(buffer)", new String(buffer));
                    if( bytes > 0 ) {
                        mHandler.obtainMessage(1, bytes, -1, sd).sendToTarget();
                    }
                }
            } catch (IOException e) {
                Log.d("Error reading", e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }

    private String CollectSensorData(byte[] rawData) throws UnsupportedEncodingException {
        String sensorData = "";

        if(rawData.length == 32) {
            if(rawData[0] == 0x42 && rawData[1] == 0x4d) {
                sensorData += new String(rawData, "UTF-8");
            }
        }

        return sensorData;
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException ignored) { }
    }
}
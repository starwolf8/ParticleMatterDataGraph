package com.example.reyes.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DataPair {
    private String _title = "";
    private int _dataValue = 0;

    public DataPair(String title, byte[] dataByteValue) {
        this._title = title;
        this._dataValue = ConvertDataToInteger(dataByteValue);
    }

    public DataPair() {}

    public String GetTitle() {
        return _title;
    }

    public int GetDataValue() {
        return _dataValue;
    }

    public void SetTitle(String title) {
        this._title = title;
    }

    public void SetDataValue(int dataValue) {
        this._dataValue = _dataValue;
    }

    public void SetDataValue(byte[] dataByteValue) {
        this._dataValue = ConvertDataToInteger(dataByteValue);
    }

    /**
     * Helper method used to convert bytes into an integer.
     * @param data bytes used to convert into an integer.
     * @return int
     */
    private int ConvertDataToInteger(byte[] data) {

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        byteBuffer.put((byte)0x00);
        byteBuffer.put((byte)0x00);
        byteBuffer.put(data);
        byteBuffer.flip();

        return byteBuffer.getInt();
    }
}

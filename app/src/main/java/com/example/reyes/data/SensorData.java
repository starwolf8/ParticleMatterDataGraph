package com.example.reyes.data;


import android.util.Pair;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Class - Sensor Data.
 */
public class SensorData {
    //private byte START_CHAR_1 = 0x42;
    //private byte START_CHAR_2 = 0X4d;
    private byte _FrameLength_H, _FrameLength_L;
    private byte _Data1_H, _Data1_L, _Data2_H, _Data2_L, _Data3_H, _Data3_L;
    private byte _Data4_H, _Data4_L, _Data5_H, _Data5_L, _Data6_H, _Data6_L;
    private byte _Data7_H, _Data7_L, _Data8_H, _Data8_L, _Data9_H, _Data9_L;
    private byte _Data10_H, _Data10_L, _Data11_H, _Data11_L, _Data12_H, _Data12_L;
    private byte _Data13_H, _Data13_L,_Data_Check_H, _Data_Check_L;

    /**
     * Constructor - Sensor Data; initializes class properties.
     */
    public SensorData() {
        _FrameLength_L = (byte)0xFF;
        _FrameLength_H = (byte)0xFF;
        _Data1_L = (byte)0xFF;
        _Data1_H = (byte)0xFF;
        _Data2_L = (byte)0xFF;
        _Data2_H = (byte)0xFF;
        _Data3_L = (byte)0xFF;
        _Data3_H = (byte)0xFF;
        _Data4_L = (byte)0xFF;
        _Data4_H = (byte)0xFF;
        _Data5_L = (byte)0xFF;
        _Data5_H = (byte)0xFF;
        _Data6_L = (byte)0xFF;
        _Data6_H = (byte)0xFF;
        _Data7_L = (byte)0xFF;
        _Data7_H = (byte)0xFF;
        _Data8_L = (byte)0xFF;
        _Data8_H = (byte)0xFF;
        _Data9_L = (byte)0xFF;
        _Data9_H = (byte)0xFF;
        _Data10_L = (byte)0xFF;
        _Data10_H = (byte)0xFF;
        _Data11_L = (byte)0xFF;
        _Data11_H = (byte)0xFF;
        _Data12_L = (byte)0xFF;
        _Data12_H = (byte)0xFF;
        _Data13_L = (byte)0xFF;
        _Data13_H = (byte)0xFF;
        _Data_Check_L = (byte)0xFF;
        _Data_Check_H = (byte)0xFF;
    }

    public DataPair[] GetAllData() {

        return new DataPair[] {GetData1(), GetData2(), GetData3(), GetData4(), GetData5(), GetData6(), GetData7(), GetData8(), GetData9(), GetData10(), GetData11(), GetData12() };
    }

    /**
     * Returns an array of integers of data (1 - 3)
     * @return int[]
     */
    public DataPair[] GetMicrogramPerMeterCubedStandPart() {
        return new DataPair[] {GetData1(), GetData2(), GetData3()};
    }

    /**
     * Returns an array of integers of data (4 - 6)
     * @return int[]
     */
    public DataPair[] GetMicrogramPerMeterCubed() {
        return new DataPair[] {GetData4(), GetData5(), GetData6()};
    }

    /**
     * Returns an array of integers of data (7 - 12)
     * @return int[]
     */
    public DataPair[] GetMicrometerPerTenthAirLiter() {
        return new DataPair[] { GetData7(), GetData8(), GetData9(), GetData10(), GetData11(), GetData12()};
    }

    /**
     * Used to set collect and save sensor data for future parsing.
     * @param collectedData data collected, to be transferred to
     */
    public void SetAllBytes(byte[] collectedData) {
        _FrameLength_L = collectedData[2];
        _FrameLength_H = collectedData[3];
        _Data1_L = collectedData[4];
        _Data1_H = collectedData[5];
        _Data2_L = collectedData[6];
        _Data2_H = collectedData[7];
        _Data3_L = collectedData[8];
        _Data3_H = collectedData[9];
        _Data4_L = collectedData[10];
        _Data4_H = collectedData[11];
        _Data5_L = collectedData[12];
        _Data5_H = collectedData[13];
        _Data6_L = collectedData[14];
        _Data6_H = collectedData[15];
        _Data7_L = collectedData[16];
        _Data7_H = collectedData[17];
        _Data8_L = collectedData[18];
        _Data8_H = collectedData[19];
        _Data9_L = collectedData[20];
        _Data9_H = collectedData[21];
        _Data10_L = collectedData[22];
        _Data10_H = collectedData[23];
        _Data11_L = collectedData[24];
        _Data11_H = collectedData[25];
        _Data12_L = collectedData[26];
        _Data12_H = collectedData[27];
        _Data13_L = collectedData[28];
        _Data13_H = collectedData[29];
        _Data_Check_L = collectedData[30];
        _Data_Check_H = collectedData[31];
    }

    /**
     * Currently not in use, have not found a purpose for it yet, but will keep it here just in case.
     * @return byte[]
     */
    private byte[] GetFrameLength() {
        return new byte[] {
                _FrameLength_H, _FrameLength_L
        };
    }

    /**
     * Currently not in use, have not found a purpose for it yet, but will keep it here just in case.
     * @param frameLength_h - high 8 bytes of frame length
     * @param frameLength_l - low 8 bytes of frame length
     */
    private void SetFrameLength(byte frameLength_h, byte frameLength_l) {
        _FrameLength_H = frameLength_h;
        _FrameLength_L = frameLength_l;
    }

    /**
     * Data 1 refers to PM1.0 concentration unit
     * μ g/m3（CF=1，standard particle）*
     * @return Pair<String, byte[]>
     */
    private DataPair GetData1() {
        return new DataPair("1.0μ g/m3", new byte[] {_Data1_L, _Data1_H});
    }

    /**
     * Data 2 refers to PM2.5 concentration unit
     * μ g/m3（CF=1，standard particle）
     * @return Pair<String, byte[]>
     */
    private DataPair GetData2() {
        return new DataPair("2.5μ g/m3", new byte[] {_Data2_L, _Data2_H});
    }

    /**
     * Data 3 refers to PM10 concentration unit
     * μ g/m3（CF=1，standard particle）
     * @return Pair<String, byte[]>
     */
    private DataPair GetData3() {
        return new DataPair("10μ g/m3", new byte[] {_Data3_L, _Data3_H});
    }

    /**
     * Data 4 refers to PM1.0 concentration unit *
     * μ g/m3（under atmospheric environment）
     * @return Pair<String, byte[]>
     */
    private DataPair GetData4() {
        return new DataPair("1.0μ g/m3", new byte[] { _Data4_L, _Data4_H});
    }

    /**
     * Data 5 refers to PM2.5 concentration unit
     * μ g/m3（under atmospheric environment）
     * @return Pair<String, byte[]>
     */
    private DataPair GetData5() {
        return new DataPair("2.5μ g/m3", new byte[] {_Data5_L, _Data5_H});
    }

    /**
     * Data 6 refers to concentration unit (under
     * atmospheric environment) μ g/m3
     * @return Pair<String, byte[]>
     */
    private DataPair GetData6() {
        return new DataPair("μ g/m3", new byte[] { _Data6_L, _Data6_H});
    }

    /**
     * Data 7 indicates the number of particles with diameter beyond 0.3 um in 0.1 L of air.
     * @return Pair<String, byte[]>
     */
    private DataPair GetData7() {
        return new DataPair("0.3 um/0.1L", new byte[] {_Data7_L, _Data7_H});
    }

    /**
     * Data 8 indicates the number of particles with diameter beyond 0.5 um in 0.1 L of air.
     * @return Pair<String, byte[]>
     */
    private DataPair GetData8() {
        return new DataPair("0.5 um/0.1L", new byte[] {_Data8_L, _Data8_H});
    }

    /**
     * Data 9 indicates the number of particles with diameter beyond 1.0um in 0.1 L of air.
     * @return Pair<String, byte[]>
     */
    private DataPair GetData9() {
        return new DataPair("1.0 um/0.1L", new byte[] {_Data9_L, _Data9_H});
    }

    /**
     * Data 10 indicates the number of particles with diameter beyond 2.5 um in 0.1L of air.
     * @return Pair<String, byte[]>
     */
    private DataPair GetData10() {
        return new DataPair("2.5 um/0.1L", new byte[] {_Data10_L, _Data10_H});
    }

    /**
     * Data 11 indicates the number of particles with diameter beyond 5.0um in 0.1L of air.
     * @return Pair<String, byte[]>
     */
    private DataPair GetData11() {
        return new DataPair("5.0 um/0.1L", new byte[] {_Data11_L, _Data11_H});
    }

    /**
     * Data 12 indicates the number of particles with diameter beyond 10um in 0.1L of air.
     * @return Pair<String, byte[]>
     */
    private DataPair GetData12() {
        return new DataPair("10 um/0.1L", new byte[] {_Data12_L, _Data12_H});
    }

    /**
     *  Returns a string of all data collected in the class/object (SensorData.java)
     * @return String
     */
    public String ToString() {
        return "";

    }
}
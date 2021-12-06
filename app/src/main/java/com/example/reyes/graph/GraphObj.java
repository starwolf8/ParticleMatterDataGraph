package com.example.reyes.graph;

import android.graphics.Color;

import com.example.reyes.data.DataPair;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;


/**
 *
 */
public class GraphObj {

    private LineData _lineData = null;
    private DataPair[] _dataPairs;
    private LineDataSet[] _lineDataSets = null;
    private float _count;
    private int ORANGE = Color.rgb(255, 127, 0);
    private int INDIGO = Color.rgb(	75, 0, 130);
    private int VIOLET = Color.rgb(127, 0, 255);
    private int AZURE_BLUE = Color.rgb(0, 127, 255);
    private int ROSE = Color.rgb(255, 0, 127);
    private int[] colors = new int[] { Color.RED, ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, INDIGO, VIOLET, Color.BLACK, Color.CYAN, AZURE_BLUE, Color.MAGENTA, ROSE };

    /**
     *
     * @param dataPairs
     */
    public GraphObj(DataPair[] dataPairs) {
        this._dataPairs = dataPairs;
        _count = 1;
        CreateEntries();
    }


    /**
     *
     * @param entryCollection
     */
    public void AddEntry(DataPair[] entryCollection) {
        int collectionCount = entryCollection.length;

        for(int i = 0; i < collectionCount; i++) {
            _lineDataSets[i].addEntry(new Entry(_count, entryCollection[i].GetDataValue()));
        }
        _count++;
    }

    /**
     *
     * @return
     */
    public LineData GetLineData() {
        return new LineData(_lineDataSets);
    }

    /**
     *
     */
    private void CreateEntries() {
        int entryCount = this._dataPairs.length;
        _lineDataSets = new LineDataSet[entryCount];

        for(int i = 0; i < entryCount; i++) {
            _lineDataSets[i] = new LineDataSet(new ArrayList<Entry>(), this._dataPairs[i].GetTitle());
            _lineDataSets[i].setMode(LineDataSet.Mode.CUBIC_BEZIER);
            _lineDataSets[i].setColor(colors[i]);
            _lineDataSets[i].setCircleColor(colors[i]);
            _lineDataSets[i].setAxisDependency(YAxis.AxisDependency.LEFT);
        }

        _lineData = new LineData(_lineDataSets);
    }
}
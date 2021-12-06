package com.example.reyes.graph;

import android.os.AsyncTask;

import com.example.reyes.data.DataPair;
import com.github.mikephil.charting.charts.LineChart;

public class AsyncGraph extends AsyncTask<LineChart, DataPair[], Boolean> {
    LineChart _lineChart;

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param lineCharts The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Boolean doInBackground(LineChart... lineCharts) {
        Boolean returnValue = false;
        if(lineCharts.length > 0) {
            //GraphObj graphObj = new GraphObj();
            _lineChart = lineCharts[0];

            returnValue = true;
        }




        //publishProgress("Starting Async Task Graph");
        //Worker worker = new Worker(_lineChart.getContext());

        return returnValue;
    }

    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     *
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param aBoolean The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(aBoolean) {
            //do something with linechart.
            // maybe update the chart?
            //
            // _lineChart.
        }
        super.onPostExecute(aBoolean);
    }

    /**
     * Runs on the UI thread after {@link #publishProgress} is invoked.
     * The specified values are the values passed to {@link #publishProgress}.
     *
     * @param values The values indicating progress.
     * @see #publishProgress
     * @see #doInBackground
     */
    @Override
    protected void onProgressUpdate(DataPair[]... values) {

        super.onProgressUpdate(values);
    }
}

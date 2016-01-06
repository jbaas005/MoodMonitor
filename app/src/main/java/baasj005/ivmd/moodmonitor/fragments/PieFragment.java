package baasj005.ivmd.moodmonitor.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import baasj005.ivmd.moodmonitor.R;
import baasj005.ivmd.moodmonitor.models.Mood;
import baasj005.ivmd.moodmonitor.tasks.MoodGetTask;
import baasj005.ivmd.moodmonitor.util.JsonUtil;
import okhttp3.Response;

/**
 * Created by Jesse on 6-1-2016.
 */
public class PieFragment extends Fragment{
    private PieChart pieChart;
    private static final List<String> xLabels = new ArrayList<String>(
            Arrays.asList("0 - 1", "1 - 2", "2 - 3", "3 - 4", "4 - 5"));
    private static final List<Integer> colors = new ArrayList<Integer>(
            Arrays.asList(Color.rgb(204,0,0),Color.rgb(204, 102, 0) , Color.rgb(204,204,0), Color.rgb(102,204,0), Color.rgb(0,102,0)));
    public static PieFragment newInstance(){
        PieFragment fragment = new PieFragment();
        return fragment;
    }

    public PieFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Response task = null;
        ArrayList<Mood> moodData;
        try {
            task = new MoodGetTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        moodData = JsonUtil.getMoodFromInputStream(task != null ? task.body().byteStream() : null);
        View rootView = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        pieChart = (PieChart) rootView.findViewById(R.id.pieChart);
        setGraphOptions();
        PieData data = new PieData(xLabels, getDataSet(getMoodPercentageData(moodData)));
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.invalidate();
        return rootView;
    }

    private float[]getMoodPercentageData(ArrayList<Mood> data){
        float[] result = new float[5];
        for(Mood current : data){
            float moodLevel = current.getMoodLevel();
            if(moodLevel < 1){
                result[0]++;
            }else if(moodLevel < 2){
                result[1]++;
            }else if(moodLevel < 3){
                result[2]++;
            }else if(moodLevel < 4){
                result[3]++;
            }else if(moodLevel > 4){
                result[4]++;
            }
        }
        return result;
    }

    private PieDataSet getDataSet(float[] data){
        ArrayList<Entry> entries = new ArrayList<>();
        for(int i = 0; i < data.length; i++){
            entries.add(new Entry(data[i], i));
        }
        PieDataSet dataSet = new PieDataSet(entries, "Mood level percentage");
        dataSet.setColors(colors);
        return dataSet;
    }

    private void setGraphOptions(){
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("");
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColorTransparent(true);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        Legend l = pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

}

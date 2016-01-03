package baasj005.ivmd.moodmonitor.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import baasj005.ivmd.moodmonitor.R;
import baasj005.ivmd.moodmonitor.models.Mood;
import baasj005.ivmd.moodmonitor.tasks.MoodGetTask;
import baasj005.ivmd.moodmonitor.util.JsonUtil;
import okhttp3.Response;


/**
 * Created by Jesse on 3-1-2016.
 */
public class BarFragment extends Fragment {
    private static final String GRAPH_TITLE = "Average mood per day";
    private static final List<String> xLabels = new ArrayList<String>(
            Arrays.asList("Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"));
    private HorizontalBarChart barChart;

    public static BarFragment newInstance(){
        return new BarFragment();
    }

    public BarFragment(){
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
        View rootView = inflater.inflate(R.layout.fragment_bar_graph, container, false);
        barChart = (HorizontalBarChart) rootView.findViewById(R.id.barChart);
        setGraphOptions();
        BarDataSet dataSet = getDataSet(getAverageMoodPerDay(moodData));
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);
        BarData data = new BarData(xLabels, dataSet);
        barChart.setData(data);
        return rootView;
    }

    private float[] getAverageMoodPerDay(ArrayList<Mood> moodData){
        int[] totalDataPerDay = new int[]{0,0,0,0,0,0,0,0};
        Calendar cal = Calendar.getInstance();
        float[] result = new float[8];
        for(Mood mood : moodData){
            Date date = new Date();
            date.setTime(mood.getMoodDate());
            cal.setTime(date);
            result[cal.get(Calendar.DAY_OF_WEEK)] += mood.getMoodLevel();
            result[cal.get(Calendar.DAY_OF_WEEK)] = result[cal.get(Calendar.DAY_OF_WEEK)];
            totalDataPerDay[cal.get(Calendar.DAY_OF_WEEK)]++;
        }
        for(int i = 0; i < result.length; i++){
            if(totalDataPerDay[i] == 0){
                result[i] = result[i];
            }else {
                result[i] = result[i] / totalDataPerDay[i];
            }
        }
        return result;
    }

    private void setGraphOptions(){
        barChart.setTouchEnabled(true);
        barChart.setDrawValueAboveBar(true);
        barChart.setDescription("");
        barChart.setExtraOffsets(30,25,15,35);
        barChart.setDrawGridBackground(false);
        barChart.animateY(3000);
        YAxis yAxisLeft = barChart.getAxis(YAxis.AxisDependency.LEFT);
        YAxis yAxisRight = barChart.getAxis(YAxis.AxisDependency.RIGHT);
        yAxisLeft.setAxisMaxValue(5);
        yAxisLeft.setShowOnlyMinMax(true);
        yAxisRight.setAxisMaxValue(5);
    }

    private BarDataSet getDataSet(float[] data){
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i = 1; i < data.length; i++){
            entries.add(new BarEntry(data[i], i-1));
        }
        return new BarDataSet(entries, "Average mood level");
    }

}

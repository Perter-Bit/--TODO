package com.example.homework.chart;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;

import com.example.homework.R;
import com.example.homework.utils.RadarMarkerView;
//import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class myradar
{
    public static void setradar(RadarChart chart, Context context,Map<String,Integer> map)
    {
        MarkerView mv = new RadarMarkerView(context, R.layout.radar_markerview);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart
        //执行动画效果
        chart.animateXY(1400, 1400);
        // 获取雷达图的布局参数
        // 应用布局参数
        Description description = chart.getDescription();
        // 隐藏描述标签
        description.setEnabled(false);
        //x轴设置
        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        Set<String> keys = map.keySet();
        String[] keyArray = keys.toArray(new String[0]);
        String[] newKeyArray = IntStream.range(0, keyArray.length)
                .filter(i -> i < 8 && !keyArray[i].matches(".*\\d.*")) // 限制只获取前四个不包含数字的键
                .mapToObj(i -> keyArray[i])
                .toArray(String[]::new);
        //设置坐标
        xAxis.setValueFormatter(new IAxisValueFormatter()
        {
            private final String[] mActivities = newKeyArray;

            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                if(mActivities.length != 0)
                {
                    return mActivities[(int) value % mActivities.length];
                }
                else
                {
                    return "0";
                }
            }
        });
        xAxis.setTextColor(Color.BLACK);
        //设置y轴
        YAxis yAxis = chart.getYAxis();
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(12f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(30f);
        yAxis.setDrawLabels(false);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM); // 设置图例垂直对齐方式为底部
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setYOffset(50f);
        l.setXEntrySpace(10f);
        l.setYEntrySpace(0f);
        l.setTextColor(Color.BLACK);
        l.setTextSize(12f);
        setsData(chart,map);
    }
    private static void setsData(RadarChart radar,Map<String,Integer> map)
    {
        float mul = 80;
        float min = 20;
        int cnt = 5;
        ArrayList<RadarEntry> entries1 = new ArrayList<>();
        ArrayList<RadarEntry> entries2 = new ArrayList<>();
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry1 = iterator.next();
            if (iterator.hasNext()) {
                Map.Entry<String, Integer> entry2 = iterator.next();
                Integer value1 = entry1.getValue();
                Integer value2 = entry2.getValue();
                entries1.add(new RadarEntry(value1));
                entries2.add(new RadarEntry(value2));
            }
        }
        RadarDataSet set1 = new RadarDataSet(entries1, "上周");
        set1.setColor(Color.rgb(189, 221, 255));
        set1.setFillColor(Color.rgb(189, 221, 255));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);
        RadarDataSet set2 = new RadarDataSet(entries2, "本周");
        set2.setColor(Color.rgb(198, 239, 206));
        set2.setFillColor(Color.rgb(198, 239, 206));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);
        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set1);
        sets.add(set2);
        RadarData data = new RadarData(sets);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);
        radar.setData(data);
        radar.invalidate();
    }
}

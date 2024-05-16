package com.example.homework.chart;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class mypie
{
    public static void setpie(PieChart pie,Map<String, String> map)
    {
        setsData(map,pie);
    }
    private static void setsData(Map<String,String> map,PieChart pie) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet())
        {
            Integer key = (int)(Math.round(Double.parseDouble(entry.getKey())*100));
            String value = entry.getValue();
            entries.add(new PieEntry(key,value));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 20));
        dataSet.setSelectionShift(5f);
        pie.animateXY(1400, 1400);
        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        // 设置 value lines 的属性
        dataSet.setDrawValues(true); // 设置显示值
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setValueLineColor(Color.BLACK); // value line 的颜色
        dataSet.setValueLineWidth(2f); // value line 的宽度
        PieData pieData = new PieData(dataSet);
        pie.setData(pieData);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        pie.setData(data);
        pie.highlightValues(null);
        Description description = pie.getDescription();
        Legend legend = pie.getLegend();
        legend.setTextSize(12f); // 设置字体大小为 12sp
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP); // 设置垂直对齐方式为顶部
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); // 设置水平对齐方式为右侧
        legend.setOrientation(Legend.LegendOrientation.VERTICAL); // 设置图例条目垂直排列
        legend.setXOffset(-70f); // 设置水平方向上的偏移量
        // 隐藏描述标签
        description.setEnabled(false);
        // 不显示扇形上的键
        pie.setDrawEntryLabels(false);
        pie.invalidate();
    }
}

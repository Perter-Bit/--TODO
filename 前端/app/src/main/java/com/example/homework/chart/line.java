package com.example.homework.chart;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class line {

    private LineChart lineChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    private LineData lineData;
    private LineDataSet lineDataSet;
    private List<ILineDataSet> lineDataSets = new ArrayList<>();
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
    private List<String> timeList = new ArrayList<>(); //存储x轴的时间
    //一条曲线
    public line(LineChart mLineChart, String name, int color) {
        this.lineChart = mLineChart;
        leftAxis = lineChart.getAxisLeft();
        rightAxis = lineChart.getAxisRight();
        xAxis = lineChart.getXAxis();
        initLineChart();
        initLineDataSet(name, color);
    }
    /**
     * 初始化LineChar
     */
    private void initLineChart() {

        lineChart.setDrawGridBackground(false);
        //显示边界
        lineChart.setDrawBorders(true);
        //折线图例 标签 设置
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1.5f);
        xAxis.setLabelCount(10);
        xAxis.setYOffset(2f); // 将标签向下移动10个像素
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return timeList.get((int) value % timeList.size());
            }
        });
        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
        // 设置数据到图表中
        lineChart.setData(lineData);
        // 创建一个空的描述对象
        Description description = new Description();
        // 将描述信息设置为空字符串
        description.setText("");
        // 将描述信息应用于图表
        lineChart.setDescription(description);
        lineChart.invalidate(); // 使图表无效以刷新
    }

    /**
     * 初始化折线(一条线)
     *
     * @param name
     * @param color
     */
    private void initLineDataSet(String name, int color) {

        lineDataSet = new LineDataSet(null,null);
        lineDataSet.setLineWidth(1.5f);
        lineDataSet.setCircleRadius(1.5f);
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setHighLightColor(color);
        //设置曲线填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //添加一个空的 LineData
        // 禁用值的绘制
        lineDataSet.setDrawValues(false);
        lineData = new LineData();
        lineChart.setData(lineData);
        lineChart.invalidate();
    }
    /**
     * 动态添加数据（一条折线图）
     *
     * @param number
     */
    public void addEntry(int number,String key)
    {
        //最开始的时候才添加 lineDataSet（一个lineDataSet 代表一条线）
        if (lineDataSet.getEntryCount() == 0) {
            lineData.addDataSet(lineDataSet);
        }
        lineChart.setData(lineData);
        //避免集合数据过多，及时清空（做这样的处理，并不知道有没有用，但还是这样做了）
        if (timeList.size() > 11) {
            timeList.clear();
        }
        System.out.println(key);
        timeList.add(key);

        Entry entry = new Entry(lineDataSet.getEntryCount(), number);
        lineData.addEntry(entry, 0);
        //通知数据已经改变
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();
        //设置在曲线图中显示的最大数量
        lineChart.setVisibleXRangeMaximum(10);
        //移到某个位置
        lineChart.moveViewToX(lineData.getEntryCount() - 5);
    }

    /**
     * 设置Y轴值
     *
     * @param max
     * @param min
     * @param labelCount
     */
    public void setYAxis(float max, float min, int labelCount) {
        if (max < min) {
            return;
        }
        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
        leftAxis.setLabelCount(labelCount, false);

        rightAxis.setAxisMaximum(max);
        rightAxis.setAxisMinimum(min);
        rightAxis.setLabelCount(labelCount, false);
        lineChart.invalidate();
    }

    /**
     * 设置高限制线
     *
     * @param high
     * @param name
     */
    public void setHightLimitLine(float high, String name, int color) {
        if (name == null) {
            name = "高限制线";
        }
        LimitLine hightLimit = new LimitLine(high, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        hightLimit.setLineColor(color);
        hightLimit.setTextColor(color);
        leftAxis.addLimitLine(hightLimit);
        lineChart.invalidate();
    }

    /**
     * 设置低限制线
     *
     * @param low
     * @param name
     */
    public void setLowLimitLine(int low, String name) {
        if (name == null) {
            name = "低限制线";
        }
        LimitLine hightLimit = new LimitLine(low, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        leftAxis.addLimitLine(hightLimit);
        lineChart.invalidate();
    }
}
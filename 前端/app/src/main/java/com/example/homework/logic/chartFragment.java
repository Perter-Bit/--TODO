package com.example.homework.logic;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.example.homework.R;
import com.example.homework.chart.line;
import com.example.homework.chart.mypie;
import com.example.homework.chart.myradar;
import com.example.homework.utils.HttpGetRequest;
import com.example.homework.utils.HttpPostRequestAsyncTask;
import com.example.homework.utils.OnResponseReceived;
import com.example.homework.utils.RequestData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

public class chartFragment extends Fragment
{
    private line dynamicLineChartManager1;
    private List<Integer> lists = new ArrayList<>(); //数据集合
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PieChart pie;
    List<PieEntry> list;
    private RadarChart radar;
    List<RadarEntry> Radarlist1;
    List<RadarEntry> Radarlist2;
    private String mParam1;
    private String mParam2;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;
    private TextView text6;
    public chartFragment() {
        // Required empty public constructor
    }
    public static chartFragment newInstance(String param1, String param2) {
        chartFragment fragment = new chartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);
        super.onCreate(savedInstanceState);
        pie = (PieChart) rootView.findViewById(R.id.pie);
        radar = (RadarChart) rootView.findViewById(R.id.radar);
        text1 = (TextView) rootView.findViewById(R.id.chart_01);
        text2 = (TextView) rootView.findViewById(R.id.chart_02);
        text3 = (TextView) rootView.findViewById(R.id.chart_03);
        text4 = (TextView) rootView.findViewById(R.id.chart_04);
        text5 = (TextView) rootView.findViewById(R.id.chart_05);
        text6 = (TextView) rootView.findViewById(R.id.chart_06);
        LineChart mChart1 = (LineChart) rootView.findViewById(R.id.dynamic_chart1);
        dynamicLineChartManager1 = new line(mChart1, "数据", Color.WHITE);
        dynamicLineChartManager1.setYAxis(800, 0, 10);
        init();
        initpie();
        initrader();
        initline();
        return rootView;
    }
    private void initpie()
    {
        Map<String, String> map = new HashMap<>();
        RequestData data = new RequestData(map,"/detail/pie");
        HttpPostRequestAsyncTask task = new HttpPostRequestAsyncTask(new OnResponseReceived() {
            @Override
            public void onPostResponse(Response response) {
                try {
                    JSONObject data = new JSONObject(response.body().string());
                    JSONObject mydata = (JSONObject) data.get("data");
                    // 创建一个空的Map来存储JSONObject的键值对
                    Map<String, String> map = new HashMap<>();
                    System.out.println(mydata);
                    Iterator<String> keys = mydata.keys();
                    // 遍历迭代器，将键值对添加到 Map 中
                    while (keys.hasNext()) {
                        String key = keys.next();
                        String value = mydata.getString(key);
                        map.put(key, value);
                    }
                    // 在新线程中执行更新 UI 的操作
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // 更新UI组件
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mypie.setpie(pie, map);
                                }
                            });
                        }
                    }).start();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onPostResponseError(Exception e) {

            }
        });
        task.execute(data);
    }
    private void initrader()
    {
        Map<String, String> map = new HashMap<>();
        RequestData data = new RequestData(map,"/detail/radar");
        HttpPostRequestAsyncTask task = new HttpPostRequestAsyncTask(new OnResponseReceived()
        {
            @Override
            public void onPostResponse(Response response)
            {
                try {
                    JSONObject data = new JSONObject(response.body().string());
                    JSONObject mydata = (JSONObject) data.get("data");
                    // 创建一个空的Map来存储JSONObject的键值对
                    Map<String, Integer> map = new HashMap<>();
                    System.out.println(mydata);
                    Iterator<String> keys = mydata.keys();
                    // 遍历迭代器，将键值对添加到 Map 中
                    while (keys.hasNext())
                    {
                        String key = keys.next();
                        Integer value = Integer.parseInt(mydata.getString(key));
                        map.put(key, value);
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // 使用Handler将更新操作发布到主线程
                            // 更新UI组件
                            ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myradar.setradar(radar,getContext(),map);
                                }
                            });
                        }
                    }).start();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onPostResponseError(Exception e)
            {
            }
        });

        task.execute(data);
    }
    private void initline()
    {
        Map<String, String> map = new HashMap<>();
        RequestData data = new RequestData(map,"/detail/line");
        HttpPostRequestAsyncTask task = new HttpPostRequestAsyncTask(new OnResponseReceived()
        {
            @Override
            public void onPostResponse(Response response)
            {
                try {
                    JSONObject data = new JSONObject(response.body().string());
                    JSONObject mydata = (JSONObject) data.get("data");
                    Map<String, Integer> map = new HashMap<>();
                    Iterator<String> keys = mydata.keys();
                    // 遍历迭代器，将键值对添加到 Map 中
                    new Thread(new Runnable() {
                        @Override
                        public void run()
                        {
                            while (keys.hasNext())
                            {
                                String key = keys.next();
                                Integer value = null;
                                try {
                                    value = Integer.parseInt(mydata.getString(key));
                                    dynamicLineChartManager1.addEntry((int)value,key);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }).start();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onPostResponseError(Exception e)
            {
            }
        });
        task.execute(data);
    }
    private void init()
    {
        Map<String, String> map = new HashMap<>();
        map.put("day","14");
        RequestData data = new RequestData(map,"/detail/statistics");
        HttpPostRequestAsyncTask task = new HttpPostRequestAsyncTask(new OnResponseReceived()
        {
            @Override
            public void onPostResponse(Response response)
            {
                JSONObject data = null;
                try {
                    data = new JSONObject(response.body().string());
                    JSONObject mydata = (JSONObject) data.get("data");
                    // 创建一个空的Map来存储JSONObject的键值对
                    Map<String, String> map = new HashMap<>();
                    System.out.println(mydata);
                    Iterator<String> keys = mydata.keys();
                    // 遍历迭代器，将键值对添加到 Map 中
                    while (keys.hasNext()) {
                        String key = keys.next();
                        String value = mydata.getString(key);
                        map.put(key, value);
                    }
                    text1.setText(map.get("累计"));
                    text2.setText(map.get("累计时长"));
                    text3.setText(map.get("日均时长"));
                    text4.setText(map.get("今日次数"));
                    text5.setText(map.get("今日时长"));
                    text6.setText(map.get("放弃次数"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onPostResponseError(Exception e)
            {
            }
        });
        task.execute(data);
    }
}

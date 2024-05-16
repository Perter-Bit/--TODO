package com.example.homework.logic;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homework.R;
import com.example.homework.entity.Affair;
import com.example.homework.entity.Detail;
import com.example.homework.utils.GetResponseListener;
import com.example.homework.utils.HttpGetRequestAsyncTask;
import com.example.homework.utils.HttpPostRequest;
import com.example.homework.utils.HttpPostRequestAsyncTask;
import com.example.homework.utils.OnResponseReceived;
import com.example.homework.utils.RequestData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TimerDialog extends Dialog {
    public static Affair affair = null;
    private ImageView backgroundImageView;
    private TextView timerTextView;
    private Button pauseButton;
    private Button leaveButton;
    private Timer timer;
    private long startTime;
    private long elapsedTime = 0;
    private boolean isTimerRunning = false;
    private final Handler handler = new UpdateTimeHandler(this);
    private static final long THRESHOLD_TIME = 10 * 60 * 1000; // 10 minutes
    public TimerDialog(Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_timer);
        // Initialize viewsve
        backgroundImageView = findViewById(R.id.backgroundImageView);
        timerTextView = findViewById(R.id.timerTextView);
        pauseButton = findViewById(R.id.pauseButton);
        leaveButton = findViewById(R.id.leaveButton);
        switch (affair.imageId) {
            case 1:
                backgroundImageView.setImageResource(R.drawable.bp1);
                break;
            case 2:
                backgroundImageView.setImageResource(R.drawable.bp2);
                break;
            case 3:
                backgroundImageView.setImageResource(R.drawable.bp3);
                break;
            case 4:
                backgroundImageView.setImageResource(R.drawable.bp4);
                break;
            case 5:
                backgroundImageView.setImageResource(R.drawable.bp5);
                break;
            case 6:
                backgroundImageView.setImageResource(R.drawable.bp6);
                break;
            case 7:
                backgroundImageView.setImageResource(R.drawable.bp7);
                break;
            case 8:
                backgroundImageView.setImageResource(R.drawable.bp8);
                break;
            case 9:
                backgroundImageView.setImageResource(R.drawable.bp9);
                break;
            case 10:
                backgroundImageView.setImageResource(R.drawable.bp10);
                break;
        }
        // Set click listeners
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseOrResumeTimer();
            }
        });
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String is_over;
                if(affair.type == 0 && affair.time * 1000 * 60 > elapsedTime)
                {
                    //倒计时
                    is_over = "0";
                }
                else
                {
                    is_over = "1";
                }
                Map<String, String> map = new HashMap<>();
                map.put("is_over", is_over);
                map.put("message", String.valueOf(affair.message));
                map.put("date", String.valueOf(System.currentTimeMillis()));
                map.put("time",String.valueOf(elapsedTime/1000));//记录为秒
                RequestData data = new RequestData(map,"/detail/add");
                HttpGetRequestAsyncTask task = new HttpGetRequestAsyncTask(new GetResponseListener() {
                    @Override
                    public void onGetResponse(Response response) {
                        JSONObject data = null;
                        try {
                            data = new JSONObject(response.body().string());
                            String message = data.getString("message");
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onGetResponseError(Exception e) {

                    }
                });
                task.execute(data);
                stopTimer();
                dismiss();
            }
        });
        startTimer();
    }
    // Handler to update timer text and check time threshold
    private static class UpdateTimeHandler extends Handler {
        private final WeakReference<TimerDialog> dialogWeakReference;

        UpdateTimeHandler(TimerDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TimerDialog dialog = dialogWeakReference.get();
            if (dialog != null) {
                dialog.updateTimerText(dialog.formatTime(dialog.elapsedTime));
                dialog.elapsedTime += 1000;
                if (dialog.elapsedTime == 0 && affair.type ==0) {
                    dialog.stopTimer();
                    dialog.showTimeUpAlert();
                }
            }
        }
    }
    private String formatTime(long time) {
        time+=1000;
        if(affair.type == 0){
            time = affair.time * 60 * 1000 -time;
        }
        return String.format("%02d:%02d:%02d",
                time / 3600000,
                (time % 3600000) / 60000,
                (time  % 60000) / 1000);
    }
    // Start the timer
    private void startTimer()
    {
        if (!isTimerRunning)
        {
            timer = new Timer();
            startTime = System.currentTimeMillis();
            //及时
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            }, 0, 1000); // Update every second
            isTimerRunning = true;
        }
    }
    private void pauseOrResumeTimer() {
        if (isTimerRunning) {
            timer.cancel();
            isTimerRunning = false;
        } else {
            startTimer();
        }
    }
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            isTimerRunning = false;
            elapsedTime = 0;
        }
    }

    // Method to update timer text
    public void updateTimerText(String time) {
        timerTextView.setText(time);
    }

    // Method to show alert when time is up
    private void showTimeUpAlert() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(getContext(), notification);
        ringtone.play();
    }
}

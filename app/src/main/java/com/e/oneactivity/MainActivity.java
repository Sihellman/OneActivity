package com.e.oneactivity;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity implements SimpleCountDownTimer.OnCountDownListener {
    public static BigDecimal checkFrequency;

    public static final int TEXT_REQUEST = 1;
    public static NotificationManager mNotifyManager;
    public static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private BigDecimal frequency;
    private BigDecimal pagesLeft;
    private SimpleCountDownTimer simpleCountDownTimer;
    private int chosenMin;
    private int chosenSec;
    private int secPerPage;
    private int minPerPage;
    private TextView viewPages;
    private TextView inquiry;
    private TextView textView;
    private Button start;
    private Button pause;
    private Button resume;
    private Button submit;
    private EditText pagesRead;
    private TextView howManyPages;
    private EditText pagesToRead;
    private Button set;
    private NumberPicker picker1;
    private NumberPicker picker2;
    private NumberPicker picker3;
    private NumberPicker picker4;
    private String[] pickerVals1;
    private String[] pickerVals2;
    private String valPicker1 = "0";
    private String valPicker2 = "0";
    private String valPicker3 = "0";
    private String valPicker4 = "0";
    private TextView howLongPerPage;
    private Button launchStopWatch;
    private TextView youWouldLike;
    private TextView afterEvery1;
    private TextView afterEvery2;
    private EditText frequencyEdit;
    private Button backToSettings;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inquiry = findViewById(R.id.inquiry);
        pagesRead = findViewById(R.id.pages_read);
        submit = findViewById(R.id.submit_pages);
        howLongPerPage = findViewById(R.id.how_long_per_page);
        launchStopWatch = findViewById(R.id.launchStopWatch);
        youWouldLike = findViewById(R.id.You_would_like);
        afterEvery1 = findViewById(R.id.after_every1);
        afterEvery2 = findViewById(R.id.after_every2);
        frequencyEdit = findViewById(R.id.frequency);
        picker1 = findViewById(R.id.minPick);
        picker2 = findViewById(R.id.minPick2);
        picker3 = findViewById(R.id.secPick);
        picker4 = findViewById(R.id.secPick2);
        picker1.setMaxValue(5);
        picker1.setMinValue(0);
        picker2.setMaxValue(9);
        picker2.setMinValue(0);
        picker3.setMaxValue(5);
        picker3.setMinValue(0);
        picker4.setMaxValue(9);
        picker4.setMinValue(0);
        pickerVals1  = new String[] {"0", "1", "2", "3", "4", "5"};
        picker1.setDisplayedValues(pickerVals1);
        pickerVals2 = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        picker2.setDisplayedValues(pickerVals2);
        picker3.setDisplayedValues(pickerVals1);
        picker4.setDisplayedValues(pickerVals2);
        picker1.setOnValueChangedListener((numberPicker, i, i1) -> {
            int valuePicker1 = picker1.getValue();
            valPicker1 = pickerVals1[valuePicker1];
            Log.d("picker value", valPicker1);
        });

        picker2.setOnValueChangedListener((numberPicker, i, i1) -> {
            int valuePicker1 = picker2.getValue();
            valPicker2 = pickerVals2[valuePicker1];
        });
        picker3.setOnValueChangedListener((numberPicker, i, i1) -> {
            int valuePicker1 = picker3.getValue();
            valPicker3 = pickerVals1[valuePicker1];
        });
        picker4.setOnValueChangedListener((numberPicker, i, i1) -> {
            int valuePicker1 = picker4.getValue();
            valPicker4 = pickerVals2[valuePicker1];
        });


        String pagesStr = "99";
        viewPages = findViewById(R.id.pagesLeft);
        viewPages.setText(pagesStr);
        pagesLeft = new BigDecimal(pagesStr);

        String frequencyString = "1";

        String minuteStr = "0";

        howManyPages = findViewById(R.id.how_many_pages);
        pagesToRead = findViewById(R.id.pages_to_read);
        set = findViewById(R.id.set_settings);


        start = findViewById(R.id.startBtn);
        resume = findViewById(R.id.resumeBtn);
        pause = findViewById(R.id.pauseBtn);

        resume.setEnabled(false);
        set.setOnClickListener(view -> {
            chosenMin = parseInt(valPicker1 + valPicker2);
            minPerPage = chosenMin;
            String secondStr = "2";
            chosenSec = parseInt(valPicker3+valPicker4);
            secPerPage = chosenSec;
            frequency = new BigDecimal(frequencyEdit.getText().toString());

            checkFrequency = frequency;
            BigDecimal multipliedMin = frequency.multiply(new BigDecimal(chosenMin));
            BigDecimal multipliedSec = frequency.multiply(new BigDecimal(chosenSec));
            BigDecimal START_TIME_IN_MILLIS = (multipliedSec.multiply(new BigDecimal(1000))).add(multipliedMin.multiply(new BigDecimal(1000)).multiply(new BigDecimal(60)));


            BigDecimal rounded = START_TIME_IN_MILLIS.setScale(2, BigDecimal.ROUND_HALF_UP);
            double roundedDouble = rounded.doubleValue();
            chosenMin = (int) (((roundedDouble / 1000) / 60) % 60);
            chosenSec = (int) (roundedDouble / 1000) % 60;
            simpleCountDownTimer = new SimpleCountDownTimer(chosenMin, chosenSec, 1, this);
            textView = findViewById(R.id.tv);
            textView.setText(simpleCountDownTimer.getCountDownTime());

            howManyPages.setVisibility(View.INVISIBLE);
            viewPages.setText(pagesToRead.getText());
            pagesToRead.setVisibility(View.INVISIBLE);
            set.setVisibility(View.INVISIBLE);
            picker1.setVisibility(View.INVISIBLE);
            picker2.setVisibility(View.INVISIBLE);
            picker3.setVisibility(View.INVISIBLE);
            picker4.setVisibility(View.INVISIBLE);
            howLongPerPage.setVisibility(View.INVISIBLE);
            launchStopWatch.setVisibility(View.INVISIBLE);
            youWouldLike.setVisibility(View.INVISIBLE);
            afterEvery1.setVisibility(View.INVISIBLE);
            afterEvery2.setVisibility(View.INVISIBLE);
            frequencyEdit.setVisibility(View.INVISIBLE);
            viewPages.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            pause.setVisibility(View.VISIBLE);
            start.setVisibility(View.VISIBLE);
            resume.setVisibility(View.VISIBLE);
            backToSettings.setVisibility(View.VISIBLE);


        });

        start.setOnClickListener(view -> {
            simpleCountDownTimer.start(false);

            start.setEnabled(false);

        });

        resume.setOnClickListener(view -> {
            simpleCountDownTimer.start(true);
            simpleCountDownTimer.runOnBackgroundThread();
        });
        pause.setOnClickListener(view -> {
            simpleCountDownTimer.pause();
            simpleCountDownTimer.setTimerPattern("s");
            resume.setEnabled(true);
        });
        submit.setOnClickListener(view -> {
            if (pagesRead.getText().toString().matches("")) {

            } else {
                pagesLeft = pagesLeft.subtract(new BigDecimal(pagesRead.getText().toString()));
                if (pagesLeft.compareTo(BigDecimal.ZERO) <= 0) {
                    launchCongratulations();
                } else {
                    //Toast.makeText(this, "else reached", Toast.LENGTH_SHORT).show();
                    textView.setVisibility(View.VISIBLE);
                    start.setVisibility(View.VISIBLE);
                    resume.setVisibility(View.VISIBLE);
                    pause.setVisibility(View.VISIBLE);
                    viewPages.setVisibility(View.VISIBLE);
                    inquiry.setVisibility(View.INVISIBLE);
                    pagesRead.setVisibility(View.INVISIBLE);
                    submit.setVisibility(View.INVISIBLE);
                }
            }
        });


    }
    @Override
    public void onBackPressed(){
        moveTaskToBack(true);



    }


    @Override
    public void onCountDownActive(String time) {

        textView.post(() -> textView.setText(time));

        Toast.makeText(this, "Seconds = " + simpleCountDownTimer.getSecondsTillCountDown() + " Minutes=" + simpleCountDownTimer.getMinutesTillCountDown(), Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onCountDownFinished() {
        textView.post(() -> {
            //textView.setText("Finished");
            start.setEnabled(true);
            resume.setEnabled(false);
            textView.setVisibility(View.INVISIBLE);
            start.setVisibility(View.INVISIBLE);
            resume.setVisibility(View.INVISIBLE);
            pause.setVisibility(View.INVISIBLE);
            viewPages.setVisibility(View.INVISIBLE);
            inquiry.setVisibility(View.VISIBLE);
            pagesRead.setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);
            createNotification();


        });


    }


    public void launchCongratulations() {
        Intent intent = new Intent(this, Congratulations.class);
        startActivity(intent);
    }

    private void createNotification() {
        mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Reading Rhythm Notification", NotificationManager
                    .IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Reading Rhythm Setter");
            mNotifyManager.createNotificationChannel(notificationChannel);

        }
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, 0);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)

                .setContentText("Check your progress")
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_android);
        if (checkFrequency.compareTo(BigDecimal.ONE) == 1) {
            notifyBuilder.setContentTitle("your allotted time to read " + checkFrequency + " pages has run out");
        } else {
            notifyBuilder.setContentTitle("your allotted time to read the page has run out");
        }
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());


    }
}


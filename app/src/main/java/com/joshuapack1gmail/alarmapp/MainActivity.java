package com.joshuapack1gmail.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private TextView mTextView;
    private EditText editTextTitle;
    private EditText editTextMessage;
    private Button buttonChannel1;
    private Button buttonChannel2;

    private NotificationHelper mNotificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.textViewAlarmSet);

        editTextTitle = findViewById(R.id.edittext_title);
        editTextMessage = findViewById(R.id.edittext_message);
        buttonChannel1 = findViewById(R.id.button_channel1);
        buttonChannel2 = findViewById(R.id.button_channel2);

        mNotificationHelper = new NotificationHelper(this);

        Button buttonCancelAlarm = findViewById(R.id.button_cancel);
        buttonCancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });

        buttonChannel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOnChannel1(editTextTitle.getText().toString(), editTextMessage.getText().toString());
            }
        });

        buttonChannel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOnChannel2(editTextTitle.getText().toString(), editTextMessage.getText().toString());
            }
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        updateTimeText(c);
        startAlarm(c);

        TextView textView = findViewById(R.id.textView);

        //hourOfDay comes in 24hr format, so this adjusts it to 12hr format
        //also determines AM, PM or neither if in 24hr format
        int hourAdjustment = 0;
        String amOrpm = "";

        if(!DateFormat.is24HourFormat(this)){
            if(hourOfDay>12){
                hourAdjustment = 12;
                amOrpm = "pm";
            } else if(hourOfDay == 12){
                amOrpm = "pm";
            } else if(hourOfDay == 0){
                hourAdjustment = -12;
                amOrpm = "am";
            } else{
                amOrpm = "am";
            }
        }

        textView.setText("Hour: " + (hourOfDay-hourAdjustment) + " Minute: " + minute + " " + amOrpm);
    }

    public void sendOnChannel1(String title, String message){
        NotificationCompat.Builder nb = mNotificationHelper.getChannel1Notification(title, message);
        mNotificationHelper.getManager().notify(1,nb.build());
    }

    public void sendOnChannel2(String title, String message){
        NotificationCompat.Builder nb = mNotificationHelper.getChannel2Notification(title, message);
        mNotificationHelper.getManager().notify(2,nb.build());
    }

    public void playGMusic(View v){
        Intent intent = new Intent("com.android.music.musicservicecommand");
        intent.putExtra("command", "play");
        sendBroadcast(intent);
    }

    public void pauseGMusic(View v){
        Intent intent = new Intent("com.android.music.musicservicecommand");
        intent.putExtra("command", "pause");
        sendBroadcast(intent);
    }

    private void updateTimeText(Calendar c){
        String timeText = "Alarm set for: ";
        timeText += java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(c.getTime());

        mTextView.setText(timeText);
    }

    private void startAlarm(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        mTextView.setText("Alarm canceled");
    }
}

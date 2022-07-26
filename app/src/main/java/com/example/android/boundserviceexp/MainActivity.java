package com.example.android.boundserviceexp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.boundserviceexp.boundService.MyService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "azeem";
    private TextView textView;
    private ProgressBar progressBar;
    private ScrollView mScroll;
    private final String[] songs = {
            "mere bina tu", "main tera hero", "sab tera"
    };

    private MyService mMyService;
    private boolean mBound = false;
    private ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyService.MyServiceBinder myServiceBinder = (MyService.MyServiceBinder) iBinder;
            mMyService = myServiceBinder.getService();
            mBound = true;
            Log.d(TAG, "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        progressBar = findViewById(R.id.progressBar);
        mScroll = findViewById(R.id.scrollLog);
        findViewById(R.id.start).setOnClickListener(view -> {
            show("Intent Service started...");
            displayProgressBar(true);
            show(mMyService.getText());
//            for (String song:songs) {
//                startService(new Intent(this, MyIntentService.class).putExtra(MESSAGE_KEY,song));
//            }
        });
        findViewById(R.id.stop).setOnClickListener(view -> {
            textView.setText("Start Intent Service \n");
            displayProgressBar(false);
//            stopService(new Intent(this, MyIntentService.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(MainActivity.this, MyService.class);
        bindService(intent,mServiceConn,BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound){
            unbindService(mServiceConn);
            mBound = false;
        }


    }

    public void displayProgressBar(boolean display) {
        if (display) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void show(String message) {
        textView.append(message + "\n");
        scrollTextToEnd();
    }

    private void scrollTextToEnd() {
        mScroll.post(new Runnable() {
            @Override
            public void run() {
                mScroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
}
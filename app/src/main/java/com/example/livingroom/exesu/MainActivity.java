package com.example.livingroom.exesu;
// This application is tested on XiaoMi Hongmi note with android versioin 4.4.4
// This application is made to copy files under sytem/etc to the external sd card through java
// It calls native method to copy system/bin/* to external sd card
// The application requires the device to be rooted
// SuperSu might be needed to allow su commands

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.File;
import java.io.OutputStream;

public class MainActivity extends Activity implements View.OnClickListener{
    private static final String TAG = "MyOwn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TAG,"going to execute");
        Button button1 = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button2);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }
    public static void execCommand()  {
        try {
            Log.v(TAG,"executing");
            // since the device is rooted, i am able to execute as super user now, i am copying everything inside system/etc  to sdcard
            // I have to allow this action from SuperSU also
            Process su = Runtime.getRuntime().exec("su");
            OutputStream os = su.getOutputStream();
            // The only rooted device I have in my hand is XiaoMi HongMi note running Android 4.4.4, by default,
            // it's external sd card's path is /storage/sdcard1, otherwise write to default path sdcard0
            if(new File("/storage/sdcard1/").exists())
            {
                os.write("cp -rf /system/etc/*.*  /storage/sdcard1".getBytes());
            }
            else
            {
                os.write("cp -rf /system/etc/*.*  /storage/sdcard0".getBytes());
            }
            os.flush();
            os.close();

        } catch (Exception e) {
            // for debug purpose
            Log.v(TAG,"Excaption throws" + e.getStackTrace());
            Log.v(TAG,"Excaption throws" + e.getMessage());
            e.printStackTrace();
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.button: {
               // when button1 clicked, execute through the android api
                execCommand();
                ((TextView) findViewById(R.id.textView)).setText("Executed through Java");
                break;
            }

            case R.id.button2: {
                // Execute  "su;cp /system/bin/*  /storage/sdcard/;exit" through native function getMsgFromJni() in my-jni.c
                // Set the text after execution to prompt user
                ((TextView) findViewById(R.id.textView)).setText(getMsgFromJni());
                break;
            }

        }
    }
    // telling system to load the native code
    static {
        System.loadLibrary("my-jni");
    }
    // declare the method, make the method visaible from java
    public native String getMsgFromJni();

}

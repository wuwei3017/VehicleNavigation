package com.graduation.chary.mynavi;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.speech.RecognitionListener;
import android.widget.Toast;

import com.graduation.chary.asr.AsrDialog;

import java.util.ArrayList;

import chary.nyist.com.baidumapdemo.NaviUtils;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    NaviUtils naviUtils = new NaviUtils(MainActivity.this);
    AsrDialog dialog = new AsrDialog(MainActivity.this);

    private Button mDb06ll = null;
    private Button btn = null;
    private SpeechRecognizer speechRecognizer;
//    private AsrDialog.SpeechRecognizerCallBack callBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDb06ll = (Button) findViewById(R.id.button);
        btn = (Button)findViewById(R.id.btn_asr);

        initPermission();

       initListener();

        if (naviUtils.initDirs()) {
            naviUtils.initNavi();
        }
    }

    private void initListener() {

        mDb06ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                naviUtils.naviEnable();
            }
        });

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.Start();
            }
        });
    }
    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.RECORD_AUDIO

        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }
}
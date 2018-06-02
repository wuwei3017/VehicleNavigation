package com.graduation.chary.mynavi;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;


import com.baidu.aip.unit.UNITUtils;
import com.graduation.chary.asr.AsrDialog;
import com.graduation.chary.asr.TTS;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import chary.nyist.com.baidumapdemo.BNDemoGuideActivity;
import chary.nyist.com.baidumapdemo.MapActivity;

import chary.nyist.com.baidumapdemo.NaviUtils;
import chary.nyist.com.baidumapdemo.RouteNode;

import static java.lang.Thread.sleep;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    NaviUtils naviUtils = new NaviUtils(MainActivity.this);
    private Handler asrhandler = null;
    private Handler gpshandler = null;

    TTS tts = null;

    private Button mDb06ll = null;
    private CheckBox btn = null;
    private SpeechRecognizer speechRecognizer;
    private Button btn_loc = null;
    private double latitude = 32.9675;
    private double longitude = 112.5522;
    private String cityString = null;

    RouteNode sNode ;
    RouteNode eNode ;
    RouteNode mNode ;
    RouteNode node = new RouteNode(0, 0);
    private EditText editend = null;
    private EditText editstart = null;
    private String start = null;
    private String end = null;
    private String mid ;
    String chatmsg = null;
    UNITUtils unitUtils ;

    HttpUtils httpUtils;
    Bot bot = new Bot();
    Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    private Button btn_chat = null;
    private boolean is_asr = false;
    boolean isAlive = false;
    private Handler unithandler;
    private Map<String, String> slotmap;
    private String address = "";
    private TCP_service tcpServer = null;
    private boolean button = false;
    private boolean is_navi = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mDb06ll = (Button) findViewById(R.id.btn_navi);
        btn = (CheckBox) findViewById(R.id.btn_set);
        btn_loc = (Button) findViewById(R.id.btn_loc);
        editstart = (EditText) findViewById(R.id.editstart);
        editend = (EditText) findViewById(R.id.editend);
        btn_chat = (Button) findViewById(R.id.btn_asr);
        editstart.setText("南阳理工学院");//设置默认数据 为当前地址
        //临时定义的当前位置经纬度
        sNode = new RouteNode(112.5522, 32.9675);
        eNode = new RouteNode(116.40386525193937, 39.915160800132085);


        //监听语音消息
        asrhandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    Handler_asrrcv(msg);
                }
            }
        };
        //监听GPS数据
        gpshandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    Handler_gpsrcv(msg);
                }
            }
        };

        unithandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 2) {
                    Handler_unitrcv(msg);
                }
            }
        };

        initTcpServer();
        initPermission();
        unitUtils = new UNITUtils(unithandler);
        tts = new TTS(this, asrhandler);

        tts.initialEnv();
        tts.initialTts();

        initListener();

        unitUtils.initAccessToken();

        if (naviUtils.initDirs()) {
            naviUtils.initNavi();
        }
    }

    private void initTcpServer() {
        if (tcpServer == null) {
            tcpServer = new TCP_service(gpshandler, 8080);
            tcpServer.start();
        }
    }

    /**
     * UNIT消息处理
     * @param msg
     */
    private void Handler_unitrcv(Message msg) {
        sNode = new RouteNode(112.5522, 32.9675);//南阳理工学院
        Log.d(TAG, "result message: " + msg.obj.toString());
        tts.speak(msg.obj.toString());
        if (unitUtils.isRoute_satisfy()) {
            slotmap = unitUtils.getMap();
            if (slotmap == null) {
                return;
            }
            if (slotmap.containsKey("user_end")){
                eNode =  getLatLng(slotmap.get("user_end"));
                if (slotmap.containsKey("user_start")){
                    sNode = getLatLng(slotmap.get("user_start"));
                    if (slotmap.containsKey("user_middle")){
                        //设置途经点
                        mNode = getLatLng(slotmap.get("user_middle"));
                        naviUtils.naviEnable(sNode, eNode, mNode);
                        return;
                    }
                }

                naviUtils.naviEnable(sNode, eNode);
            }
            start = slotmap.get("user_start");
//            Log.d(TAG, "start：" + start);\
//            if (end = slotmap.get("user_end"))

//            Toast.makeText(this, " 经度： " + sNode.getLatitude() + " " + eNode.getLatitude(), Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * GPS 数据处理
     *
     * @param msg
     */
    private void Handler_gpsrcv(Message msg) {
        String message = msg.obj.toString();
        Log.d(TAG, "message: " + message);
//        latitude = 0;
//        longitude = 0;

    }

    /**
     * 获取起点和终点
     */
    private boolean getTextFromEdit() {
        if (TextUtils.isEmpty(editstart.getText()) || TextUtils.isEmpty(editend.getText())) {
            Toast.makeText(this, "请将起点和终点输入完整", Toast.LENGTH_SHORT).show();
            return false;
        }
        start = editstart.getText().toString();
        end = editend.getText().toString();

        sNode = getLatLng(start);
        eNode = getLatLng(end);

        Toast.makeText(this, " 经度： " + sNode.getLatitude() + " " + eNode.getLatitude(), Toast.LENGTH_SHORT).show();
        return true;
    }

    /**
     * 语音消息处理
     *
     * @param msg
     */
    private void Handler_asrrcv(Message msg) {
        final String message = msg.obj.toString();
        Toast.makeText(MainActivity.this, "text : " + message, Toast.LENGTH_SHORT).show();

//        if (message.contains("打开导航")) {
//            naviUtils.naviEnable(sNode, eNode);
//            return;
//        }
//        if (message.contains("关闭导航")) {
//            BNDemoGuideActivity.instance.stopNavi();
//            return;
//        }
        if (message.contains("语音导航")) {
            if (message.contains("打开") || message.contains("进入")) {
                is_navi = true;
                return;
            }
            if(message.contains("关闭") || message.contains("退出")){
                is_navi = false;
                return;
            }
        }
        if (message.contains("关闭导航")) {
            BNDemoGuideActivity.instance.stopNavi();
            return;
        }
        if (message.contains("这是哪") || message.contains("我在哪")) {
            tts.speak(getAddress(latitude, longitude));
            Toast.makeText(MainActivity.this, convertAddress(this, latitude, longitude), Toast.LENGTH_SHORT).show();
            return;
        }

        if (is_navi){
            unitUtils.sendMessage(message);;


        //if (is_asr) {//语音聊天
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    chatmsg = bot.getData(message);
//                    Log.d(TAG, chatmsg);
//                }
//            });
//            thread.start();
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            Toast.makeText(MainActivity.this, "chatmessage : " + chatmsg, Toast.LENGTH_SHORT).show();
//            tts.speak(chatmsg);
//            try {
//                sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            tts.Stop();
//            is_asr = false;
        } else {

            if(true) {
                if (message.contains("打开定位")) {
                    MapActivity.latitude = latitude;
                    MapActivity.longitude = longitude;
                    String adr = getAddress(latitude, longitude);
                    Log.d(TAG, "add:  " + adr);
                    tts.speak(adr);
                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    startActivity(intent);
                    return;
                }
                if (message.contains("关闭定位")) {
                    MapActivity.instance.finish();
                    return;
                }

                if (message.contains("打开导航")) {
                    naviUtils.naviEnable(sNode, eNode);
                    return;
                }
//                if (message.contains("关闭导航")) {
//                    BNDemoGuideActivity.instance.stopNavi();
//                    return;
//                }
            }

            //聊天
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    chatmsg = bot.getData(message);
                    Log.d(TAG, chatmsg);
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Toast.makeText(MainActivity.this, "chatmessage : " + chatmsg, Toast.LENGTH_SHORT).show();
            tts.speak(chatmsg);
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 临时 转换地址
     *
     * @param context
     * @param latitude
     * @param longitude
     * @return
     */
    private static String convertAddress(Context context, double latitude, double longitude) {
        Geocoder mGeocoder = new Geocoder(context, Locale.getDefault());
        StringBuilder mStringBuilder = new StringBuilder();

        try {
            List<Address> mAddresses = mGeocoder.getFromLocation(latitude, longitude, 1);
            if (!mAddresses.isEmpty()) {
                Address address = mAddresses.get(0);
                mStringBuilder.append(address.getAdminArea()).append(", ").append(address.getLocality()).append(", ").append(address.getCountryName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mStringBuilder.toString();
    }

    /**
     * 临时 根据经纬度获取城市名称
     *
     * @return
     */
    String city() {
        List<Address> addList = null;
        Geocoder ge = new Geocoder(getApplicationContext());
        try {
            addList = ge.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (addList != null && addList.size() > 0) {
            for (int i = 0; i < addList.size(); i++) {
                Address ad = addList.get(i);
                cityString = ad.getLocality();
            }
        }
        return cityString;
    }


    //按键监听
    private void initListener() {

        mDb06ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (getTextFromEdit()) {
                    naviUtils.naviEnable(sNode, eNode);
                } else {

                }

            }
        });

        btn_loc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MapActivity.latitude = latitude;
                MapActivity.longitude = longitude;
                String adr = getAddress(latitude, longitude);
                tts.speak(adr);
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        btn_chat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(!button){
//                    is_asr = true;
                    tts.startWakeup();//唤醒测试
//                    button = true;
//                }else{
//                    is_asr = false;
//                    button = false;
//                    tts.Stop();
//                }

            }
        });
        btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
//                dialog.Start();//识别测试
//                tts.speak("在呢");//tts测试
                    tts.startWakeup();//唤醒测试
                } else {
                    tts.Stop();
                }
            }
        });

    }

    /**
     * 获取经纬度
     *
     * @param address
     */

    public synchronized RouteNode getLatLng(final String address) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                httpUtils = new HttpUtils();
                map = httpUtils.getLatAndLngByAddress(address);
                Log.d(TAG, map.get("lat").toString() + " " + map.get("lng").toString());
                // 将获取到的经纬度创建节点
                node = new RouteNode(Double.parseDouble(map.get("lat").toString()), Double.parseDouble(map.get("lng").toString()));
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return node;
    }

    public synchronized String getAddress(final double latitude, final double longitude) {
        Log.d(TAG, "线程获取地址");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                httpUtils = new HttpUtils();
                Log.d(TAG,  " 获取位置" );
                address = httpUtils.getAddressByLatlng(latitude, longitude);
                Log.d(TAG,  " address:" + address);

            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return address;
    }

        @Override
    protected void onDestroy() {
        super.onDestroy();
            tcpServer.setis_start(false);
            if(tcpServer!=null)
            {
                tcpServer.close();
                tcpServer=null;
            }
        tts.Stop();
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
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_FINE_LOCATION
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
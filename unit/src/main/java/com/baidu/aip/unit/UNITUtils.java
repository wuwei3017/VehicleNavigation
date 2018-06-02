package com.baidu.aip.unit;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baidu.aip.unit.exception.UnitError;
import com.baidu.aip.unit.listener.OnResultListener;
import com.baidu.aip.unit.model.AccessToken;
import com.baidu.aip.unit.model.CommunicateResponse;
import com.baidu.aip.unit.model.Scene;
import com.baidu.aip.unit.parser.CommunicateParser;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by LuakyChay on 2018/5/31.
 */

public class UNITUtils {


    private static final String TAG = "UNITUtils";
    private String accessToken;
    private List<String> waitList = new ArrayList<>();
    Scene curScene = new Scene(100, "聊天");
    private String sessionId = "";
    private int id = 0;
    Handler handler;
    String message = null;

    public boolean isRoute_satisfy() {
        return isRoute_satisfy;
    }

    public void setRoute_satisfy(boolean route_satisfy) {
        isRoute_satisfy = route_satisfy;
    }

    private boolean isRoute_satisfy = false;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public Map<String, String> map;

    public UNITUtils(Handler handler){
        this.handler = handler;
    }

    /**
     * UNIT
     * 为了防止破解app获取ak，sk，建议您把ak，sk放在服务器端。
     */
    public void initAccessToken() {
        APIService.getInstance().init(this);
        APIService.getInstance().initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                accessToken = result.getAccessToken();
                Log.i("MainActivity", "AccessToken->" + result.getAccessToken());
                if (!TextUtils.isEmpty(accessToken)) {
                    resendWaitList();//重发消息
                }

            }

            @Override
            public void onError(UnitError error) {
                Log.i("wtf", "AccessToken->" + error.getErrorMessage());
            }
        }, "AE7A8nI6CamyozdUhnTwUv8e", "ZC14YhKE9ihmQkcqKVeSZa1nyGVZSDtv");
//        }, "ruKQt3mup9uvn0NACO1Qm3V7", "ZzOlRNdKYBE7hBwaVO7qRd3euqoAveN5");
    }

    /**
     * 发送消息
     * @param message
     */
    public void sendMessage(String message) {
        curScene.setId(id);
        if (TextUtils.isEmpty(accessToken)) {
            waitList.add(message);
            return;
        }

        APIService.getInstance().communicate(new OnResultListener<CommunicateResponse>() {
            @Override
            public void onResult(CommunicateResponse result) {

                handleResponse(result);
            }

            @Override
            public void onError(UnitError error) {

            }
        }, 18133, message, sessionId);
        id++;
    }

    /**
     * 重发未发送成功的消息
     */
    private void resendWaitList() {
        for (String message : waitList) {
            sendMessage(message);
        }
    }

    private void handleResponse(CommunicateResponse result) {
        Random rand = new Random();
        String str = "";
        isRoute_satisfy = false;
        if (result != null) {
            sessionId = result.sessionId;

            //  如果有对于的动作action，请执行相应的逻辑
            List<CommunicateResponse.Action> actionList = result.actionList;

            setMap(result.slotsmap);

            if (actionList.size() > 1) {
                for (CommunicateResponse.Action action : actionList) {

                    if (!TextUtils.isEmpty(action.say)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(action.say);
                    }
                }
                int index = rand.nextInt(actionList.size());
                Log.d(TAG, "result message: " + actionList.get(index).say);

                message = actionList.get(index).say;

            } else if (actionList.size() == 1){

                CommunicateResponse.Action action = actionList.get(0);
                if (!TextUtils.isEmpty(action.say)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(action.say);
                    Log.d(TAG, "result[1] message: " + sb.toString());
//                    wakeUp.speak(sb.toString());

                    message = sb.toString();
                }

                if ("route_satisfy".equals(action.actionId)){
                    isRoute_satisfy = true;
                }

                // 执行自己的业务逻辑
                if ("start_work_satisfy".equals(action.actionId)) {
                    Log.i("wtf", "开始扫地");
                } else if ("stop_work_satisfy".equals(action.actionId)) {
                    Log.i("wtf", "停止工作");
                } else if ("move_action_satisfy".equals(action.actionId)) {
                    Log.i("wtf", "移动");
                } else if ("timed_charge_satisfy".equals(action.actionId)) {
                    Log.i("wtf", "定时充电");
                } else if ("timed_task_satisfy".equals(action.actionId)) {
                    Log.i("wtf", "定时扫地");
                } else if ("sing_song_satisfy".equals(action.actionId)) {
                    Log.i("wtf", "唱歌");
                }

                if (!TextUtils.isEmpty(action.mainExe)) {
//                    Toast.makeText(MainActivity.this, "请执行函数：" + action.mainExe, Toast.LENGTH_SHORT).show();
                }
            }

            Message mesg = handler.obtainMessage(2, message.length(), 1, message);
            handler.sendMessage(mesg);
        }
    }
}

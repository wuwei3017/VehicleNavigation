/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.aip.unit;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.aip.unit.listener.OnResultListener;
import com.baidu.aip.unit.model.AccessToken;
import com.baidu.aip.unit.model.CommunicateResponse;
import com.baidu.aip.unit.parser.CommunicateParser;
import com.baidu.aip.unit.utils.HttpUtil;

import android.content.Context;

public class APIService {

    private static final String BASE_URL = "https://aip.baidubce.com";
    private static final String ACCESS_TOEKN_URL = BASE_URL + "/oauth/2.0/token?";
    private static final String COMMUNCATE_URL = BASE_URL + "/rpc/2.0/solution/v1/unit_utterance";


    private static APIService instance;
    private Context context;
    private String accessToken;


    private APIService() {

    }

    public static APIService getInstance() {
        synchronized (APIService.class) {
            if (instance == null) {
                instance = new APIService();
            }
        }

        return instance;
    }

    public void init(UNITUtils context) {
//        this.context = context.getApplicationContext();

        HttpUtil.getInstance().init();
        // DeviceUuidFactory.init(context);
    }

    public void initAccessToken(final OnResultListener<AccessToken> listener, String ak, String sk) {
        StringBuilder sb = new StringBuilder();
        sb.append("client_id=").append(ak);
        sb.append("&client_secret=").append(sk);
        sb.append("&grant_type=client_credentials");
        HttpUtil.getInstance().getAccessToken(listener, ACCESS_TOEKN_URL, sb.toString());
    }

    public void setAccessToken(String token) {
        this.accessToken = token;
    }

    /**
     * 用户向平台请求对话内容，平台返回场景bot理解和应答的信息
     * @param listener 场景ID
     * @param sceneId  当前轮用户的query
     * @param query    标记一次会话，当新建一个会话时，不需要传值。服务端会返回一个唯一id。如果要保持这次多轮会话，再次请求时传入该id
     * @param sessionId
     */
    public void communicate(final OnResultListener<CommunicateResponse> listener,
                            int sceneId, String query, String sessionId) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("scene_id", sceneId);
            jsonObject.put("query", query);
            jsonObject.put("session_id", sessionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommunicateParser parser = new CommunicateParser();
        HttpUtil.getInstance().post(urlAppendCommonParams(COMMUNCATE_URL), jsonObject.toString(), parser, listener);
    }

    /**
     * URL append access token，sdkversion，aipdevid
     *
     * @param url
     * @return
     */
    private String urlAppendCommonParams(String url) {
        StringBuilder sb = new StringBuilder(url);
        sb.append("?access_token=").append(accessToken);

        return sb.toString();
    }


}

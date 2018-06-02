package com.graduation.chary.mynavi;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by LuakyChay on 2018/5/28.
 */

public class Bot {
    private static String API_KEY = "24900f8a12014850800c0cc9c91457ad";
    private static String URL = "http://www.tuling123.com/openapi/api";
    /**
     * 拼接Url
     * @param msg
     * @return
     */
    private  String setParams(String msg){
        try
        {
            msg = URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return URL + "?key=" + API_KEY + "&info=" + msg;
    }
    
    public String getData(String msg) {
        String text = null;
        URLConnection httpsConn = null;
        java.net.URL myURL = null;
        String url = setParams(msg);
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {

        }
        try {
            httpsConn = (URLConnection) myURL.openConnection();
            if (httpsConn != null) {
                InputStreamReader insr = new InputStreamReader(
                        httpsConn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(insr);
                String data = null;
                if ((data = br.readLine()) != null) {
                    Log.d("ChatData:", data);
                    JSONObject json = null;
                    json = new JSONObject(data);
                    text = json.getString("text");
                    Log.d("ChatData2:", text);

                }
            }
        } catch (IOException e) {

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return text;
    }
}

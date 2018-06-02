package com.graduation.chary.mynavi;

/**
 * Created by LuakyChay on 2018/5/28.
 */

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class HttpUtils
{

    private static String KEY = "871d0fb46d78c3355427df402fb8d210";

    public String getAddressByLatlng(double latitude, double longitude) {
        String value = null;
        String address = "";
        String url = String.format("http://restapi.amap.com/v3/geocode/regeo?key="+ KEY + "&location=" + longitude + "," + latitude + "&poitype=&radius=&extensions=all&batch=false&roadlevel=\n");
        Log.d("Http:", url);
        URL myURL = null;
        URLConnection httpsConn = null;
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
                    Log.d("Http:", data);
                    JSONObject json = null;
                    try {
                        json = new JSONObject(data);
                        JSONObject regeocode = json.getJSONObject("regeocode");
                         address = regeocode.optString("formatted_address");
                        Log.d("address:", address);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                insr.close();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }


    public Map<String, BigDecimal> getLatAndLngByAddress(String addr){
        String value = null;
        String address = "";
        String lat = "";
        String lng = "";
        try {
            address = java.net.URLEncoder.encode(addr,"UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String url = String.format("http://restapi.amap.com/v3/geocode/geo?key="+ KEY +"&address="+addr+"\n");
        Log.d("Http:", url);
        URL myURL = null;
        URLConnection httpsConn = null;
        //进行转码
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
                    Log.d("Http:", data);
                    JSONObject json = null;
                    JSONObject geocode = null;
                    try {
                        json = new JSONObject(data);
                        String geocodes = json.getString("geocodes");
                        Log.d("JSON1:", geocodes);
                        JSONArray arr = new JSONArray(geocodes);
                        String temp = arr.get(0).toString();
                        Log.d("JSON2:", temp);
                        JSONObject json1 = (JSONObject) arr.get(0);
                        value = json1.getString("location");
                        Log.d("JSON3:", value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                insr.close();
            }
        } catch (IOException e) {

        }
        String[] ll= value.split(",");
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        map.put("lat", new BigDecimal(ll[0]));
        map.put("lng", new BigDecimal(ll[1]));
        return map;
    }

}

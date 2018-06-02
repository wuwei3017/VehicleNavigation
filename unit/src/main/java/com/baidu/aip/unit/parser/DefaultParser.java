/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.aip.unit.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.aip.unit.exception.UnitError;
import com.baidu.aip.unit.model.ResponseResult;

import android.util.Log;

public class DefaultParser implements Parser<ResponseResult> {

    @Override
    public ResponseResult parse(String json) throws UnitError {
        Log.e("xx", "DefaultParser:" + json);
        try {
            JSONObject jsonObject = new JSONObject(json);

            if (jsonObject.has("error_code")) {
                UnitError error = new UnitError(jsonObject.optInt("error_code"), jsonObject.optString("error_msg"));
                throw error;
            }

            ResponseResult result = new ResponseResult();
            result.setLogId(jsonObject.optLong("log_id"));
            result.setJsonRes(json);

            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            UnitError error = new UnitError(UnitError.ErrorCode.JSON_PARSE_ERROR, "Json parse error:" + json, e);
            throw error;
        }
    }
}

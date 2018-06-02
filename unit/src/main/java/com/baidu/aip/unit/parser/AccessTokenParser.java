/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.aip.unit.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.aip.unit.exception.UnitError;
import com.baidu.aip.unit.model.AccessToken;

public class AccessTokenParser implements Parser<AccessToken> {
    @Override
    public AccessToken parse(String json) throws UnitError {
        try {
            AccessToken accessToken = new AccessToken();
            accessToken.setJson(json);
            JSONObject jsonObject = new JSONObject(json);

            if (jsonObject != null) {

                accessToken.setAccessToken(jsonObject.optString("access_token"));
                accessToken.setExpiresIn(jsonObject.optInt("expires_in"));
                return accessToken;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            UnitError error = new UnitError(UnitError.ErrorCode.JSON_PARSE_ERROR, "Json parse error", e);
            throw error;
        }
        return null;
    }
}

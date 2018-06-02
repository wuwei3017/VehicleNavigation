/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.aip.unit.utils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class JsonRequestBody extends RequestBody {

    private static final MediaType CONTENT_TYPE =
            MediaType.parse("application/json");

    private static final String FORM_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#&!$(),~";

    private String stringParams;

    public void setStringParams(String params) {
        this.stringParams = params;
    }

    @Override
    public MediaType contentType() {
        return CONTENT_TYPE;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        sink.writeUtf8(Util.canonicalize(stringParams, FORM_ENCODE_SET, false, false));
        sink.close();
    }

}

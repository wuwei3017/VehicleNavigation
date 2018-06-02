/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.aip.unit.listener;

import com.baidu.aip.unit.exception.UnitError;

public interface OnResultListener<T> {
    void onResult(T result);

    void onError(UnitError error);
}

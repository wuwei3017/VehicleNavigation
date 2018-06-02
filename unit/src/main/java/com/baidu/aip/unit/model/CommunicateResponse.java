/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.aip.unit.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommunicateResponse  extends ResponseResult {

    public List<Action> actionList = new ArrayList<>();
    public Map<String, String> slotsmap = new HashMap<>();

    public Schema schema;

    public String sessionId;


    public static class Action {
        public String actionId;
        public ActionType actionType;
        public List argList = new ArrayList<>();
        // public CodeAction codeAction;
        public int confidence;
        public List exeStatusList = new ArrayList<>();
        public List<String> hintList = new ArrayList<String>();
        public String mainExe;
        public String say;
    }

    public static class ActionType {
        public String target;
        public String targetDetail;
        public String type;
        public String typeDetail;
    }

    // public static class CodeAction {}

    public static class Schema {
        public List botMergedSlots = new ArrayList();
        public String currentQueryInent;
        public int intentConfidence;
    }

}

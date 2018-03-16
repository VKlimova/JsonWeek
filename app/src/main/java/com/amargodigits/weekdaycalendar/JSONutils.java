/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.amargodigits.weekdaycalendar;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import static com.amargodigits.weekdaycalendar.R.array.dayName;

/**
 * Utility functions to handle OpenWeatherMap JSON data.
 */
public final class JSONutils {

    public static void getSimpleStringsFromJson(String scheduleJsonStr)
            throws JSONException {


        final String OWM_LIST       = "Schedule";
        final String JSONDNUM       = "dayNumber";
        final String JSONDNAME      = "dayName";
        final String JSONDSCHEDULE  = "daySchedule";

     //   String[] parsedScheduleData = null;
        JSONObject scheduleJson = new JSONObject(scheduleJsonStr);
        JSONArray scheduleArray = scheduleJson.getJSONArray(OWM_LIST);
       // parsedScheduleData = new String[scheduleArray.length()];

        Log.i("WD", " scheduleJsonStr=" + scheduleJsonStr);
        Log.i("WD", " scheduleJson=" + scheduleJson);
        Log.i("WD", " scheduleArray=" + scheduleArray);


        for (int i = 0; i < scheduleArray.length(); i++) {

            /* Get the JSON object representing the day */
            JSONObject dayScheduleObj = scheduleArray.getJSONObject(i);
            Log.i("WD", " dayScheduleObj=" + dayScheduleObj);

            MainActivity.dayName[dayScheduleObj.getInt("dayNumber")]=dayScheduleObj.getString("dayName");

            MainActivity.daySchedule[dayScheduleObj.getInt("dayNumber")]=dayScheduleObj.getString("daySchedule");



            Log.i("WD","dayNumber="+dayScheduleObj.getInt("dayNumber"));
            Log.i("WD","dayName="+dayScheduleObj.getString("dayName"));
            Log.i("WD","daySchedule="+dayScheduleObj.getString("daySchedule"));
        }
        return ;
    }
}
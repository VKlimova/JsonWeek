package com.amargodigits.weekdaycalendar;

import android.provider.BaseColumns;

public class WDScheduleContract {

    public static final class ScheduleEntry implements BaseColumns {
        public static final String TABLE_NAME = "schedule";
        public static final String COLUMN_DAYNAME = "dayName";
        public static final String COLUMN_DAYID = "dayId";
        public static final String COLUMN_DAYSCHEDULE = "daySchedule";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

}

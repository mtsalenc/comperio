package com.rigel.comperio.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class ComperioContract {

    public static final String CONTENT_AUTHORITY = "com.rigel.comperio";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SCHEDULE = "schedules";
    public static final String PATH_FAVORITE = "favorites";

    public static class ScheduleEntry implements BaseColumns{

        public static final String COLUMN_SCHEDULE_ID = "schedule_id";
        public static final String COLUMN_HOUR_PRICE = "hour_price";
        public static final String COLUMN_SUBJECT_NAME = "subject_name";
        public static final String COLUMN_TEACHER_NAME = "teacher_name";
        public static final String COLUMN_TEACHER_RATING = "teacher_rating";
        public static final String COLUMN_TEACHER_PHONE = "teacher_phone";
        public static final String COLUMN_TEACHER_STORY = "teacher_story";
        public static final String COLUMN_TEACHER_LAT = "teacher_coord_lat";
        public static final String COLUMN_TEACHER_LONG = "teacher_coord_long";
        public static final String COLUMN_TEACHER_PIC_URL = "teacher_pic_url";
        public static final String COLUMN_TEACHER_DISTANCE = "distance";

    }

    public static final class ScheduleTable extends ScheduleEntry {

        public static final String TABLE_NAME = "schedules";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SCHEDULE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCHEDULE;


        public static Uri buildScheduleUriWith(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class FavoriteTable extends ScheduleEntry {

        public static final String TABLE_NAME = "favorites";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE;


        public static Uri buildFavoriteUriWith(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}

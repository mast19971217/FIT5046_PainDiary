package com.example.paindiary.room;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
public class Converters {
    public static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    @TypeConverter
    public static Date fromString(String value) {
        try {
            return value == null ? null :  sdf.parse(value);
        }
        catch (Exception e)
        { return null;}
    }

    @TypeConverter
    public static String dateToString(Date date) {
        //Log.d("!!!",sdf.format(date));
        return date == null ? null : sdf.format(date);
    }
}
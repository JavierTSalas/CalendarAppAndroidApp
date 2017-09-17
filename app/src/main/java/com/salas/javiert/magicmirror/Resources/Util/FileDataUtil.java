/*
 * Copyright (c) 2017. Javier Salas
 */

package com.salas.javiert.magicmirror.Resources.Util;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by javi6 on 8/10/2017.
 * <p>
 * https://stackoverflow.com/questions/11093182/date-formatting-based-on-user-locale-on-android
 */

public class FileDataUtil {
    public static String getModifiedDate(long modified) {
        return getModifiedDate(Locale.getDefault(), modified);
    }

    public static String getModifiedDate(Locale locale, long modified) {
        SimpleDateFormat dateFormat = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            dateFormat = new SimpleDateFormat(getDateFormat(locale));
        } else {
            dateFormat = new SimpleDateFormat("EEE, d MMM");
        }

        return dateFormat.format(new Date(modified));
    }

    public static String getModifiedTime(Locale locale, long modified) {
        SimpleDateFormat dateFormat = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            dateFormat = new SimpleDateFormat(getTimeFormat(locale));
        } else {
            dateFormat = new SimpleDateFormat("hh:mm");
        }

        return dateFormat.format(new Date(modified));
    }

    public static String getModifiedMonthName(Locale locale, long modified) {
        SimpleDateFormat dateFormat = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            dateFormat = new SimpleDateFormat(getMonthFormat(locale));
        } else {
            dateFormat = new SimpleDateFormat("MMM");
        }

        return dateFormat.format(new Date(modified));
    }

    public static String getModifiedCalendarDay(Locale locale, long modified) {
        SimpleDateFormat dateFormat = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            dateFormat = new SimpleDateFormat(getCalendarDayFormat(locale));
        } else {
            dateFormat = new SimpleDateFormat("dd");
        }

        return dateFormat.format(new Date(modified));
    }


    public static String getRecyclerDate(Locale locale, long modified) {
        SimpleDateFormat dateFormat = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            dateFormat = new SimpleDateFormat(getRecyclerDateJellyBean(locale));
        } else {
            dateFormat = new SimpleDateFormat("EEEE, MMM d");
        }
        return dateFormat.format(new Date(modified));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getDateFormat(Locale locale) {
        return DateFormat.getBestDateTimePattern(locale, "EEE, d MMM ");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getTimeFormat(Locale locale) {
        return DateFormat.getBestDateTimePattern(locale, "hh:mm");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getMonthFormat(Locale locale) {
        return DateFormat.getBestDateTimePattern(locale, "MMM");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getCalendarDayFormat(Locale locale) {
        return DateFormat.getBestDateTimePattern(locale, "dd");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getRecyclerDateJellyBean(Locale locale) {
        return DateFormat.getBestDateTimePattern(locale, "EEEE, MMM d");
    }
}
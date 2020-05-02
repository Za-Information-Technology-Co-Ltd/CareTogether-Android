package com.za.caretogether.utils;

import android.content.Context;

import org.rabbitconverter.rabbit.Rabbit;

/**
 * Created by arkar on 3/23/17.
 */

public class ViewUtil {

    public static String getString(Context context, int stringRes) {
        String text = context.getString(stringRes);
        if (isZawgyi(context)) {
            text = Rabbit.uni2zg(text);
        }
        return text;
    }

    public static String getString(Context context, String text) {
        if (isZawgyi(context))
            return Rabbit.uni2zg(text);
        return text;
    }

    public static boolean isZawgyi(Context context) {
        return Storage.make(context).getIsZawgyi();
    }

}

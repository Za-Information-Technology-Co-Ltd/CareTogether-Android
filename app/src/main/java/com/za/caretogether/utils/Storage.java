package com.za.caretogether.utils;

import android.content.Context;
import android.location.Location;

import com.google.gson.Gson;

public class Storage {

    private static Storage mInstance = null;

    private PreferencesHelper mPreferencesHelper;

    private Storage(Context context) {
        mPreferencesHelper = new PreferencesHelper(context);
    }

    public static Storage make(Context context) {
        if (mInstance == null) {
            mInstance = new Storage(context);
        }

        return mInstance;
    }

    public static Storage createNew(Context context) {
        mInstance = new Storage(context);

        return mInstance;
    }

    public String getAuthToken() {
        return mPreferencesHelper.getString(PreferencesHelper.TOKEN, null);
    }

    public void setAuthToken(String token) {
        mPreferencesHelper.putString(PreferencesHelper.TOKEN, token);
    }

    public String getPhone() {
        return mPreferencesHelper.getString(PreferencesHelper.PHONE, null);
    }

    public void setPhone(String phone) {
        mPreferencesHelper.putString(PreferencesHelper.PHONE, phone);
    }

    public String getLat() {
        return mPreferencesHelper.getString(PreferencesHelper.LAT, null);
    }

    public void setLat(String phone) {
        mPreferencesHelper.putString(PreferencesHelper.LAT, phone);
    }

    public String getLong() {
        return mPreferencesHelper.getString(PreferencesHelper.LONG, null);
    }

    public void setLong(String phone) {
        mPreferencesHelper.putString(PreferencesHelper.LONG, phone);
    }

    public String getUserPhone() {
        return mPreferencesHelper.getString(PreferencesHelper.PHONE_USER, null);
    }

    public void setUserPhone(String phone) {
        mPreferencesHelper.putString(PreferencesHelper.PHONE_USER, phone);
    }

    public String getLocale() {
        return mPreferencesHelper.getString(PreferencesHelper.LOCALE, "my");
    }

    public void setLocale(String locale) {
        mPreferencesHelper.putString(PreferencesHelper.LOCALE, locale);
    }

    public String getInstallDate() {
        return mPreferencesHelper.getString(PreferencesHelper.INSTALL_DATE, null);
    }

    public void setInstallDate(String phone) {
        mPreferencesHelper.putString(PreferencesHelper.INSTALL_DATE, phone);
    }
    public Integer getCount() {
        return mPreferencesHelper.getInt(PreferencesHelper.COUNT, 0);
    }

    public void setCount(int count) {
        mPreferencesHelper.putInt(PreferencesHelper.COUNT, count);
    }

    public Location getLocation() {
        Gson gson = new Gson();
        String json = mPreferencesHelper.getString("locationObj", "");
        return gson.fromJson(json, Location.class);
    }

    public void setLocation(Location location) {
        Gson gson = new Gson();
        String json = gson.toJson(location);
        mPreferencesHelper.putString("locationObj", json);
    }

    public void clearLoginData() {
        mPreferencesHelper.clear();
    }

    public boolean getIsZawgyi() {
        return mPreferencesHelper.getBoolean(PreferencesHelper.IS_ZAWGYI, false);
    }

    public void saveIsZawgyi(boolean isZawgyi) {
        mPreferencesHelper.putBoolean(PreferencesHelper.IS_ZAWGYI, isZawgyi);
    }

    public boolean getIsFirstTime() {
        return mPreferencesHelper.getBoolean(PreferencesHelper.IS_FIRST_TIME_LOGIN, true);
    }

    public void saveIsFirstTime(boolean isFirstTime) {
        mPreferencesHelper.putBoolean(PreferencesHelper.IS_FIRST_TIME_LOGIN, isFirstTime);
    }

    public boolean getIsFirstConnect() {
        return mPreferencesHelper.getBoolean(PreferencesHelper.IS_FIRST_TIME_CONNECT, true);
    }

    public void saveIsFirstConnect(boolean isFirstTime) {
        mPreferencesHelper.putBoolean(PreferencesHelper.IS_FIRST_TIME_CONNECT, isFirstTime);
    }
}

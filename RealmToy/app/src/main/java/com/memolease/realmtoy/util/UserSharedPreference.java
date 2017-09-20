package com.memolease.realmtoy.util;

import android.content.SharedPreferences;

import com.memolease.realmtoy.application.RealmToyApplication;

public class UserSharedPreference {
    private static final String TAG = UserSharedPreference.class.getSimpleName();
    private static UserSharedPreference mInstance = null;
    private SharedPreferences mSharedPreferencesInstance = null;
    private SharedPreferences.Editor editor = null;
    private int selectedLibrary;
    private int selectedLibraryState;

    protected UserSharedPreference() {
    }

    public static UserSharedPreference getInstance() {
        if (mInstance == null) {
            mInstance = new UserSharedPreference();
        }

        mInstance.mSharedPreferencesInstance = RealmToyApplication.getInstance().getApplicationContext().getSharedPreferences("user_shared_preferences", 0);
        mInstance.editor = mInstance.mSharedPreferencesInstance.edit();
        return mInstance;
    }

    public int getId() {
        return getInt("id");
    }

    public void setId(int id) {
        putInt("id", id);
    }

    public String getAuthToken() {
        return getString("authToken");
    }

    public void setAuthToken(String authToken) {
        putString("authToken", authToken);
    }

    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        putString("email", email);
    }

    public String getThumbnail() { return getString("thumbnail"); }

    public void setThumbnail(String url) { putString("thumbnail", url); }

    public String getNickname() { return getString("name"); }

    public void setNickname(String name) { putString("name", name); }

    public Boolean getNewBadgeFlag(int idx) {
        return getBoolean("newBadgeFlag_"+idx);
    }

    public void setNewBadgeFlag(int idx, Boolean value) {
        putBoolean("newBadgeFlag_" + idx, value);
    }

    public Boolean getReadGuideFlag() {
        return getBoolean("readGuideFlag");
    }

    public void setReadGuideFlag(Boolean value) { putBoolean("readGuideFlag", value); }

    public Boolean getLoginState() {
        return getBoolean("loginState");
    }

    public void setLoginState(Boolean value) { putBoolean("loginState", value); }

    public String getLocationTitle(int idx) { return getString("locationTitles_" + idx); }
    public void setLocationTitle(int idx, String value) { putString("locationTitles_" + idx, value); }

    public double getLocationLng(int idx) { return Double.parseDouble(getString("locationLngs_" + idx)); }
    public void setLocationLng(int idx, double value) { putString("locationLngs_"+idx, String.valueOf(value)); }

    public double getLocationLat(int idx) { return Double.parseDouble(getString("locationLats_" + idx)); }
    public void setLocationLat(int idx, double value) { putString("locationLats_"+idx, String.valueOf(value)); }

    public int getLocationStaticId(int idx) { return getInt("locationStaticIds_" + idx); }
    public void setLocationStaticId(int idx, int value) { putInt("locationStaticIds_" + idx, value); }

    private int getInt(String key) {
        return mSharedPreferencesInstance.getInt(key, 0);
    }

    private String getString(String key) {
        return mSharedPreferencesInstance.getString(key, "");
    }

    private Boolean getBoolean(String key) {
        return mSharedPreferencesInstance.getBoolean(key, false);
    }

    private Float getFloat(String key) {
        return mSharedPreferencesInstance.getFloat(key, 0);
    }

    private void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    private void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    private void putBoolean(String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    private void putFloat(String key, Float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public int getSelectedLibrary() {
        return selectedLibrary;
    }

    public void setSelectedLibrary(int selectedLibrary) {
        this.selectedLibrary = selectedLibrary;
        this.selectedLibraryState = 0;
    }

    public int getSelectedLibraryState() {
        return selectedLibraryState;
    }

    public void setSelectedLibraryState(int selectedLibraryState) {
        this.selectedLibrary = 0;
        this.selectedLibraryState = selectedLibraryState;
    }
}

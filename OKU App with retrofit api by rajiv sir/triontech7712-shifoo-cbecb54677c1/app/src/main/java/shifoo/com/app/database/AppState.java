package shifoo.com.app.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class AppState {

    private static AppState appState;
    private SharedPreferences preferences;


    private String SHARED_PREFS_NAME = "shifoo.com.app";

    private AppState(Context context) {
        preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static AppState getInstance(Context context) {
        synchronized (AppState.class) {
            if (appState == null) {
                appState = new AppState(context);
            }
        }
        return appState;
    }



    public void setIsUserReqistered(boolean isUserReqistered) {
        storeboolean(PrefsKeys.IS_USER_REGISTERED, isUserReqistered);
    }

    public boolean isUserRegistered() {
        return getBoolean(PrefsKeys.IS_USER_REGISTERED);
    }


    public void setUserToken(String userToken) {
        storeString(PrefsKeys.USER_TOKEN, userToken);
    }

    public String getUserToken() {
        return getStoreString(PrefsKeys.USER_TOKEN);
    }

    private boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    private Boolean storeboolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).commit();
        return storeboolean(key, value);
    }


    private void storeObjectAsString(String key, Object value) {
        preferences.edit().putString(key, new Gson().toJson(value)).commit();
    }

    private void storeString(String key, String value) {
        preferences.edit().putString(key, value).commit();
    }

    private String getStoreString(String key) {
        return preferences.getString(key, null);
    }


}

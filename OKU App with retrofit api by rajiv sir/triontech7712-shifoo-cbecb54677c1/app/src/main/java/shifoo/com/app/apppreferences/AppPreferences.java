/*
 * Copyright (C) 2017 Orange Tree technology Private Limited

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Orange Tree Technology private Limited
 * @developer  Rajiv Ranjan Singh
 *  Email : rajiv.ar73@gmail.com
 *
 * used to keep all utility methods which we used in the app
 */


package shifoo.com.app.apppreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import shifoo.com.app.library.HostUrl;


/**
 *  enum for SharedPreference of application because it will
 *         use through out the app.
 */
public enum AppPreferences
{
	INSTANCE;
	private SharedPreferences mPreferences;
	private Editor mEditor;

	/**
	 * Enum for shared preferences keys to store various values
	 * 
	 * @author Appstudioz
	 */
	public enum SharedPreferncesKeys
	{
        user_login_data,user_id,user_name, user_avtaar,  auth_key,is_logged_in


        ///









    }

	/**
	 * private constructor for singleton class
	 * 
	 * @param context
	 */
	public void initAppPreferences(Context context)
	{
		mPreferences = context.getSharedPreferences(HostUrl.prefName, Context.MODE_PRIVATE);
		mEditor = mPreferences.edit();
	}


//    public int getAppVersion()
//    {
//        return mPreferences.getInt(SharedPreferncesKeys.appVersion.toString(), 0);
//    }
//    public void setAppVersion(int value)
//    {
//        mEditor.putInt(SharedPreferncesKeys.appVersion.toString(), value);
//        mEditor.commit();
//    }


    public void setString(String key,String value)
    {
        mEditor.putString(key, value);
        mEditor.commit();
    }
    public void setLong(String key,long value)
    {
        mEditor.putLong(key, value);
        mEditor.commit();
    }




    public void setInt(String key,int value)
    {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

//    public void setLat(String lat)
//    {
//        mEditor.putString(SharedPreferncesKeys.Current_Lattitude.toString(), lat);
//        mEditor.commit();
//    }
//    public String getLat()
//    {
//        return mPreferences.getString(SharedPreferncesKeys.Current_Lattitude.toString(), "");
//    }
//
//    public void setLongt(String longt)
//    {
//        mEditor.putString(SharedPreferncesKeys.Current_Longitude.toString(), longt);
//        mEditor.commit();
//    }
//    public String getLongt()
//    {
//        return mPreferences.getString(SharedPreferncesKeys.Current_Longitude.toString(),"");
//    }


//    public void setGCMid(String gcmid)
//    {
//        mEditor.putString(SharedPreferncesKeys.gcmid.toString(), gcmid);
//        mEditor.commit();
//    }
//    public String getGCMid()
//    {
//        return mPreferences.getString(SharedPreferncesKeys.gcmid.toString(),"");
//    }

    public void setBoolean(String key,boolean value)
    {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public String getString(String key)
    {
        return mPreferences.getString(key, null);
    }
    public String getString(String key,String defaultValue)
    {
        return mPreferences.getString(key, defaultValue);
    }

    public int getInt(String key)
    {
        return mPreferences.getInt(key, Integer.MIN_VALUE);
    }
    public int getInt(String key,int defaultValue)
    {
        return mPreferences.getInt(key, defaultValue);
    }

    public long getLong(String key)
    {
        return mPreferences.getLong(key, Long.MIN_VALUE);
    }
    public long getLong(String key,long defaultValue)
    {
        return mPreferences.getLong(key, defaultValue);
    }

    public boolean getBoolean(String key)
    {
        return mPreferences.getBoolean(key, false);
    }
    public boolean getBoolean(String key,boolean defaultValue)
    {
        return mPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Used to clear all the values stored in preferences
     * @return void
     */
    public void clearPreferences()
    {
        mEditor.clear();
        mEditor.commit();
    }
}

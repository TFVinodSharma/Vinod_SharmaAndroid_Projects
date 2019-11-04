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


package shifoo.com.app.modules.utility;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

/**
 * this is util function class . it contains handles all utilities method 
 * for this application
 */
public class UtilFunctions
{
	/**
	 * this method is use to get contact number for specific id
	 * @param id
	 * @param context
	 * @return {@link ArrayList}
	 */
	public static final ArrayList<String> getContactNumbersFromId(String id,Context context) 
	{
		ArrayList<String> number = new ArrayList<String>();
		Cursor cursor = null;
		try
		{
			cursor = context.getContentResolver().query(Phone.CONTENT_URI,null, Phone.CONTACT_ID + " = ?", new String[] { id }, null);
			if (cursor != null && cursor.getCount() > 0) 
			{
				cursor.moveToFirst();
				do
				{
					String num = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
					if(num!=null)
						number.add(num);
				}while(cursor.moveToNext());
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return number;
	}

	public static int dpToPx(int dp)
	{
		return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
	}

	/**
	 * This method is use to remove extra parameter from number
	 * @param number
	 * @return String
	 */
	public static String removeExtraParameterFromNumber(String number)
	{
		if (number == null)
			return number;

		number= number.trim();
		number = number.replaceAll(" ", "");
		number = number.replaceAll("-", "");
		number = number.replaceAll("\\(", "");
		number = number.replaceAll("\\)", "");
		return number.trim();
	}

	/**
	 * This method is used to fetch all the application installed in device.
	 * @return List<ApplicationInfo>
	 */
	public static List<ApplicationInfo> getInstalledApplication(Context c) 
	{
		List<ApplicationInfo> installedApps = c.getPackageManager().getInstalledApplications(PackageManager.PERMISSION_GRANTED);
		List<ApplicationInfo> launchableInstalledApps = new ArrayList<ApplicationInfo>();
		for(int i =0; i<installedApps.size(); i++)
		{
			if(c.getPackageManager().getLaunchIntentForPackage(installedApps.get(i).packageName) != null)
			{
				//If you're here, then this is a launch-able app
				launchableInstalledApps.add(installedApps.get(i));
			}
		}
		return launchableInstalledApps;
	}

	/**
	 * this method is use to hide keyboard 
	 * @param activity
	 * @return void
	 */
	public static void hideKeyboard(Activity activity)
	{
		InputMethodManager inputManager = null;
		if(inputManager == null)
			inputManager =(InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		try
		{
			if(inputManager.isActive())
				inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * This method returns the formatted number by removing +91 or 0 any other code from the number 
	 * depending on the length of the number.
	 * @param number
	 * @return String (formatted number)
	 */
	public static String formatToQuerySearchNumber(String number)
	{
		if (number != null)
		{
			int length = number.length();
			if (length == 10) 
			{
				number= "%" + number;
				return number;
			}
			else if (length > 12) 
			{
				number = "%" + number.substring(length - 10, length);
			} else if (length > 10) 
			{
				number = "%" + number.substring(length - 10, length);
			} else if (length > 6) 
			{
				number = "%" + number.substring(length - 7, length);
			}
		}
		return number;
	}
}

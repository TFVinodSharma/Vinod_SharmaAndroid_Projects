package com.recorder.screen.screen_recorder.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.recorder.screen.screen_recorder.R;

import java.util.List;
import java.util.Vector;

public class first extends ListActivity{

    private LayoutInflater mInflater;
    private Vector<RowData> data;
    RowData rd;
    static final String[] title = new String[] {
            "*New*Apple iPad Wi-Fi (16GB)",
            "7 Touch Tablet -2GB Google Android",
            "Apple iPad Wi-Fi (16GB) Rarely Used ",
            "Apple iPad Wi-Fi (16GB) AppleCase" };
    static final String[] detail = new String[] { "1h 37m Shipping: $10.00",
            "1h 39m Shipping: Free", "58m 6s Shipping: $10.00",
            "59m 30s Shipping: $10.95" };

	/*public void  onBackPressed() {


		finish();
		return;
	}*/

/*	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		Log.v("Debug", "On key pressed");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// super.onKeyDown(KeyEvent.KEYCODE_BACK,event);
			Log.v("Debug", "back pressed");
			Intent intent23 = new Intent(Intent.ACTION_MAIN);
			intent23.addCategory(Intent.CATEGORY_HOME);
			intent23.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent23.addFlags(Intent.FLAG_FROM_BACKGROUND);
			getApplicationContext().startActivity(intent23);
			finish();
			return  true;
		}
		// return super.onKeyDown(keyCode,event);
		return true;
	}*/



    /*
     * In to the on create method we have to fetching Packege Name data Size
     * catche saize Data size and Combination of these we have to draw a List
     * view which is view to the user and filter thode Application on to that
     * List view which Packege Name Starts with .org that packege is not showing
     * in to Manage Applicatiion List view
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Log.v("Debug", "in create");
        mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        data = new Vector<RowData>();
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm
                .getInstalledApplications(PackageManager.GET_META_DATA);

        int size = packages.size();



        for (ApplicationInfo packageInfo : packages) {

            PackageStats pks = new PackageStats(packageInfo.name);
            Log.v("File", "package name  =" + packageInfo.packageName);
            Log.v("File", "dataSize " + pks.dataSize);
            Log.v("File", "cacheSize " + pks.cacheSize);
            Log.v("File", "codeSize " + pks.codeSize);

            if (!(packageInfo.publicSourceDir).startsWith("/data/app/"))
                continue;
            if ((packageInfo.packageName).startsWith("com.android.service")
                    || packageInfo.packageName.startsWith("video.")||packageInfo.packageName.startsWith("com."))
                continue;
            rd = new RowData(packageInfo.loadIcon(pm),
                    (String) packageInfo.loadLabel(pm),
                    packageInfo.processName, packageInfo.packageName);


            data.add(rd);
        }
        CustomAdapter adapter = new CustomAdapter(this, R.layout.list,
                R.id.title, data);
        setListAdapter(adapter);
        getListView().setTextFilterEnabled(true);
    }// end of declaration of class ListImage

    public void onListItemClick(ListView parent, View v, int position, long id) {

        RowData rd1 = data.elementAt(position);
        String packagename = "package:" + rd1.mpackagename;
        Uri packageURI = Uri.parse(packagename);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        startActivity(uninstallIntent);
        // finish();

    }

    private class RowData {
        protected Drawable mId;
        protected String mTitle;
        protected String mDetail;
        public String mpackagename;

        RowData(Drawable id, String title, String detail, String packagename) {
            mId = id;
            mTitle = title;
            mDetail = detail;
            mpackagename = packagename;
        }

        @Override
        public String toString() {
            return mId + " " + mTitle + " " + mDetail;
        }
    }

    private class CustomAdapter extends ArrayAdapter<RowData> {
        public CustomAdapter(Context context, int resource,
                             int textViewResourceId, List<RowData> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            TextView title = null;
            TextView detail = null;
            ImageView i11 = null;
            RowData rowData = getItem(position);
            if (null == convertView) {
                convertView = mInflater.inflate(R.layout.list, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }



            holder = (ViewHolder) convertView.getTag();
            title = holder.gettitle();
            title.setText(rowData.mTitle);
            detail = holder.getdetail();
            detail.setText(rowData.mDetail);
            i11 = holder.getImage();

            i11.setImageDrawable(rowData.mId);
            return convertView;
        }

        private class ViewHolder {
            private View mRow;
            private TextView title = null;
            private TextView detail = null;
            private ImageView i11 = null;

            public ViewHolder(View row) {
                mRow = row;
            }

            public TextView gettitle() {
                if (null == title) {
                    title = (TextView) mRow.findViewById(R.id.title);
                }
                return title;
            }

            public TextView getdetail() {
                if (null == detail) {
                    detail = (TextView) mRow.findViewById(R.id.detail);
                }
                return detail;
            }

            public ImageView getImage() {
                if (null == i11) {
                    i11 = (ImageView) mRow.findViewById(R.id.img);

                }
                return i11;
            }
        } // end of declaration of class ViewHolder

    }// end of declaration of class CustomAdapter

}
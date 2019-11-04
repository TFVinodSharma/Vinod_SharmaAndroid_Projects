package com.recorder.screen.screen_recorder.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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

public class ListImage extends ListActivity {
    private LayoutInflater mInflater;
    private Vector<RowData> data;
    RowData rd;

    static final String[] title = new String[] {
            "*New*Apple iPad Wi-Fi (16GB)", "7 Touch Tablet -2GB Google Android",
            "Apple iPad Wi-Fi (16GB) Rarely Used ","Apple iPad Wi-Fi (16GB) AppleCase"    	};
    static final String[] detail = new String[] {
            "1h 37m Shipping: $10.00","1h 39m Shipping: Free","58m 6s Shipping: $10.00",
            "59m 30s Shipping: $10.95"	};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle b= new Bundle();
        super.onCreate(b);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.main);
        //Log.v("Debug", "in create");
        mInflater = (LayoutInflater) getSystemService(
                Activity.LAYOUT_INFLATER_SERVICE);
        data = new Vector<RowData>();
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm
                .getInstalledApplications(PackageManager.GET_META_DATA);

        int size = packages.size();



        for (ApplicationInfo packageInfo : packages) {



            if((packageInfo.packageName).startsWith("com.")||(packageInfo.packageName).startsWith("video."))
                continue;
            rd = new RowData(packageInfo.loadIcon(pm),(String)packageInfo.loadLabel(pm),packageInfo.processName,packageInfo.packageName);


            //  }

            data.add(rd);
        }
        CustomAdapter adapter = new CustomAdapter(this, R.layout.list,
                R.id.title, data);
        setListAdapter(adapter);
        getListView().setTextFilterEnabled(true);
    }//end of declaration of class ListImage



    public void onListItemClick(ListView parent, View v, int position,
                                long id) {
        //   Toast.makeText(getApplicationContext(), "You have selected "
        //                 +(position+1)+"th item",  Toast.LENGTH_SHORT).show();
        RowData rd1 =data.elementAt(position);
        String packagename ="package:"+rd1.mpackagename;
        Uri packageURI = Uri.parse(packagename);
		/*	Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
			startActivity(uninstallIntent);*/
        //finish();
        ////////////////

        Intent intent;

        if (android.os.Build.VERSION.SDK_INT >= 9) {
            //  Uri packageURI = Uri.parse("package:" + pkgName);
            intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", packageURI);
            startActivity(intent);
        }  else  {
		        /* on older Androids, use trick to show app details */
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", packagename);
            intent.putExtra("pkg", packagename);
            startActivity(intent);
        }


        /////////////////////


    }
    private class RowData {
        protected Drawable mId;
        protected String mTitle;
        protected String mDetail;
        public  String mpackagename;
        RowData(Drawable id,String title,String detail,String packagename){
            mId=id;
            mTitle = title;
            mDetail=detail;
            mpackagename=packagename;
        }
        @Override
        public String toString() {
            return mId+" "+mTitle+" "+mDetail;
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
            ImageView i11=null;
            RowData rowData= getItem(position);
            if(null == convertView){
                convertView = mInflater.inflate(R.layout.list, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            title = holder.gettitle();
            title.setText(rowData.mTitle);
            detail = holder.getdetail();
            detail.setText(rowData.mDetail);
            i11=holder.getImage();
            //i11.setImageResource(imgid[rowData.mId]);
            i11.setImageDrawable(rowData.mId);
            return convertView;
        }
        private class ViewHolder {
            private View mRow;
            private TextView title = null;
            private TextView detail = null;
            private ImageView i11=null;
            public ViewHolder(View row) {
                mRow = row;
            }
            public TextView gettitle() {
                if(null == title){
                    title = (TextView) mRow.findViewById(R.id.title);
                }
                return title;
            }
            public TextView getdetail() {
                if(null == detail){
                    detail = (TextView) mRow.findViewById(R.id.detail);
                }
                return detail;
            }
            public ImageView getImage() {
                if(null == i11){
                    i11 = (ImageView) mRow.findViewById(R.id.img);
                }
                return i11;
            }
        }  // end of declaration of  class ViewHolder

    }// end of declaration of class CustomAdapter

}
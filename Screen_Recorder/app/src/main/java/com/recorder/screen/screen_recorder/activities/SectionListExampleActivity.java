package com.recorder.screen.screen_recorder.activities;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.recorder.screen.screen_recorder.R;
import com.recorder.screen.screen_recorder.helper.EntryAdapter;
import com.recorder.screen.screen_recorder.helper.EntryItem;
import com.recorder.screen.screen_recorder.helper.Item;
import com.recorder.screen.screen_recorder.helper.SectionItem;
import com.recorder.screen.screen_recorder.services.myappmanager2;

import java.util.ArrayList;

public class SectionListExampleActivity extends ListActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_section_list_example);
//    }

    public static boolean running =false;

    ArrayList<Item> items = new ArrayList<Item>();

    @Override
    public void onBackPressed() {
        Intent intent23 = new Intent(Intent.ACTION_MAIN);
        intent23.addCategory(Intent.CATEGORY_HOME);
        intent23.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent23.addFlags(Intent.FLAG_FROM_BACKGROUND);
        getApplicationContext().startActivity(intent23);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.d("MyDebug", "keycode "+keyCode);
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        running=false;
        try
        {
            //unregisterReceiver(broadcastReceiverRecent);
        }
        catch(Exception e){;}
        super.onDestroy();
        finish();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        running= true;
        IntentFilter filter = new IntentFilter(myappmanager2.recentappfired);
        filter.setPriority(1000);
        //registerReceiver(broadcastReceiverRecent, filter);

        items.add(new SectionItem(getString(R.string.Wireless)));
        items.add(new EntryItem(getString(R.string.Wi)));
        items.add(new EntryItem(getString(R.string.Bluetooth)));
        items.add(new EntryItem(getString(R.string.More)));


        items.add(new SectionItem(getString(R.string.Device)));
        items.add(new EntryItem(getString(R.string.Call)));
        items.add(new EntryItem(getString(R.string.Sound)));
        items.add(new EntryItem(getString(R.string.Display)));
        items.add(new EntryItem(getString(R.string.Applications)));


        items.add(new SectionItem(getString(R.string.Personal)));
        items.add(new EntryItem(getString(R.string.Accounts)));
        items.add(new EntryItem(getString(R.string.Location)));
        items.add(new EntryItem(getString(R.string.Security)));


        items.add(new SectionItem(getString(R.string.System)));
        items.add(new EntryItem(getString(R.string.Date)));
        items.add(new EntryItem(getString(R.string.Accessibility)));
        items.add(new EntryItem(getString(R.string.About)));


        EntryAdapter adapter = new EntryAdapter(this, items);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        if(!items.get(position).isSection()){

            EntryItem item = (EntryItem)items.get(position);

            //	Toast.makeText(this, "You clicked "+position+" " + item.title , Toast.LENGTH_SHORT).show();

            if (id == 1) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                startActivity(intent);
            }
            else if (id == 2) {
                Intent intentBluetooth = new Intent();
                intentBluetooth.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(intentBluetooth);
            }

            else if (id == 3) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                startActivity(intent);
            }
            else if (id == 5) {
                Intent intent24 = new Intent(Intent.ACTION_MAIN).addCategory(
                        Intent.CATEGORY_LAUNCHER).setClassName("com.android.phone",
                        "com.android.phone.CallFeaturesSetting").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_FROM_BACKGROUND).setComponent(new ComponentName("com.android.phone",
                                "com.android.phone.CallFeaturesSetting"));
                getApplicationContext().startActivity(intent24);
            }
            else if (id == 6) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_SOUND_SETTINGS);
                startActivity(intent);
            }
            else if (id == 7) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_DISPLAY_SETTINGS);
                startActivity(intent);
            }
            else if (id == 8) {

                Intent intent24 = new Intent(Intent.ACTION_MAIN).addCategory(
                        Intent.CATEGORY_LAUNCHER).setClassName("video.android.combine",
                        "video.android.combine.TabActivity").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_FROM_BACKGROUND).setComponent(new ComponentName("com.recorder.screen.screen_recorder",
                                "com.recorder.screen.screen_recorder.activities.TabActivity"));
                getApplicationContext().startActivity(intent24);

				/*Intent intent25 = new Intent();
				intent25.setAction(Settings.ACTION_APPLICATION_SETTINGS);
				startActivity(intent25);*/

            }
            else if (id == 10) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_ADD_ACCOUNT);
                startActivity(intent);
            }
            else if (id == 11) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

            else if (id == 12) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_SECURITY_SETTINGS);
                startActivity(intent);
            }

            else if (id == 14) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_DATE_SETTINGS);
                startActivity(intent);
            } else if (id == 15) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            }

            else if (id == 16) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_DEVICE_INFO_SETTINGS);
                startActivity(intent);
            }




        }

        super.onListItemClick(l, v, position, id);
    }

//	private BroadcastReceiver broadcastReceiverRecent = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			 /* Intent intent23 = new Intent(
//						Intent.ACTION_MAIN);
//				intent23.addCategory(Intent.CATEGORY_HOME);
//				intent23.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent23.addFlags(Intent.FLAG_FROM_BACKGROUND);
//
//				getApplicationContext()
//
//						.startActivity(
//								intent23);*/
//
//			SectionListExampleActivity.this.finish();
//	       /*   Intent intent23 = new Intent(
//						Intent.ACTION_MAIN);
//				intent23.addCategory(Intent.CATEGORY_HOME);
//				intent23.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent23.addFlags(Intent.FLAG_FROM_BACKGROUND);
//
//				getApplicationContext()
//
//						.startActivity(
//								intent23);
//		Intent intent24 = new Intent(Intent.ACTION_MAIN).addCategory(
//					Intent.CATEGORY_LAUNCHER).setClassName("com.android.systemui",
//					"com.android.systemui.recent.RecentsActivity").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//					.addFlags(Intent.FLAG_FROM_BACKGROUND).setComponent(new ComponentName("com.android.systemui",
//				"com.android.systemui.recent.RecentsActivity"));
//	getApplicationContext().startActivity(intent24);
//
//    Intent intent25 = new Intent(
//			Intent.ACTION_MAIN);
//	intent25.addCategory(Intent.CATEGORY_HOME);
//	intent25.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	intent25.addFlags(Intent.FLAG_FROM_BACKGROUND);
//
//	getApplicationContext()
//
//			.startActivity(
//					intent25);
//
//
//
//
//
//*/          Log.d("tab", "receiver fired");
//		}
//	};



}

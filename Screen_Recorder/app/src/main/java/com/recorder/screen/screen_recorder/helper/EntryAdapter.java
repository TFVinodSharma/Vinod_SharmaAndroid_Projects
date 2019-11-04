package com.recorder.screen.screen_recorder.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.recorder.screen.screen_recorder.R;

import java.util.ArrayList;


public class EntryAdapter extends ArrayAdapter<Item> {

	private Context context;
	private ArrayList<Item> items;
	private LayoutInflater vi;

	public EntryAdapter(Context context, ArrayList<Item> items) {
		super(context,0, items);
		this.context = context;
	 	this.items = items;
		vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		   View v = convertView;

		  final Item i = items.get(position);
		if (i != null) {
			if(i.isSection()){
				SectionItem si = (SectionItem)i;
				v = vi.inflate(R.layout.list_item_section, null);

				v.setOnClickListener(null);
				v.setOnLongClickListener(null);
				v.setLongClickable(false);
				
				final TextView sectionView = (TextView) v.findViewById(R.id.list_item_section_text);
				sectionView.setText(si.getTitle());
			}else{
				EntryItem ei = (EntryItem)i;
				v = vi.inflate(R.layout.list_item_entry, null);
				final TextView title = (TextView)v.findViewById(R.id.list_item_entry_title);
				//final TextView subtitle = (TextView)v.findViewById(R.id.list_item_entry_summary);
				
				if (title != null) 
					title.setText(ei.title);
			
				ImageView img = (ImageView)v.findViewById(R.id.list_item_entry_drawable);
				img.setImageResource(R.drawable.about);
				  switch (position) {
	              case 1:
	            	  img.setImageResource(R.drawable.wifi);
	            	  break;
	              case 2:
	            	  img.setImageResource(R.drawable.bluetooth);
	            	  break;
	            	  
	            	  
	              case 3:
	            	  img.setImageResource(R.drawable.more);
	            	  break;
	            	  
	              case 5:
	            	  img.setImageResource(R.drawable.call);
	            	  break;	  
	            	  
	              case 6:
	            	  img.setImageResource(R.drawable.sound);
	            	  break;
	            	  
	              case 7:
	            	  img.setImageResource(R.drawable.ic_dis);
	            	  break;
	              case 8:
	            	  img.setImageResource(R.drawable.application);
	            	  break;
	              case 10:
	            	  img.setImageResource(R.drawable.acc);
	            	  break;
	              case 11:
	            	  img.setImageResource(R.drawable.location);
	            	  break;
	              case 12:
	            	  img.setImageResource(R.drawable.lock);
	            	  break;
	             /* case 14:
	            	  img.setImageResource(R.drawable.acc));
	            	  break;*/
	              case 14:
	            	  img.setImageResource(R.drawable.date);
	            	  break;
	              case 15:
	            	  img.setImageResource(R.drawable.acce);
	            	  break;
	              case 16:
	                 	  img.setImageResource(R.drawable.about_us);
	            	  break;
	           
	              }
	              
			}
		}
		return v;
	}

}

package bingo.binaryfork.com.bingo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bingo.binaryfork.com.bingo.flickr.model.Photo;

public class ImagesAdapter extends BaseAdapter {

    private ArrayList<Photo> data;
    private LayoutInflater inflater;
    private Context context;

    public ImagesAdapter(ArrayList<Photo> data, Activity activity) {
        this.data = data;
        this.context = activity.getApplicationContext();
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.hashCode();
    }

    private class ViewHolder {
        public DynamicHeightImageView image;

        public ViewHolder(View convertView) {
            image = (DynamicHeightImageView) convertView.findViewById(R.id.image);
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Photo item = data.get(position);

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_image, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Honour height aspect ratio because Staggered Grid needs to know what size to allocate
        // for the image.
        viewHolder.image.setHeightRatio((float) item.height_m / item.width_m);

        Picasso.with(context).load(item.url_m).into(viewHolder.image);
        return convertView;
    }
}

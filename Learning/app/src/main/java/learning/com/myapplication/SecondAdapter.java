package learning.com.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class SecondAdapter extends BaseAdapter implements View.OnClickListener {
    Context context;
    List<AvatarModel> avatarModels;
    LayoutInflater inflter;

    public SecondAdapter(Context applicationContext, List<AvatarModel> avatarModels) {
        this.context = applicationContext;
        this.avatarModels = avatarModels;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return avatarModels.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.row_list, null); // inflate the layout
        ImageView icon = view.findViewById(R.id.imageView);
        ImageView checked = view.findViewById(R.id.checked); // get the reference of ImageView
        AvatarModel avatarModel = avatarModels.get(i);

        icon.setImageResource(avatarModel.getDrawableId()); // set logo images
        view.setOnClickListener(this);
        view.setTag(i);

        if (avatarModel.isSelected()) {
            checked.setVisibility(View.VISIBLE);
        } else {
            checked.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null) {
            int index = (int) v.getTag();
            for (AvatarModel avatar : avatarModels) {
                avatar.setSelected(false);
            }
            avatarModels.get(index).setSelected(true);
            notifyDataSetChanged();
        }
    }
}
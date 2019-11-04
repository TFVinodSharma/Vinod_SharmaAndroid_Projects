package shifoo.com.app.modules.login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import oku.app.R;
import shifoo.com.app.Interface.AvtaarSelectInterface;
import shifoo.com.app.Interface.HeroSelectionInterface;
import shifoo.com.app.modules.login.model.AvatarModel;

public class SecondAdapter extends BaseAdapter implements View.OnClickListener {
    Context context;
    List<AvatarModel> avatarModels;
    LayoutInflater inflter;


    OnItemClicked onItemClicked;
    private AvtaarSelectInterface avtaarSelectInterface;

    public SecondAdapter(Context applicationContext, List<AvatarModel> avatarModels,AvtaarSelectInterface listener) {
        this.context = applicationContext;
        this.avatarModels = avatarModels;
        inflter = (LayoutInflater.from(applicationContext));
        this.avtaarSelectInterface = listener;
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
        view = inflter.inflate(R.layout.row_avatar, null); // inflate the layout
        ImageView icon = view.findViewById(R.id.imageView);
        ImageView checked = view.findViewById(R.id.checked);
        RelativeLayout relativeLayout = view.findViewById(R.id.relativelayout1);// get the reference of ImageView
        AvatarModel avatarModel = avatarModels.get(i);

        icon.setImageResource(avatarModel.getDrawableId()); // set logo images
        view.setOnClickListener(this);
        view.setTag(i);


        if (avatarModel.isSelected()) {
            checked.setVisibility(View.VISIBLE);
            relativeLayout.setBackgroundResource(R.drawable.drawable_avtaar_bg);
        } else {
            checked.setVisibility(View.GONE);
            relativeLayout.setBackgroundResource(R.drawable.drawable_inner_back_layout);
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
            onItemClicked.onItemCLicked();
            avtaarSelectInterface.onSelectAvtaar(index);
        }
    }

    public void setOnItemClicked(OnItemClicked onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    public interface OnItemClicked {

        void onItemCLicked();
    }

}
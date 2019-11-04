package shifoo.com.app.modules.profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import oku.app.R;

public class LeaderShipAdapter extends RecyclerView.Adapter<LeaderShipAdapter.MyViewHolder> {
    private int photos[];

    public LeaderShipAdapter(int[] photos) {

        this.photos = photos;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.row_leadership, viewGroup, false);
        MyViewHolder imageViewHolder1 = new MyViewHolder(view);
        return imageViewHolder1;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        int image_id = photos[position];
        myViewHolder.LeadrerImageView.setImageResource(image_id);
    }

    @Override
    public int getItemCount() {
        return photos.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView LeadrerImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            LeadrerImageView = itemView.findViewById(R.id.leaderimage);
        }
    }
}

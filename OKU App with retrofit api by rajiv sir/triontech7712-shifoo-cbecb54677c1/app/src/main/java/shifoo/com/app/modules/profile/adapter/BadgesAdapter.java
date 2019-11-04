package shifoo.com.app.modules.profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import oku.app.R;


public class BadgesAdapter extends RecyclerView.Adapter<BadgesAdapter.MyViewHolder> {


    private  int logos[];

    public  BadgesAdapter(int[] logos)
    {

        this.logos = logos;

    }

    @NonNull
    @Override
    public BadgesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.row_badges, viewGroup, false);
        MyViewHolder imageviewHolder= new MyViewHolder(view);
        return imageviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BadgesAdapter.MyViewHolder myViewHolder, int position) {
        int imageid=logos[position];
        myViewHolder.BadgesImageView.setImageResource(imageid);
    }

    @Override
    public int getItemCount() {
        return logos.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView BadgesImageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            BadgesImageView=itemView.findViewById(R.id.badgesimageview);
        }
    }
}

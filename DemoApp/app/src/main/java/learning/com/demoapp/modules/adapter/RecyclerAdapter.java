package learning.com.demoapp.modules.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import learning.com.demoapp.R;
import learning.com.demoapp.modules.view.DetailsActivity;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private String[] data1;
    private int[] data2;




    public RecyclerAdapter(String[] data1, int[] data2 ) {
        this.data1 = data1;
        this.data2 = data2;

    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_recycler_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String pnd = data1[position];
        holder.txtView.setText(pnd);


       int rnk = data2[position];
        holder.imageView.setImageResource(rnk);
    }

    @Override
    public int getItemCount() {
        return data1.length;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        TextView txtView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            txtView = itemView.findViewById(R.id.imageTittle);
            imageView = itemView.findViewById(R.id.getimage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("image", data2[getAdapterPosition()]); // put image data in Intent
                    intent.putExtra("text", data1[getAdapterPosition()]); // put image data in Intent

                    context.startActivity(intent);

                }
            });
        }
    }


    }

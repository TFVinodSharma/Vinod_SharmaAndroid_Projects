package shifoo.com.app.modules.home.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import oku.app.R;

/*
public class ChooseHerosAdaptertwo {
}
*/
public class ChooseHerosAdaptertwo extends RecyclerView.Adapter<ChooseHerosAdaptertwo.MyViewHolder> {


    private int logos[];

    public ChooseHerosAdaptertwo(int[] logos) {

        this.logos = logos;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public ChooseHerosAdaptertwo.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.shiffo, viewGroup, false);
        MyViewHolder imageviewHolder = new MyViewHolder(view);


        return imageviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseHerosAdaptertwo.MyViewHolder myViewHolder, int position) {
        int imageid = logos[position];
        myViewHolder.BadgesImageView.setImageResource(imageid);
    }

    @Override
    public int getItemCount() {
        return logos.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView BadgesImageView;

        TextView TxtNameCelebrity, TxtDesignation;
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            BadgesImageView = itemView.findViewById(R.id.myImageView);
            ProgressBar PHome = itemView.findViewById(R.id.pBar);
            PHome.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
            
            TxtNameCelebrity  =itemView.findViewById(R.id.vinods);
            TxtDesignation =itemView.findViewById(R.id.designaion_celi);

            Typeface adapterHerosTypeFace = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/JosefinSans_Bold.ttf");
            this.TxtNameCelebrity.setTypeface(adapterHerosTypeFace);
            this.TxtDesignation.setTypeface(adapterHerosTypeFace);






        }
    }
}

package shifoo.com.app.modules.chooseheros.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import oku.app.R;
import shifoo.com.app.Interface.HeroSelectionInterface;
import shifoo.com.app.Models.chooseHeroesModel;
import shifoo.com.app.modules.chooseheros.adapter.ChooseHerosAdapter.ChooseHeroesHolder;

public class ChooseHerosAdapter extends RecyclerView.Adapter<ChooseHeroesHolder> {

   // private int checkedPosition = -1;
//    private String [] data1;
//    private String [] data2;
   // TextView Name, Designation;
   private Context mContext;

    private List<chooseHeroesModel> heroesList;

    ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoadingListener imageListener;
    DisplayImageOptions options;

    //  private String [] data3 ;

    private  HeroSelectionInterface heroSelectionInterface;

    public  ChooseHerosAdapter(Context context, List<chooseHeroesModel> list,HeroSelectionInterface listener)
    {

        this.mContext = context;
        this.heroesList= list;
//        this.data1=data1;
//        this.data2=data2;
       // this.data3=data3;
        this.heroSelectionInterface = listener;


        options = new DisplayImageOptions.Builder()
                //.showImageOnFail(R.drawable.ic_error)
//                .showStubImage(R.mipmap.ic_launcher)
                //.showImageForEmptyUri(R.drawable.ic_empty)
                .cacheInMemory()
                .cacheOnDisc().build();

        imageListener = new ImageDisplayListener();
    }

        @Override
    public ChooseHeroesHolder onCreateViewHolder(ViewGroup parent, int viewType) {



            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chooseheros, null);
            ChooseHeroesHolder subs_holder = new ChooseHeroesHolder(v);


        return subs_holder;
    }

    @Override
   public void onBindViewHolder(ChooseHeroesHolder holder, int position) {
       // String name=data1[position];

        final chooseHeroesModel subsItem = heroesList.get(position);

        holder.Name.setText(subsItem.getHero_name());
        holder.Designation.setText(subsItem.getHero_profession());

//        ImageAware imageAware = new ImageViewAware(holder.imageView, false);
//        imageLoader.displayImage(subsItem.getCover_photos(), imageAware, options);



        if(subsItem.getSetSelected() == 0 ){
            holder.mCheckedHeros.setVisibility(View.INVISIBLE);
        }else{
            holder.mCheckedHeros.setVisibility(View.VISIBLE);
        }

        holder.top_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<heroesList.size();i++){
                    final chooseHeroesModel subsItem = heroesList.get(i);
                    if(i == position){
                        subsItem.setSetSelected(1);
                    }else{
                        subsItem.setSetSelected(0);
                    }

                }

                holder.mCheckedHeros.setVisibility(View.VISIBLE);
                heroSelectionInterface.onItemClick(1,position);
                notifyDataSetChanged();
            }
        });


//        myViewHolder.Name.setText(name);
//        String rnk=data2[position];
//        myViewHolder.Designation.setText(rnk);
        /*String wrk=data3[position];
        myViewHolder.imageView.setText(wrk);*/

    }

    @Override
    public int getItemCount() {
        return heroesList.size();
    }

    private static class ImageDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    public class ChooseHeroesHolder extends RecyclerView.ViewHolder {
        TextView Name, Designation;
        private Context context;
        Button mGetStarted;
        ImageView imageView,mCheckedHeros;
LinearLayout top_ll;
        public ChooseHeroesHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            Name = itemView.findViewById(R.id.txt_name);
            Designation = itemView.findViewById(R.id.txt_designation);
            imageView=itemView.findViewById(R.id.image);
            mCheckedHeros=itemView.findViewById(R.id.checkedheros);

            top_ll=itemView.findViewById(R.id.top_ll);

            // mGetStarted=itemView.findViewById(R.id.btn_started);
            Typeface adapterTypeFace = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/JosefinSans_Bold.ttf");
            this.Name.setTypeface(adapterTypeFace);
            this.Designation.setTypeface(adapterTypeFace);
//            this.mGetStarted.setTypeface(adapterTypeFace);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                   // mGetStarted.setBackgroundResource(R.drawable.round);
//                    mGetStarted.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            //Toast.makeText(context, "hELLO", Toast.LENGTH_SHORT).show();
//                            Intent i = new Intent(context, AboutCelebrityActivity.class);
//                            context.startActivity(i);
//                        }
//                    });


                }
            });
        }
    }
}

//
//public class ChooseHerosAdapter extends RecyclerView.Adapter<ChooseHerosAdapter.MyViewHolder> {
//    private List<choo> heroesList;
//
//
//    private Context mContext;
//   // private HistoryClickListener mListener;
//
//    public ChooseHerosAdapter(Context context, List<chooseHeroesModel> subsList) {
//
//
//        this.heroesList = subsList;
//        this.mContext = context;
//       // mListener = (HistoryClickListener) context;
//
//    }
//
//
//    @Override
//    public chooseHeroHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mysubs_listitem, null);
//        MyViewHolder subs_holder = new Subs_Holder(v);
//
//        return subs_holder;
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
////        final History_Model subsItem = SubscriptionList.get(position);
////
////
////        // holder.pendingMonth.setText("3 month pending");
////        holder.dateTime_tv.setText(AppUtilsMethod.parseDateToddMMyyyy(subsItem.getSubscriptionDate()));
////        holder.month_tv.setText(subsItem.getMonth() + " "+ subsItem.getYear());
////
////        holder.mReorderIv.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                clickReorder();
////            }
////        });
////
////        holder.mGiftIv.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                clickGift();
////            }
////        });
//
//
//
//    }
////    public void clickGift() {
////        this.mListener.giftSelect("");
////    }
////
////    public void clickReorder() {
////        this.mListener.reOrderSelect("");
////    }
////    @Override
////    public int getItemCount() {
////        return (null != SubscriptionList ? SubscriptionList.size() : 0);
////    }
//}

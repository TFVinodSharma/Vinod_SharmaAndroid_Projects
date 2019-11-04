package shifoo.com.app.modules.chooseheros.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Queue;

import oku.app.R;
import shifoo.com.app.modules.cardviewquiz.view.QuizMainActivity;


public class DataFragmentAdapter extends RecyclerView.Adapter<DataFragmentAdapter.MyViewHolder> {

    private String[] text1;
    // private String [] text2;

    public DataFragmentAdapter(String[] text1) {
        this.text1 = text1;
        //this.text2=text2;
        // this.data3=data3;
    }


    @NonNull
    @Override
    public DataFragmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_gridviewdata, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DataFragmentAdapter.MyViewHolder myViewHolder, int position) {
        String name = text1[position];
        myViewHolder.Text.setText(name);
       /* String rnk=data2[position];
        myViewHolder.Designation.setText(rnk);
*/
    }

    @Override
    public int getItemCount() {
        return text1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Text, count;
        private Context context;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            LinearLayout LinearGotoCardQuiz = itemView.findViewById(R.id.lineargotocard);
            Text = itemView.findViewById(R.id.textdata);
            //Designation = itemView.findViewById(R.id.txt_designation);
            imageView = itemView.findViewById(R.id.imagedata);
            LinearGotoCardQuiz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateDetail();
                }
            });

        }

        public void updateDetail() {
            Intent intent = new Intent(context, QuizMainActivity.class);
            context.startActivity(intent);
        }
    }
}

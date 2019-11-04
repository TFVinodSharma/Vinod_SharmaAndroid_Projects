package learning.vinodsharma.com.navigationactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class TrueFalseActivity extends AppCompatActivity {
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_false);

        LinearLayout linearLayout = findViewById(R.id.layout);

        TextView CheckPlaindrome = findViewById(R.id.CheckPlainderome);

        CheckPlaindrome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = 121, reversedInteger = 0, remainder, originalInteger;

                originalInteger = num;

                // reversed integer is stored in variable
                while (num != 0) {
                    remainder = num % 10;
                    reversedInteger = reversedInteger * 10 + remainder;
                    num /= 10;
                }

                // palindrome if orignalInteger and reversedInteger are equal
                if (originalInteger == reversedInteger)
                    Toast.makeText(TrueFalseActivity.this, "is a palindrome." + originalInteger, Toast.LENGTH_SHORT).show();
                    //  System.out.println(originalInteger + " is a palindrome.");
                else
                    Toast.makeText(TrueFalseActivity.this, "isnot  a palindrome.", Toast.LENGTH_SHORT).show();

                //System.out.println(originalInteger + " is not a palindrome.");
            }


        });


        RatingBar ratingBar1 = findViewById(R.id.ratingBar1);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(TrueFalseActivity.this, R.style.AppBaseTheme);
                dialog.setContentView(R.layout.row_rating);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.CENTER;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                window.setAttributes(wlp);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                dialog.show();


                Toast.makeText(TrueFalseActivity.this, "Show Rating here ", Toast.LENGTH_SHORT).show();

            }
        });
      /*  ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(TrueFalseActivity.this, "rating bar", Toast.LENGTH_SHORT).show();
            }
        });*/

    }

}

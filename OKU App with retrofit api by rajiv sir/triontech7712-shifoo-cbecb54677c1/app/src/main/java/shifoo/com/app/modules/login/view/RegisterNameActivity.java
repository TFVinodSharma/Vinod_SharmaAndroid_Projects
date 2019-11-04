package shifoo.com.app.modules.login.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import oku.app.R;


public class RegisterNameActivity extends AppCompatActivity {
       @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_name);

        final Button mWelcomeProceed=(Button)findViewById(R.id.welcomeproceed);

       TextView  mTextHeading = (TextView) findViewById(R.id.nameheading);
           TextView mTextName = (TextView) findViewById(R.id.nameText);
           TextView mTxtHeadingTwo=findViewById(R.id.textHeadingtwo);

        EditText mPersonName=findViewById(R.id.person_name_edt);
           /*PersonName.getBackground().setColorFilter(getResources().getColor(R.color.black),
                   PorterDuff.Mode.SRC_ATOP);*/
          /* PersonName.getBackground().mutate().
                   setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);*/

        Typeface tRegisterName = Typeface.createFromAsset(getAssets(),
                "fonts/JosefinSans_Bold.ttf");
           mTxtHeadingTwo.setTypeface(tRegisterName);
           mPersonName.setTypeface(tRegisterName);
           mTxtHeadingTwo.setTypeface(tRegisterName);
           mWelcomeProceed.setTypeface(tRegisterName);
           mTextHeading.setTypeface(tRegisterName);
           mTextName.setTypeface(tRegisterName);
        /*Typeface tRegisterName1 = Typeface.createFromAsset(getAssets(),
                   "fonts/JosefinSans_Regular.ttf");
           mTxtHeadingTwo.setTypeface(tRegisterName1);*/



        mPersonName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0 && s.equals("")) {
                    mWelcomeProceed.setBackground(getDrawable(R.drawable.roundgrey));
                    mWelcomeProceed.setEnabled(false);
                } else {
                    mWelcomeProceed.setBackground(getDrawable(R.drawable.round));
                    mWelcomeProceed.setEnabled(true);

                    mWelcomeProceed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent= new Intent(RegisterNameActivity.this,AvtarActivity.class);
                            intent.putExtra("user_name",mPersonName.getText().toString());
                            startActivity(intent);
                            finish();
                           // Toast.makeText(RegisterNameActivity.this, "string is  not empty", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });

        }
}

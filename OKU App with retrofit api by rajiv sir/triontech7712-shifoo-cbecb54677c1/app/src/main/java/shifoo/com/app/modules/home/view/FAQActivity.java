package shifoo.com.app.modules.home.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import oku.app.R;

public class FAQActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        android.support.v7.widget.Toolbar FaqToolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.faqtoolbar);
        setSupportActionBar(FaqToolBar);
///        getSupportActionBar().setDisplayShowTitleEnabled(true);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.backbutton);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

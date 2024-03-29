package vinod.learning.com.imagecardapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import miaoyongjun.pagetransformer.TransitionEffect;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.zoom).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.zoom:
                ShowZoomActivity.transitionEffect = TransitionEffect.Zoom;
                startActivity(new Intent(MainActivity.this, ShowZoomActivity.class));
                break;

        }
    }
}

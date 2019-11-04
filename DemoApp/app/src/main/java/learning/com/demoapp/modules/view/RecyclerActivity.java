package learning.com.demoapp.modules.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import learning.com.demoapp.R;
import learning.com.demoapp.modules.adapter.RecyclerAdapter;

public class RecyclerActivity extends AppCompatActivity {
    public String[] Name = {"Dhoni", "Cool ", "BackGround", "Nature", "Evening", "Ship","Tiger","Gujrati River","Healthy River","Dhoni", "Cool ", "BackGround", "Nature", "Evening", "Ship","Tiger","Gujrati River","Healthy River"};

    int logos[] = {R.drawable.dhoni, R.drawable.cool, R.drawable.background, R.drawable.nature,
            R.drawable.evening, R.drawable.ship,R.drawable.tiger,R.drawable.gujrati,R.drawable.healtyriver,R.drawable.dhoni, R.drawable.cool, R.drawable.background, R.drawable.nature,
            R.drawable.evening, R.drawable.ship,R.drawable.tiger,R.drawable.gujrati,R.drawable.healtyriver};


    RecyclerView Recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Recycler = findViewById(R.id.recyclerview);

        Recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Recycler.setAdapter(new RecyclerAdapter(Name, logos));



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.gridview:
                Recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                Recycler.setAdapter(new RecyclerAdapter(Name, logos));
                StaggeredGridLayoutManager staggeredGridLayoutManager =
                        new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

                Recycler.setLayoutManager(staggeredGridLayoutManager);

                Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.verticalView:
                Recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                Recycler.setAdapter(new RecyclerAdapter(Name, logos));

                Toast.makeText(getApplicationContext(), "Item 2 Selected", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

package learning.com.myapplication;

import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    GridView simpleGrid;
    int logos[] = {R.drawable.avtra1, R.drawable.avtar2, R.drawable.avtar3, R.drawable.avtar5,
            R.drawable.avtar6, R.drawable.avtra1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        simpleGrid = (GridView) findViewById(R.id.simpleGridView); // init GridView
        // Create an object of CustomAdapter and set Adapter to GirdView


        List<AvatarModel> avatarModels = getAvatarModels(logos);

        SecondAdapter customAdapter = new SecondAdapter(getApplicationContext(), avatarModels);
        simpleGrid.setAdapter(customAdapter);
        // implement setOnItemClickListener event on GridView

    }

    private List<AvatarModel> getAvatarModels(int[] logos) {

        List<AvatarModel> avatarModelList = new ArrayList<>();

        for (int drawable : logos) {
            AvatarModel avtarModel = new AvatarModel();
            avtarModel.setSelected(false);
            avtarModel.setDrawableId(drawable);
            avatarModelList.add(avtarModel);
        }

        return avatarModelList;
    }
}

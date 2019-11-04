package bingo.binaryfork.com.bingo;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;

import bingo.binaryfork.com.bingo.flickr.model.PhotosResponse;


public class MainActivity extends BaseSpiceActivity implements AbsListView.OnScrollListener {

    private AbsListView gridView;
    private View reset;

    /**
     * The last top position of the first visible view in the grid after scrolling.
     * Used to detect the direction of scrolling.
      */
    private int lastScrollPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reset = findViewById(R.id.reset);
        gridView = (AbsListView) findViewById(R.id.grid_view);
        gridView.setOnScrollListener(this);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanCache();
                loadRecentFlickrPhotos();
            }
        });

        loadRecentFlickrPhotos();
    }

    @Override
    protected void onPhotosLoaded(PhotosResponse images) {
        ImagesAdapter adapter = new ImagesAdapter(images.photos.photo, this);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        View topChild = view.getChildAt(1);
        if (topChild == null) {
            return;
        } else if (firstVisibleItem == 0 && reset.getAlpha() != 1) {
            // Show reset button when scrolled to the top.
            reset.animate().alpha(1);
            return;
        }
        int scrollPosition = -topChild.getTop();
        if (lastScrollPosition > scrollPosition) {
            // Scrolling down.
            if (reset.getAlpha() < 1f) {
                reset.setAlpha(reset.getAlpha() + .05f);
            }
        } else if (lastScrollPosition < scrollPosition) {
            // Scrolling up.
            if (reset.getAlpha() > 0f) {
                reset.setAlpha(reset.getAlpha() - .05f);
            }
        }
        lastScrollPosition = scrollPosition;
    }
}
package bingo.binaryfork.com.bingo;

import android.support.v7.app.ActionBarActivity;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import bingo.binaryfork.com.bingo.flickr.FlickrRequest;
import bingo.binaryfork.com.bingo.flickr.FlickrSpiceService;
import bingo.binaryfork.com.bingo.flickr.model.PhotosResponse;

public abstract class BaseSpiceActivity extends ActionBarActivity {

    protected SpiceManager spiceManager = new SpiceManager(FlickrSpiceService.class);

    protected abstract void onPhotosLoaded(PhotosResponse images);

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    protected void cleanCache() {
        spiceManager.removeAllDataFromCache();
    }

    protected void loadRecentFlickrPhotos() {
        FlickrRequest instagramRequest = new FlickrRequest();
        spiceManager.execute(instagramRequest,
                "recent",
                DurationInMillis.ALWAYS_RETURNED, // Always return data from cache if it exists.
                new FlickrRequestListener());
    }

    /**
     * SpiceManager callbacks.
     */
    private class FlickrRequestListener implements RequestListener<PhotosResponse> {

        @Override
        public void onRequestFailure(SpiceException e) {
            e.printStackTrace();
        }

        @Override
        public void onRequestSuccess(PhotosResponse images) {
            onPhotosLoaded(images);
        }
    }
}

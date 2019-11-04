package bingo.binaryfork.com.bingo.flickr;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import bingo.binaryfork.com.bingo.flickr.endpoints.FlickrPhotos;
import bingo.binaryfork.com.bingo.flickr.model.PhotosResponse;

/**
 * Request for flickr recent photos.
 */
public class FlickrRequest extends RetrofitSpiceRequest<PhotosResponse, FlickrPhotos> {

    public FlickrRequest() {
        super(PhotosResponse.class, FlickrPhotos.class);
    }

    @Override
    public PhotosResponse loadDataFromNetwork() {
        return getService().getRecentPhotos();
    }
}
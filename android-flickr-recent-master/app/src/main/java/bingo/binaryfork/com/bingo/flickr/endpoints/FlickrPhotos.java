package bingo.binaryfork.com.bingo.flickr.endpoints;


import bingo.binaryfork.com.bingo.flickr.model.PhotosResponse;
import retrofit.http.GET;

public interface FlickrPhotos {

    @GET("/rest/?method=flickr.photos.getRecent")
    public PhotosResponse getRecentPhotos();

}
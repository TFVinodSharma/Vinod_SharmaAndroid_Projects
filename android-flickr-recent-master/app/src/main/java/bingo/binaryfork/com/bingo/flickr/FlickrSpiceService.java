package bingo.binaryfork.com.bingo.flickr;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class FlickrSpiceService extends RetrofitGsonSpiceService {

    private static final String BASE_URL = "https://api.flickr.com/services";

    /**
     * Available image sizes are described here: https://www.flickr.com/services/api/misc.urls.html
     */
    private final static String IMAGE_SIZE_PARAMTER = "url_m";

    /**
     * Create a Builder with specified parameters for flickr API.
     */
    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        RestAdapter.Builder builder = super.createRestAdapterBuilder();
        builder.setEndpoint(BASE_URL);
        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addQueryParam("api_key", "6c67ac42519cfe25a38ff0cce1704139");
                request.addQueryParam("format", "json");
                request.addQueryParam("nojsoncallback", "1");
                request.addQueryParam("per_page", "50");
                request.addQueryParam("extras", IMAGE_SIZE_PARAMTER);
            }
        });
        builder.setLogLevel(RestAdapter.LogLevel.FULL);
        return builder;
    }

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }

}
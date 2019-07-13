package demos.okan.doordash.service.repository.api;

import android.util.Log;

import java.util.List;

import demos.okan.doordash.service.models.Restaurant;
import demos.okan.doordash.service.repository.RestaurantRepository;
import demos.okan.doordash.service.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantApiClient {

    private static final String TAG = "RestaurantApiClient";

    /* Single Ton Instance */
    private static volatile RestaurantApiClient instance;

    /* Retrofit API Interface */
    private final RestaurantsApi restaurantsApi;

    /**
     * Thread-Safe SingleTon method.
     *
     * @return
     */
    public static RestaurantApiClient getInstance() {

        if (instance == null) {

            synchronized (RestaurantRepository.class) {
                instance = new RestaurantApiClient();
            }
        }

        return instance;
    }

    private RestaurantApiClient() {

        /* Build from Base API URL and GSON Factory */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestaurantsApi.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        /* Create Service */
        restaurantsApi = retrofit.create(RestaurantsApi.class);
    }

    /**
     * This method uses Constant Values for Latitude, Longitude and Limit parameters, so passing
     * page (offset) is enough for infinite scrolling.
     *
     * @param page
     */
    public void retrieveRestaurants(int page, final RestaurantApiCallback callback) {

        /* Create Call Instance to send GET Request with RetroFit */
        Call<List<Restaurant>> apiCall = restaurantsApi.searchRestaurants(Constants.LATITUDE, Constants.LONGITUDE, page, Constants.PAGE_SIZE_LARGE);

        /* Execute Call in a background Thread */
        apiCall.enqueue(new Callback<List<Restaurant>>() {

            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {

                /* Check If Success */
                if (response.isSuccessful()) {

                    /* Send Error Message back with a Callback interface */
                    if (callback != null) callback.onSuccess(response.body());

                } else {

                    Log.e(TAG, "onResponse: " + response.message());

                    /* Send Error Message back with a Callback interface */
                    if (callback != null) callback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {

                String errorMessage;
                if (t.getMessage() == null) {
                    errorMessage = "unknown error";
                } else {
                    errorMessage = t.getMessage();
                }

                Log.d(TAG, "onFailure: " + errorMessage);

                /* Send Error Message back with a Callback interface */
                if (callback != null) callback.onFailure(errorMessage);
            }
        });
    }

    public interface RestaurantApiCallback {

        /**
         * Called when Restaurants API is called Successfully.
         *
         * @param restaurants
         */
        void onSuccess(List<Restaurant> restaurants);

        /**
         * Called when Restaurants API fails.
         *
         * @param errorMessage
         */
        void onFailure(String errorMessage);
    }
}

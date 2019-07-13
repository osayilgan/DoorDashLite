package demos.okan.doordash.service.repository.api;

import java.util.List;

import demos.okan.doordash.service.models.Restaurant;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestaurantsApi {

    String BASE_API_URL = "https://api.doordash.com/";

    @GET("/v2/restaurant/")
    Call<List<Restaurant>> searchRestaurants(@Query("lat") double lat, @Query("lng") double lng, @Query("offset") int offset, @Query("limit") int limit);
}

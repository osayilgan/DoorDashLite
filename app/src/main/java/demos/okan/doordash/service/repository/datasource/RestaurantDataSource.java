package demos.okan.doordash.service.repository.datasource;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import java.util.ArrayList;
import java.util.List;

import demos.okan.doordash.service.models.NetworkState;
import demos.okan.doordash.service.models.Restaurant;
import demos.okan.doordash.service.repository.api.RestaurantApiClient;

/**
 * Data Source Class to extend from PageKeyedDataSource.
 * Here we can load Restaurants Page by Page which is the given Integer parameter.
 */
public class RestaurantDataSource extends PageKeyedDataSource<Integer, Restaurant> {

    private RestaurantApiClient apiClient;
    private MutableLiveData<NetworkState> networkStateMutableLiveData;

    public RestaurantDataSource(RestaurantApiClient apiClient) {

        /* Init API Client */
        this.apiClient = apiClient;

        /* init Network State */
        networkStateMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<NetworkState> getNetworkStateMutableLiveData() {
        return networkStateMutableLiveData;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Restaurant> callback) {

        /* Update Status */
        networkStateMutableLiveData.postValue(NetworkState.LOADING);

        apiClient.retrieveRestaurants(0, new RestaurantApiClient.RestaurantApiCallback() {

            @Override
            public void onSuccess(List<Restaurant> restaurants) {
                callback.onResult(restaurants, null, 1);

                /* Update Status */
                networkStateMutableLiveData.postValue(NetworkState.LOADED);
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onResult(new ArrayList<>(), null, 1);

                /* Update Status */
                networkStateMutableLiveData.postValue(NetworkState.FAILED);
            }
        });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Restaurant> callback) {

        /* Update Status */
        networkStateMutableLiveData.postValue(NetworkState.LOADING);

        apiClient.retrieveRestaurants(params.key, new RestaurantApiClient.RestaurantApiCallback() {

            @Override
            public void onSuccess(List<Restaurant> restaurants) {
                callback.onResult(restaurants, params.key+1);

                /* Update Status */
                networkStateMutableLiveData.postValue(NetworkState.LOADED);
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onResult(new ArrayList<>(), params.key+1);

                /* Update Status */
                networkStateMutableLiveData.postValue(NetworkState.FAILED);
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Restaurant> callback) { /* Ignore */ }
}

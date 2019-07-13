package demos.okan.doordash.service.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import demos.okan.doordash.service.models.NetworkState;
import demos.okan.doordash.service.models.Restaurant;
import demos.okan.doordash.service.repository.api.RestaurantApiClient;
import demos.okan.doordash.service.repository.datasource.RestaurantDataSource;
import demos.okan.doordash.service.repository.datasource.RestaurantDataSourceFactory;
import demos.okan.doordash.service.util.Constants;

public class RestaurantRepository {

    private int NUMBERS_OF_THREADS = 3;

    /* Repository SingleTon Instance */
    private static volatile RestaurantRepository instance;

    private LiveData<PagedList<Restaurant>> restaurantsLiveData;
    private LiveData<NetworkState> networkState;

    private RestaurantDataSourceFactory dataSourceFactory;

    /**
     * Thread-Safe SingleTon method.
     *
     * @return
     */
    public static RestaurantRepository getInstance() {

        if (instance == null) {
            synchronized (RestaurantRepository.class) {
                instance = new RestaurantRepository();
            }
        }

        return instance;
    }
    
    private RestaurantRepository() {

        dataSourceFactory = new RestaurantDataSourceFactory(RestaurantApiClient.getInstance());

        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(Constants.PAGE_SIZE_LARGE)
                .setPageSize(Constants.PAGE_SIZE_LARGE)
                .build();

        Executor executor = Executors.newFixedThreadPool(NUMBERS_OF_THREADS);
        restaurantsLiveData = new LivePagedListBuilder<>(dataSourceFactory, pagedListConfig).setFetchExecutor(executor).build();

        /* Transform Network State */
        networkState = Transformations.switchMap(dataSourceFactory.getDataSourceLiveData(), RestaurantDataSource::getNetworkStateMutableLiveData);
    }

    /**
     * Get Live Data for Restaurants.
     *
     * @return
     */
    public LiveData<PagedList<Restaurant>> getRestaurants() {
        return restaurantsLiveData;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    /**
     * Invalidates data loaded from DataSource in DataSourceFactory.
     */
    public void invalidate() {
        dataSourceFactory.invalidate();
    }
}

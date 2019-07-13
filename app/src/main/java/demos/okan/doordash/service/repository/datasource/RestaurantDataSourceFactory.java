package demos.okan.doordash.service.repository.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import demos.okan.doordash.service.models.Restaurant;
import demos.okan.doordash.service.repository.api.RestaurantApiClient;

public class RestaurantDataSourceFactory extends DataSource.Factory<Integer, Restaurant> {

    private RestaurantApiClient apiClient;
    private MutableLiveData<RestaurantDataSource> dataSourceLiveData;

    public RestaurantDataSourceFactory(RestaurantApiClient apiClient) {
        this.apiClient = apiClient;
        dataSourceLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource<Integer, Restaurant> create() {

        RestaurantDataSource mDataSource = new RestaurantDataSource(apiClient);
        dataSourceLiveData.postValue(mDataSource);
        return mDataSource;
    }

    public MutableLiveData<RestaurantDataSource> getDataSourceLiveData() {
        return dataSourceLiveData;
    }
}

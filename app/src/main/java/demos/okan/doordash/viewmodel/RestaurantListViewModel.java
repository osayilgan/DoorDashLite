package demos.okan.doordash.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import demos.okan.doordash.service.models.NetworkState;
import demos.okan.doordash.service.models.Restaurant;
import demos.okan.doordash.service.repository.RestaurantRepository;

public class RestaurantListViewModel extends ViewModel {

    private RestaurantRepository repository;

    public RestaurantListViewModel() {
        repository = RestaurantRepository.getInstance();
    }

    public LiveData<PagedList<Restaurant>> getRestaurants() {
        return repository.getRestaurants();
    }

    public LiveData<NetworkState> getNetworkState() {
        return repository.getNetworkState();
    }
}

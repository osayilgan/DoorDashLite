package demos.okan.doordash.view;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import demos.okan.doordash.R;
import demos.okan.doordash.service.models.NetworkState;
import demos.okan.doordash.viewmodel.RestaurantListViewModel;

public class RestaurantListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "RestaurantListActivity";

    private RestaurantListViewModel mRestaurantListViewModel;

    /* UI */
    private RecyclerView restaurantsRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    /* Adapters */
    private RestaurantListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Set Toolbar Title */
        setToolbarTitle();

        /* init UI */
        initUI();

        /* init View Model */
        mRestaurantListViewModel = ViewModelProviders.of(this).get(RestaurantListViewModel.class);

        /* subscribe observers */
        subscribeRestaurantListObserver();
        subscribeToNetworkState();
    }

    /**
     * Set default toolbar title.
     */
    private void setToolbarTitle() {
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) mActionBar.setTitle(R.string.restaurant_list_activity_title);
    }

    /**
     * Initializes Recycler View and Adapters.
     */
    private void initUI() {

        /* Recycler View */
        restaurantsRecyclerView = findViewById(R.id.restaurantsRecyclerView);
        restaurantsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        adapter = new RestaurantListAdapter();
        restaurantsRecyclerView.setAdapter(adapter);

        /* Progress Bar */
        swipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * Observes Restaurants from ViewModel.
     */
    private void subscribeRestaurantListObserver() {

        mRestaurantListViewModel.getRestaurants().observe(this, restaurants -> {

            /* Submit New DataSet to the Recycler View */
            adapter.submitList(restaurants);
        });
    }

    /**
     * Observes Network State Changes.
     */
    private void subscribeToNetworkState() {

        Handler mHandler = new Handler();

        mRestaurantListViewModel.getNetworkState().observe(this, networkState -> {

            if (networkState == NetworkState.LOADING) {
                swipeRefreshLayout.setRefreshing(true);
            } else {
                mHandler.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 1000);
            }

            Log.d(TAG, "Network Status : " + networkState.getMsg());
        });
    }

    @Override
    public void onRefresh() {
        mRestaurantListViewModel.invalidate();
    }
}

package demos.okan.doordash.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import demos.okan.doordash.R;
import demos.okan.doordash.service.models.Restaurant;

public class RestaurantListAdapter extends PagedListAdapter<Restaurant, RestaurantListAdapter.RestaurantItemViewHolder> {

    public RestaurantListAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public RestaurantItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurants_adapter_item, parent, false);
        return new RestaurantItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantItemViewHolder holder, int position) {

        Restaurant restaurant = getItem(position);
        if (restaurant != null) holder.bindData(restaurant);
        else holder.clear();
    }

    class RestaurantItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView restaurantLogo;
        private TextView  restaurantTitle;
        private TextView  restaurantDescription;
        private TextView  restaurantStatus;

        RestaurantItemViewHolder(View itemView) {
            super(itemView);

            /* init UI */
            restaurantLogo = itemView.findViewById(R.id.restaurantImage);
            restaurantTitle = itemView.findViewById(R.id.restaurantTitle);
            restaurantDescription = itemView.findViewById(R.id.restaurantDescription);
            restaurantStatus = itemView.findViewById(R.id.restaurantStatus);
        }

        /**
         * Binds Restaurant Data to ItemView.
         *
         * @param restaurant
         */
        void bindData(Restaurant restaurant) {

            /* Load Image */
            Picasso.get().load(restaurant.getCoverImageUrl()).placeholder(R.drawable.baseline_photo_24).into(restaurantLogo);

            /* Bind Restaurant Details */
            restaurantTitle.setText(restaurant.getName());
            restaurantDescription.setText(restaurant.getDescription());
            restaurantStatus.setText(restaurant.getStatus());
        }

        void clear() {
            itemView.invalidate();
            restaurantLogo.invalidate();
            restaurantTitle.invalidate();
            restaurantDescription.invalidate();
            restaurantStatus.invalidate();
        }
    }

    private static DiffUtil.ItemCallback<Restaurant> DIFF_CALLBACK = new DiffUtil.ItemCallback<Restaurant>() {

        @Override
        public boolean areItemsTheSame(Restaurant oldRestaurant, Restaurant newRestaurant) {
            return oldRestaurant.getId() == newRestaurant.getId();
        }

        @Override
        public boolean areContentsTheSame(Restaurant oldRestaurant, Restaurant newRestaurant) {
            return oldRestaurant.getDescription().equals(newRestaurant.getDescription());
        }
    };
}

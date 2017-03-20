package com.anna.cookingtime.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anna.cookingtime.R;
import com.anna.cookingtime.interfaces.APICalls;
import com.anna.cookingtime.interfaces.FragmentRequestListener;
import com.anna.cookingtime.models.Dish;
import com.anna.cookingtime.views.BetterImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DishRecyclerViewAdapter
        extends RecyclerView.Adapter<DishRecyclerViewAdapter.PeopleHolder> {

    private final FragmentRequestListener fragmentRequestListener;
    private final APICalls calls;
    private List<Dish> dishList;

    public DishRecyclerViewAdapter(
            List<Dish> dishList,
            FragmentRequestListener fragmentRequestListener,
            APICalls calls) {
        this.dishList = dishList;
        this.fragmentRequestListener = fragmentRequestListener;
        this.calls = calls;
    }

    @Override
    public PeopleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dish, parent, false);
        return new PeopleHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(PeopleHolder holder, int position) {
        final Dish dish = dishList.get(position);
        holder.dishPhoto.loadImage(dish.getImageUrl(), holder.dishProgressBar);
        holder.dishName.setText(dish.getName());
        holder.dishTime.setText(String.valueOf(dish.getCookingTime()));
    }

    @Override
    public long getItemId(int position) {
        return dishList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public void setNewData(List<Dish> dishList) {
        this.dishList = dishList;
        notifyDataSetChanged();
    }

    public long getDish(int position) {
        return dishList.get(position).getId();
    }

    public class PeopleHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.dishPhoto)
        BetterImageView dishPhoto;
        @BindView(R.id.dishName)
        TextView dishName;
        @BindView(R.id.dishTime)
        TextView dishTime;
        @BindView(R.id.dishProgressBar)
        ProgressBar dishProgressBar;

        public PeopleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}

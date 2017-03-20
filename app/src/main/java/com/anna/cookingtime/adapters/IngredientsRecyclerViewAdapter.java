package com.anna.cookingtime.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anna.cookingtime.R;
import com.anna.cookingtime.models.Dish;
import com.anna.cookingtime.models.Ingredients;
import com.anna.cookingtime.views.BetterImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsRecyclerViewAdapter
        extends RecyclerView.Adapter<IngredientsRecyclerViewAdapter.IngredientsHolder> {

    private List<Ingredients> ingredientsList;

    public IngredientsRecyclerViewAdapter(List<Ingredients> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    @Override
    public IngredientsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredients, parent, false);
        return new IngredientsHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(IngredientsHolder holder, int position) {
        final Ingredients ingredient = ingredientsList.get(position);
        holder.nameIngredient.setText(ingredient.getName());
        holder.quantityIngredient.setText(String.valueOf(ingredient.getQuantity()));
        holder.unitIngredient.setText(String.valueOf(ingredient.getUnit()));
    }

    @Override
    public long getItemId(int position) {
        return ingredientsList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }

    public void setNewData(List<Ingredients> ingredientsList) {
        this.ingredientsList = ingredientsList;
        notifyDataSetChanged();
    }

    public long getDish(int position) {
        return ingredientsList.get(position).getId();
    }

    class IngredientsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nameIngredient)
        TextView nameIngredient;
        @BindView(R.id.quantityIngredient)
        TextView quantityIngredient;
        @BindView(R.id.unitIngredient)
        TextView unitIngredient;

        IngredientsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}

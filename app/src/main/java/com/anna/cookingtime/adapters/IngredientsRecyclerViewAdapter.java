package com.anna.cookingtime.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anna.cookingtime.R;
import com.anna.cookingtime.models.Dish;
import com.anna.cookingtime.models.Ingredients;
import com.anna.cookingtime.views.BetterImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsRecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Integer> selectIngredients;
    private List<Ingredients> ingredientsList;
    private boolean isCheckBox;

    public IngredientsRecyclerViewAdapter(List<Ingredients> ingredientsList, ArrayList<Integer> selectIngredients) {
        this.ingredientsList = ingredientsList;
        if (selectIngredients == null) {
            this.isCheckBox = false;
        } else {
            this.isCheckBox = true;
        }
        this.selectIngredients = selectIngredients;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isCheckBox) {
            View listItemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ingredients_check_box, parent, false);
            return new IngredientsCheckBoxHolder(listItemView);
        }
        View listItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredients, parent, false);
        return new IngredientsHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Ingredients ingredient = ingredientsList.get(position);
        if (isCheckBox) {
            ((IngredientsCheckBoxHolder) holder).nameIngredient.setText(ingredient.getName());
            if (selectIngredients.contains(ingredient.getId())) {
                ((IngredientsCheckBoxHolder) holder).checkboxIngredient.setChecked(true);
            } else {
                ((IngredientsCheckBoxHolder) holder).checkboxIngredient.setChecked(false);
            }
//            ((IngredientsCheckBoxHolder) holder).checkboxIngredient.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    CheckBox checkBox = (CheckBox) v;
//                    if (checkBox.isChecked()) {
//                        checkBox.setChecked(false);
//                        selectIngredients.remove(ingredient.getId());
//                    } else {
//                        checkBox.setChecked(true);
//                        selectIngredients.add(ingredient.getId());
//                    }
//                }
//            });
            ((IngredientsCheckBoxHolder) holder).checkBoxContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox = ((IngredientsCheckBoxHolder) holder).checkboxIngredient;
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        selectIngredients.remove(ingredient.getId());
                    } else {
                        checkBox.setChecked(true);
                        selectIngredients.add((int) ingredient.getId());
                    }
                }
            });
        } else {
            ((IngredientsHolder) holder).nameIngredient.setText(ingredient.getName());
            ((IngredientsHolder) holder).quantityIngredient.setText(String.valueOf(ingredient.getQuantity()));
            ((IngredientsHolder) holder).unitIngredient.setText(String.valueOf(ingredient.getUnit()));
        }
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

    public ArrayList<Integer> getSelectedList() {
        return selectIngredients;
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

    class IngredientsCheckBoxHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.checkBoxContainer)
        RelativeLayout checkBoxContainer;
        @BindView(R.id.nameIngredient)
        TextView nameIngredient;
        @BindView(R.id.checkboxIngredient)
        CheckBox checkboxIngredient;

        IngredientsCheckBoxHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}

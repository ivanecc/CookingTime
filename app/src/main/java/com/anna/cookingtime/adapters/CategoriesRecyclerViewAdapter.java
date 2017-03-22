package com.anna.cookingtime.adapters;

import android.graphics.Typeface;
import android.os.ParcelUuid;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anna.cookingtime.CookingTimeApp;
import com.anna.cookingtime.R;
import com.anna.cookingtime.interfaces.FragmentRequestListener;
import com.anna.cookingtime.models.Category;
import com.anna.cookingtime.models.Dish;
import com.anna.cookingtime.utils.FontManager;
import com.anna.cookingtime.views.BetterImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesRecyclerViewAdapter
        extends RecyclerView.Adapter<CategoriesRecyclerViewAdapter.CategoryHolder> {

    private final FragmentRequestListener fragmentRequestListener;
    private List<Category> categoryList;

    public CategoriesRecyclerViewAdapter(
            List<Category> dishList,
            FragmentRequestListener fragmentRequestListener) {
        this.categoryList = dishList;
        this.fragmentRequestListener = fragmentRequestListener;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        final Category category = categoryList.get(position);
        Typeface iconFont = FontManager.getTypeface(CookingTimeApp.getAppContext(), FontManager.FONTAWESOME);
        holder.categoryPhoto.setTypeface(iconFont);
        String ss = "\ue603";
        holder.categoryPhoto.setText(ss);
        holder.categoryName.setText(category.getName());
    }

    @Override
    public long getItemId(int position) {
        return categoryList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setNewData(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    public Category getCategory(int position) {
        return categoryList.get(position);
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.categoryPhoto)
        TextView categoryPhoto;
        @BindView(R.id.categoryName)
        TextView categoryName;

        public CategoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}

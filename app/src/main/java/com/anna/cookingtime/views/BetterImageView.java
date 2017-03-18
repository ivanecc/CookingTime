package com.anna.cookingtime.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.anna.cookingtime.CookingTimeApp;
import com.anna.cookingtime.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by iva on 18.03.17.
 */

public class BetterImageView extends android.support.v7.widget.AppCompatImageView {
    public BetterImageView(Context context) {
        super(context);
    }

    public BetterImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BetterImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadImage(String url, final ProgressBar progressBar) {
        progressBar.setVisibility(VISIBLE);
        Glide
                .with(CookingTimeApp.getAppContext())
                .load(url)
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        progressBar.setVisibility(GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(GONE);
                        return false;
                    }
                })
                .dontAnimate()
                .placeholder(R.color.colorAccent)
                .into(BetterImageView.this);
    }
}

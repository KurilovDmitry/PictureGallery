package com.example.dmitrykurilov.picturegallery;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class MyPictureActivity extends AppCompatActivity {

    public static final String EXTRA_PICTURE_ID = "MyPictureActivity.PICTURE_ID";
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_detail);

        imageView = findViewById(R.id.image);
        Integer picture = getIntent().getIntExtra(EXTRA_PICTURE_ID, 0);

        Glide.with(this)
                .load(picture)
                .asBitmap()
                .error(R.drawable.ic_cloud_off_red)
                .listener(new RequestListener<Integer, Bitmap>() {

                    @Override
                    public boolean onException(Exception e, Integer model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Integer model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        onPalette(Palette.from(resource).generate());
                        imageView.setImageBitmap(resource);

                        return false;
                    }

                    public void onPalette(Palette palette) {
                        if (null != palette) {
                            ViewGroup parent = (ViewGroup) imageView.getParent().getParent();
                            parent.setBackgroundColor(palette.getDarkVibrantColor(Color.GRAY));
                        }
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }
}

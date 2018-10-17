package com.example.dmitrykurilov.picturegallery;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4);
        RecyclerView recyclerView = findViewById(R.id.rv_images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        ImageGalleryAdapter adapter = new ImageGalleryAdapter(listAllIdInRaw(), this);
        recyclerView.setAdapter(adapter);
    }

    private Integer[] listAllIdInRaw() {
        List<Integer> pictures = new ArrayList<>();

        Field[] ID_Fields = R.raw.class.getFields();
        for (Field f : ID_Fields) {
            try {
                pictures.add(getResourceId(f.getName()));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        return pictures.toArray(new Integer[pictures.size()]);
    }

    private class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder> {

        private Integer[] pictures;
        private Context context;
        private ImageService imageService;

        public ImageGalleryAdapter(Integer[] pictures, Context context) {
            this.pictures = pictures;
            this.context = context;
            imageService = new ImageService(4, context);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView myImageView;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.myImageView = itemView.findViewById(R.id.iv_picture);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Integer picture = pictures[position];
                    Intent intent = new Intent(context, MyPictureActivity.class);
                    intent.putExtra(MyPictureActivity.EXTRA_PICTURE_ID, picture);
                    startActivity(intent);
                }
            }
        }

        @NonNull
        @Override
        public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.picture_layout, parent,false);
            ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ImageGalleryAdapter.MyViewHolder holder, int position) {
            Integer picture = pictures[position];

            ImageView imageView = holder.myImageView;
            Glide.with(context)
                    .load(R.drawable.ic_cloud_off_red)
                    .into(imageView);

            imageService.getImage(picture, imageView);
        }

        @Override
        public int getItemCount() {
            return pictures.length;
        }
    }

    private int getResourceId(String name) {
        return this.getResources().getIdentifier(name, "raw", this.getPackageName());
    }
}

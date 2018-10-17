package com.example.dmitrykurilov.picturegallery;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

public class ImageService {
    private Semaphore sem;
    private Context context;

    public ImageService(int sem, Context context) {
        this.sem = new Semaphore(sem);
        this.context = context;
    }

    public void getImage(int imageId, ImageView imageView) {
        GetImageThread getImageThread = new GetImageThread(sem, imageId, imageView, context);
        getImageThread.start();
    }

    public class GetImageThread extends Thread {
        private Semaphore sem;
        private int imageId;
        private ImageView imageView;
        private Context context;

        public GetImageThread(Semaphore sem, int imageId, ImageView imageView, Context context) {
            this.sem = sem;
            this.imageId = imageId;
            this.imageView = imageView;
            this.context = context;
        }

        public void run() {
            try {
                sem.acquire();

                System.out.println("--- Thread id: " + getId());

                final Bitmap bitmap = Glide.with(context)
                        .load(imageId)
                        .asBitmap()
                        .placeholder(R.drawable.ic_cloud_off_red)
                        .into(200,200)
                        .get();

                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });

                sem.release();
            } catch (InterruptedException|ExecutionException e) {
                e.printStackTrace();
            }

        }
    }
}

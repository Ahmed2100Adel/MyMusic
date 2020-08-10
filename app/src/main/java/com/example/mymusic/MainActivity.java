package com.example.mymusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mymusic.databinding.ActivityMainBinding;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View root=binding.getRoot();
        setContentView(root);

        settingBackground();
        if (checkPermissionForReadExtertalStorage()) {
            ContentResolver cr = this.getContentResolver();

           Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
            String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
            Cursor cur = cr.query(uri, null, selection, null, sortOrder);
            int count = 0;

            if(cur != null)
            {
                count = cur.getCount();
                Log.v("main", "12");
                Log.v("main", String.valueOf(cur.getCount()));

                if(count > 0)
                {
                    while(cur.moveToNext())
                    {
                        String data = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
                        // Add code to get more column here
                        Log.v("main",cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                        Log.v("main",cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                        //Log.v("main",cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.VOLUME_NAME)));
                        //Log.v("main",cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME)));
                        //Log.v("main",cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.OWNER_PACKAGE_NAME)));
                       // Log.v("main",cur.getString(cur.getColumnIndex(MediaStore.Audio.Media._COUNT)));
                        //Log.v("main",cur.getString(cur.getColumnIndex(MediaStore.Audio.Media._ID)));
                        Log.v("main",cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                        Log.v("main",cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                        Log.v("main",cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                        //Log.v("main",cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.BOOKMARK)));
                        //Log.v("main",cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.COMPOSER)));
                        Log.v("main",cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)));

                        // Save to your list here
                    }

                }

                cur.close();
            }
        }else{
            try {
                requestPermissionForReadExtertalStorage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }
    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){

        }
    }

    public void settingBackground(){
        Observable.interval(0,5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o->{
                    Random random= new Random();
                    int max=22;
                    int min=1;
                    int ran=random.nextInt((max-min)+1)+min;
                    String uri="@drawable/background_"+ran;
                    int imageRes=getResources().getIdentifier(uri,null,getPackageName());
                    Glide.with(this)
                            .load(imageRes)
                             .transition(DrawableTransitionOptions.withCrossFade(1000))
                            .into(new CustomTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    binding.wholeBackground.setBackground(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });
                });


    }
}
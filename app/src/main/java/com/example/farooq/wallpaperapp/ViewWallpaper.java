package com.example.farooq.wallpaperapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;



import com.example.farooq.wallpaperapp.Common.Common;
import com.example.farooq.wallpaperapp.Database.DataSource.RecentsRepository;
import com.example.farooq.wallpaperapp.Database.LocalDatabase.LocalDatabase;
import com.example.farooq.wallpaperapp.Database.LocalDatabase.RecentsDataSource;
import com.example.farooq.wallpaperapp.Database.Recents;
import com.example.farooq.wallpaperapp.Helper.SaveImageHelper;
import com.example.farooq.wallpaperapp.Model.Wallpaper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ViewWallpaper extends AppCompatActivity {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private static final String TAG = "ViewWallpaper";

    CompositeDisposable compositeDisposable;
    RecentsRepository recentsRepository;
    LocalDatabase database;

    //    //facebook sharing
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;
    private FloatingActionMenu fabMainMenu;
    private com.github.clans.fab.FloatingActionButton fabSahreFacebook;

    private CollapsingToolbarLayout mCollapse;
    private FloatingActionButton mFabWallpaper;
    private FloatingActionButton mFabDownload;
    private Toolbar mToolbar;
    private ImageView mImage;
    private CoordinatorLayout rootLayout;

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
            try {
                manager.setBitmap(bitmap);
                Snackbar.make(rootLayout,"Wallpaper is Set",Snackbar.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) { }
        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) { }
    };
    Target facebookConvertBitmap = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto sharePhoto= new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            if (ShareDialog.canShow(SharePhotoContent.class)){
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(sharePhoto)
                        .build();
                shareDialog.show(content);
            }
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) { }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) { }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wallpaper);

        compositeDisposable=  new CompositeDisposable();
        database= LocalDatabase.getInstance(this);
        recentsRepository = RecentsRepository
                .getInstance(RecentsDataSource.getInstance(database.recentsDAO()));


        mToolbar = (Toolbar) findViewById(R.id.view_toolbar);
        setSupportActionBar(mToolbar);
        if(mToolbar!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        rootLayout = (CoordinatorLayout) findViewById(R.id.view_coordinator_layout);
        mCollapse =  (CollapsingToolbarLayout) findViewById(R.id.view_collapsing_toolbar);
        mCollapse.setCollapsedTitleTextAppearance(R.style.CollapseAppBar);
        mCollapse.setExpandedTitleTextAppearance(R.style.Expanded_App_Bar);
        mCollapse.setTitle(Common.CATEGORY_SELECTED);

        mImage = (ImageView) findViewById(R.id.view_image);
        Picasso.get()
                .load(Common.SELECT_BACKGROUND.getImageurl())
                .into(mImage);
        addToRecents();
        mFabWallpaper = (FloatingActionButton)  findViewById(R.id.view_fab_wallpaper);
        mFabWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get()
                        .load(Common.SELECT_BACKGROUND.getImageurl())
                        .into(target);
            }
        });

        mFabDownload = (FloatingActionButton)  findViewById(R.id.view_fab_download);
        mFabDownload.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ViewWallpaper.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},Common.REQUSET_PERMISSION_CODE);
                }
                else {
                    dialogLoadSaveIamge();
                }
            }
        });
        //increase view count on seening wallpaper
        IncreaseViewCount();

        //facebook
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        fabMainMenu = (FloatingActionMenu) findViewById(R.id.view_fab_menu);
        fabSahreFacebook = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.view_fab_menu_item);

        fabSahreFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //create callback
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(ViewWallpaper.this, "Share Successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(ViewWallpaper.this, "Share Cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(ViewWallpaper.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                //we will fetch the image and convert into bitmap
                Picasso.get()
                        .load(Common.SELECT_BACKGROUND.getImageurl())
                        .into(facebookConvertBitmap);
            }
        });
    }

    private void IncreaseViewCount() {
        FirebaseDatabase.getInstance()
                .getReference(Common.STR_WALLPAPER)
                .child(Common.SELECT_BACKGROUND_KEY)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("viewCount")){
                            Wallpaper wallpaper= dataSnapshot.getValue(Wallpaper.class);
                            f(wallpaper.getViewCount() +1);
                        }else{
                            //if view count is not set then default value will be one
                            f(Long.valueOf(1));
                        }//else end
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }// increase view count is end

    private void f(long count) {
        Map map= new HashMap();
        if(count>1){
            map.put("viewCount",count);
        }else {
            map.put("viewCount",count);
        }
        FirebaseDatabase.getInstance()
                .getReference(Common.STR_WALLPAPER)
                .child(Common.SELECT_BACKGROUND_KEY).updateChildren(map)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewWallpaper.this, "Cannot Update View Count", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: update view count is not updated");
                    }
                });
    }
    private void addToRecents() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {

                Recents recents = new Recents(
                        Common.SELECT_BACKGROUND.getCategoryId(),
                        Common.SELECT_BACKGROUND.getImageurl(),
                        String.valueOf(System.currentTimeMillis()),
                        Common.SELECT_BACKGROUND_KEY
                );
                recentsRepository.insertRecents(recents);
                emitter.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                               @Override
                               public void accept(Object o) throws Exception { }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("Error", "accept:"+throwable.getMessage());
                            }
                        },
                        new Action() {
                            @Override
                            public void run() throws Exception { }
                        });
        compositeDisposable.add(disposable);
    }

    private void dialogLoadSaveIamge() {
        AlertDialog dialog = new SpotsDialog(ViewWallpaper.this);
        dialog.show();
        dialog.setMessage("Please waiting....");

        String file_name = UUID.randomUUID().toString() + ".png";

        Picasso.get().load(Common.SELECT_BACKGROUND.getImageurl())
                .into(new SaveImageHelper(getBaseContext(),
                        dialog,
                        getApplicationContext().getContentResolver(),
                        file_name,
                        "Farooq creation Live Wallpaper"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Common.REQUSET_PERMISSION_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    dialogLoadSaveIamge();
                else
                    Toast.makeText(this, "You need to accept this permission to download", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Picasso.get().cancelRequest(target);
        compositeDisposable.clear();
    }
}
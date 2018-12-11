package com.example.farooq.wallpaperapp;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.farooq.wallpaperapp.Common.Common;
import com.example.farooq.wallpaperapp.Interface.ItemClickListener;
import com.example.farooq.wallpaperapp.Model.Wallpaper;
import com.example.farooq.wallpaperapp.ViewHolder.ListWallpaperViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ListWallpaper extends AppCompatActivity {

    private static final String TAG = "ListWallpaper";

    private Query query;
    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Wallpaper> options;
    private FirebaseRecyclerAdapter<Wallpaper,ListWallpaperViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_wallpaper);

        Toolbar toolbar = (Toolbar) findViewById(R.id.list_wallpaper_toolbar);
        toolbar.setTitle(Common.CATEGORY_SELECTED);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.list_recycler_view);
        recyclerView.hasFixedSize();
        GridLayoutManager manager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(manager);

        loadListWallpaper();
    }

    private void loadListWallpaper() {
        query = FirebaseDatabase.getInstance().getReference(Common.STR_WALLPAPER)
            .orderByChild("categoryId").equalTo(Common.CATEGORY_ID_SELECTED);
        options=  new FirebaseRecyclerOptions.Builder<Wallpaper>()
                .setQuery(query,Wallpaper.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Wallpaper, ListWallpaperViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ListWallpaperViewHolder holder, final int position, @NonNull final Wallpaper model) {
                Picasso.get()
                        .load(model.getImageurl())
                        .into(holder.mListImage, new Callback() {
                            @Override
                            public void onSuccess() { }
                            @Override
                            public void onError(Exception e) {
                                Picasso.get()
                                        .load(model.getImageurl())
                                        .error(R.drawable.ic_terrain_black_24dp)
                                        .into(holder.mListImage, new Callback() {
                                            @Override
                                            public void onSuccess() { }
                                            @Override
                                            public void onError(Exception e) {
                                                Log.d(TAG, "onError: Image Fetch in Problem in List Wallpaper");
                                            }
                                        });
                            }
                        });
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int postion) {
                        Intent intent=  new Intent(ListWallpaper.this,ViewWallpaper.class);
                        Common.SELECT_BACKGROUND = model;
                        Common.SELECT_BACKGROUND_KEY=adapter.getRef(position).getKey();
                        startActivity(intent);
                    }
                });
            }
            @Override
            public ListWallpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_layout_wallpaper_lsit,parent,false);
                int height = parent.getMeasuredHeight()/2;
                view.setMinimumHeight(height);
                return new ListWallpaperViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}

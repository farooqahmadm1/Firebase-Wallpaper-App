package com.example.farooq.wallpaperapp.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farooq.wallpaperapp.Common.Common;
import com.example.farooq.wallpaperapp.Interface.ItemClickListener;
import com.example.farooq.wallpaperapp.ListWallpaper;
import com.example.farooq.wallpaperapp.Model.Wallpaper;
import com.example.farooq.wallpaperapp.R;
import com.example.farooq.wallpaperapp.ViewHolder.ListWallpaperViewHolder;
import com.example.farooq.wallpaperapp.ViewWallpaper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class TrendingFragment extends Fragment {

    private static final String TAG = "TrendingFragment";
    private static TrendingFragment INSTANCE = null;

    private RecyclerView recyclerView;

    private DatabaseReference reference;
    private FirebaseRecyclerOptions<Wallpaper> options;
    private FirebaseRecyclerAdapter<Wallpaper,ListWallpaperViewHolder> adapter;

    public static Fragment getInstance() {
        if (INSTANCE == null)
            return new TrendingFragment();
        return INSTANCE;
    }
    public TrendingFragment() {
        // Required empty public constructor
        reference = FirebaseDatabase.getInstance()
                .getReference(Common.STR_WALLPAPER);

        Query query = reference.orderByChild("viewCount").limitToLast(10);
        options = new FirebaseRecyclerOptions.Builder<Wallpaper>()
                .setQuery(query,Wallpaper.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Wallpaper, ListWallpaperViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ListWallpaperViewHolder holder, int position, @NonNull final Wallpaper model) {
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
                        Intent intent=  new Intent(getActivity(),ViewWallpaper.class);
                        Common.SELECT_BACKGROUND = model;
                        startActivity(intent);
                    }
                });
            }

            @Override
            public ListWallpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_layout_wallpaper_lsit,parent,false);
                return new ListWallpaperViewHolder(view);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_trending, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.trending_recyler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager= new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        loadTrending();
        return view;
    }

    private void loadTrending() {
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter!=null){
            adapter.startListening();
        }
    }
}
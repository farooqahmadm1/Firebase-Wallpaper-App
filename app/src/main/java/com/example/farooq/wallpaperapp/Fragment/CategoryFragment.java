package com.example.farooq.wallpaperapp.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farooq.wallpaperapp.Common.Common;
import com.example.farooq.wallpaperapp.Interface.ItemClickListener;
import com.example.farooq.wallpaperapp.ListWallpaper;
import com.example.farooq.wallpaperapp.Model.CategoryItem;
import com.example.farooq.wallpaperapp.R;
import com.example.farooq.wallpaperapp.ViewHolder.CategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class CategoryFragment extends Fragment {

    private static final String TAG = "CategoryFragment";
    private static CategoryFragment INSTANCE = null;
    //Firebase
    private DatabaseReference reference;

    //Firebase-UI Adapter
    FirebaseRecyclerAdapter<CategoryItem,CategoryViewHolder> adapter;
    FirebaseRecyclerOptions<CategoryItem> options;

    //View
    RecyclerView recyclerView;

    public CategoryFragment() {
        Log.d(TAG, "CategoryFragment: Constructer");
        reference= FirebaseDatabase.getInstance().getReference(Common.STR_CATEGORY_BACKGROUND);
        options = new FirebaseRecyclerOptions.Builder<CategoryItem>()
                .setQuery(reference,CategoryItem.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<CategoryItem, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position, @NonNull final CategoryItem model) {
                Picasso.get()
                        .load(model.getImageurl())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.category_image, new Callback() {
                            @Override
                            public void onSuccess() {

                            }
                            @Override
                            public void onError(Exception e) {
                                Picasso.get()
                                        .load(model.getImageurl())
                                        .error(R.drawable.ic_terrain_black_24dp)
                                        .into(holder.category_image, new Callback() {
                                            @Override
                                            public void onSuccess() { }
                                            @Override
                                            public void onError(Exception e) {
                                                Picasso.get()
                                                        .load(R.drawable.ic_terrain_black_24dp)
                                                        .into(holder.category_image);
                                            }
                                        });
                            }
                        });
                holder.category_name.setText(model.getName());
                Log.d(TAG, "onBindViewHolder: message");
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int postion) {
                        Common.CATEGORY_ID_SELECTED = adapter.getRef(position).getKey();
                        Common.CATEGORY_SELECTED = model.getName();
                        Log.d(TAG, "onClick: new activity start");
                        Intent intent = new Intent(getActivity(),ListWallpaper.class);
                        startActivity(intent);
                    }
                });
            }
            @Override
            public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_layout_category_item,parent,false);
                return new CategoryViewHolder(view);
            }
        };
    }

    public static Fragment getInstance() {
        if (INSTANCE == null)
            return new CategoryFragment();
        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.category_recyler_view);
        GridLayoutManager manager= new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        setCategory();

        return view;
    }

    private void setCategory() {
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        if (adapter==null){
            adapter.startListening();
        }
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
}
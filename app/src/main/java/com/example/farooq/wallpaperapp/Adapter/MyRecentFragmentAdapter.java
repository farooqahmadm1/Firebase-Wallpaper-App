package com.example.farooq.wallpaperapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farooq.wallpaperapp.Common.Common;
import com.example.farooq.wallpaperapp.Database.Recents;
import com.example.farooq.wallpaperapp.Interface.ItemClickListener;
import com.example.farooq.wallpaperapp.ListWallpaper;
import com.example.farooq.wallpaperapp.Model.Wallpaper;
import com.example.farooq.wallpaperapp.R;
import com.example.farooq.wallpaperapp.ViewHolder.ListWallpaperViewHolder;
import com.example.farooq.wallpaperapp.ViewWallpaper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyRecentFragmentAdapter extends RecyclerView.Adapter<ListWallpaperViewHolder> {
    private static final String TAG = "MyRecentFragmentAdapter";

    private Context context;
    private List<Recents> list;

    public MyRecentFragmentAdapter() { }

    public MyRecentFragmentAdapter(Context context, List<Recents> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ListWallpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.single_layout_wallpaper_lsit,parent,false);
        return new ListWallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder( final ListWallpaperViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: link" + list.get(position).getImageurl());
        Picasso.get()
                .load(list.get(position).getImageurl())
                .into(holder.mListImage, new Callback() {
                    @Override
                    public void onSuccess() { }
                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(list.get(position).getImageurl())
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
                Intent intent=  new Intent(context,ViewWallpaper.class);
                Wallpaper wallpaper = new Wallpaper();
                wallpaper.setCategoryId(list.get(position).getCategoryId());
                wallpaper.setImageurl(list.get(position).getImageurl());
                Common.SELECT_BACKGROUND = wallpaper;
                Common.SELECT_BACKGROUND_KEY = list.get(position).getKey();
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}

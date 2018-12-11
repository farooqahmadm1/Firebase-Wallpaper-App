package com.example.farooq.wallpaperapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.farooq.wallpaperapp.Interface.ItemClickListener;
import com.example.farooq.wallpaperapp.R;

public class ListWallpaperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView mListImage;

    ItemClickListener itemClickListener;
    public ListWallpaperViewHolder(View itemView) {
        super(itemView);
        mListImage = (ImageView) itemView.findViewById(R.id.single_list_wallpaper_image);
        mListImage.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition());
    }
}

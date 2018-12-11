package com.example.farooq.wallpaperapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.farooq.wallpaperapp.Interface.ItemClickListener;
import com.example.farooq.wallpaperapp.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView category_image;
    public TextView category_name;

    ItemClickListener itemClickListener;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        category_image = (ImageView) itemView.findViewById(R.id.single_category_image);
        category_name= (TextView) itemView.findViewById(R.id.single_category_name);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition());
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

}

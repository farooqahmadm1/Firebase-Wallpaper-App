package com.example.farooq.wallpaperapp.Model;

public class Wallpaper {
    public String categoryId;
    public String imageurl;
    public long viewCount;


    public Wallpaper() { }

    public Wallpaper(String categoryId, String imageurl) {
        this.categoryId = categoryId;
        this.imageurl = imageurl;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public long getViewCount() { return viewCount; }

    public void setViewCount(long viewCount) { this.viewCount = viewCount; }
}

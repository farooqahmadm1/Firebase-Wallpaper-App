package com.example.farooq.wallpaperapp.Model;

public class CategoryItem {
    public String name;
    public String imageurl;

    public CategoryItem() { }

    public CategoryItem(String name, String imageurl) {
        this.name = name;
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }
    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}

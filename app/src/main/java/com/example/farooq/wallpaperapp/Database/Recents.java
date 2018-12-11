package com.example.farooq.wallpaperapp.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "recents",primaryKeys = {"categoryId","imageurl"})
public class Recents {

    @ColumnInfo(name = "categoryId")
    @NonNull
    private String categoryId;

    @ColumnInfo(name = "imageurl")
    @NonNull
    private String imageurl;

    @ColumnInfo(name = "saveTime")
    private String saveTime;

    @ColumnInfo(name = "key")
    private String key;

    @Ignore
    public Recents() { }

    public Recents(@NonNull String categoryId, @NonNull String imageurl, String saveTime, String key) {
        this.categoryId = categoryId;
        this.imageurl = imageurl;
        this.saveTime = saveTime;
        this.key = key;
    }

    @NonNull
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NonNull String categoryId) {
        this.categoryId = categoryId;
    }

    @NonNull
    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(@NonNull String imageurl) {
        this.imageurl = imageurl;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }
}

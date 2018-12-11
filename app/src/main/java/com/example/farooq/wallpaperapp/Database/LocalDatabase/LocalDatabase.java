package com.example.farooq.wallpaperapp.Database.LocalDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.location.Location;

import com.example.farooq.wallpaperapp.Database.Recents;
import static com.example.farooq.wallpaperapp.Database.LocalDatabase.LocalDatabase.DATABASE_VERSION;


@Database(entities = Recents.class, version = DATABASE_VERSION,exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase{

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME ="FAROOQLiveWallpaper";

    public abstract RecentsDAO recentsDAO();

    public static LocalDatabase instance;
    public static LocalDatabase getInstance(Context context){
        if (instance == null){
            instance= Room.databaseBuilder(context,LocalDatabase.class,DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}

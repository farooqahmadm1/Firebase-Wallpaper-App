package com.example.farooq.wallpaperapp.Database.DataSource;

import com.example.farooq.wallpaperapp.Database.Recents;

import java.util.List;

import io.reactivex.Flowable;

public class RecentsRepository implements IRecentsDataSource {

    private IRecentsDataSource mLocalDatabase;
    private static RecentsRepository instance;

    public RecentsRepository(IRecentsDataSource mLocalDatabase) {
        this.mLocalDatabase = mLocalDatabase;
    }
    public static RecentsRepository getInstance(IRecentsDataSource mLocalDatabase){
        if (instance == null){
            instance = new RecentsRepository(mLocalDatabase);
        }
        return instance;
    }

    @Override
    public Flowable<List<Recents>> getAllRecents() {
        return mLocalDatabase.getAllRecents();
    }

    @Override
    public void insertRecents(Recents... recents) {
        mLocalDatabase.insertRecents(recents);
    }

    @Override
    public void updateRecents(Recents... recents) {
        mLocalDatabase.updateRecents(recents);
    }

    @Override
    public void deleteRecents(Recents... recents) {
        mLocalDatabase.deleteRecents(recents);
    }

    @Override
    public void deleteAllRecents() {
        mLocalDatabase.deleteAllRecents();
    }
}

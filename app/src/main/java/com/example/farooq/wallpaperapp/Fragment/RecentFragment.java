package com.example.farooq.wallpaperapp.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farooq.wallpaperapp.Adapter.MyRecentFragmentAdapter;
import com.example.farooq.wallpaperapp.Database.DataSource.RecentsRepository;
import com.example.farooq.wallpaperapp.Database.LocalDatabase.LocalDatabase;
import com.example.farooq.wallpaperapp.Database.LocalDatabase.RecentsDataSource;
import com.example.farooq.wallpaperapp.Database.Recents;
import com.example.farooq.wallpaperapp.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RecentFragment extends Fragment {
    private static final String TAG = "RecentFragment";
    
    Context context;
    private MyRecentFragmentAdapter adapter;
    private List<Recents> recentsList;
    private static RecentFragment INSTANCE = null;
    private RecyclerView mRecyclerView;

    private CompositeDisposable compositeDisposable;
    private LocalDatabase database;
    private RecentsRepository recentsRepository;

    public static Fragment getInstance(Context context) {
        if (INSTANCE == null)
            return new RecentFragment(context);
        return INSTANCE;
    }

    public RecentFragment() {   }

    @SuppressLint("ValidFragment")
    public RecentFragment(Context context) {
        this.context = context;

        compositeDisposable=  new CompositeDisposable();
        database= LocalDatabase.getInstance(context);
        recentsRepository = RecentsRepository
                .getInstance(RecentsDataSource.getInstance(database.recentsDAO()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);

        mRecyclerView = (RecyclerView)  view.findViewById(R.id.recents_recyler_view);
        GridLayoutManager manager = new GridLayoutManager(getActivity(),2);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);

        recentsList= new ArrayList<Recents>();
        adapter = new MyRecentFragmentAdapter(context,recentsList);
        mRecyclerView.setAdapter(adapter);

        loadRecents();
        return view;
    }

    private void loadRecents() {
        Disposable disposable = recentsRepository.getAllRecents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Recents>>() {
                               @Override
                               public void accept(List<Recents> recents) throws Exception {
                                   OnGetAllReccentsSuccess(recents);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d(TAG, "accept: Error in getting all recents ");
                            }
                        });
        compositeDisposable.add(disposable);
    }

    private void OnGetAllReccentsSuccess(List<Recents> recents) {
        recentsList.clear();
        recentsList.addAll(recents);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}

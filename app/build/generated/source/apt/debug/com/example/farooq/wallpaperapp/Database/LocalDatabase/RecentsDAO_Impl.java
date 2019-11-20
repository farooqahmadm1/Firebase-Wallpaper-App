package com.example.farooq.wallpaperapp.Database.LocalDatabase;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.RxRoom;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import com.example.farooq.wallpaperapp.Database.Recents;
import io.reactivex.Flowable;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings("unchecked")
public class RecentsDAO_Impl implements RecentsDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfRecents;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfRecents;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfRecents;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllRecents;

  public RecentsDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRecents = new EntityInsertionAdapter<Recents>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `recents`(`categoryId`,`imageurl`,`saveTime`,`key`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Recents value) {
        if (value.getCategoryId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getCategoryId());
        }
        if (value.getImageurl() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getImageurl());
        }
        if (value.getSaveTime() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getSaveTime());
        }
        if (value.getKey() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getKey());
        }
      }
    };
    this.__deletionAdapterOfRecents = new EntityDeletionOrUpdateAdapter<Recents>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `recents` WHERE `categoryId` = ? AND `imageurl` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Recents value) {
        if (value.getCategoryId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getCategoryId());
        }
        if (value.getImageurl() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getImageurl());
        }
      }
    };
    this.__updateAdapterOfRecents = new EntityDeletionOrUpdateAdapter<Recents>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `recents` SET `categoryId` = ?,`imageurl` = ?,`saveTime` = ?,`key` = ? WHERE `categoryId` = ? AND `imageurl` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Recents value) {
        if (value.getCategoryId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getCategoryId());
        }
        if (value.getImageurl() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getImageurl());
        }
        if (value.getSaveTime() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getSaveTime());
        }
        if (value.getKey() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getKey());
        }
        if (value.getCategoryId() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getCategoryId());
        }
        if (value.getImageurl() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getImageurl());
        }
      }
    };
    this.__preparedStmtOfDeleteAllRecents = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM recents";
        return _query;
      }
    };
  }

  @Override
  public void insertRecents(Recents... recents) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfRecents.insert(recents);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteRecents(Recents... recents) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfRecents.handleMultiple(recents);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateRecents(Recents... recents) {
    __db.beginTransaction();
    try {
      __updateAdapterOfRecents.handleMultiple(recents);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllRecents() {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllRecents.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAllRecents.release(_stmt);
    }
  }

  @Override
  public Flowable<List<Recents>> getAllRecents() {
    final String _sql = "SELECT * FROM recents ORDER BY saveTime DESC LIMIT 10";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return RxRoom.createFlowable(__db, new String[]{"recents"}, new Callable<List<Recents>>() {
      @Override
      public List<Recents> call() throws Exception {
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfCategoryId = _cursor.getColumnIndexOrThrow("categoryId");
          final int _cursorIndexOfImageurl = _cursor.getColumnIndexOrThrow("imageurl");
          final int _cursorIndexOfSaveTime = _cursor.getColumnIndexOrThrow("saveTime");
          final int _cursorIndexOfKey = _cursor.getColumnIndexOrThrow("key");
          final List<Recents> _result = new ArrayList<Recents>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Recents _item;
            final String _tmpCategoryId;
            _tmpCategoryId = _cursor.getString(_cursorIndexOfCategoryId);
            final String _tmpImageurl;
            _tmpImageurl = _cursor.getString(_cursorIndexOfImageurl);
            final String _tmpSaveTime;
            _tmpSaveTime = _cursor.getString(_cursorIndexOfSaveTime);
            final String _tmpKey;
            _tmpKey = _cursor.getString(_cursorIndexOfKey);
            _item = new Recents(_tmpCategoryId,_tmpImageurl,_tmpSaveTime,_tmpKey);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }
}

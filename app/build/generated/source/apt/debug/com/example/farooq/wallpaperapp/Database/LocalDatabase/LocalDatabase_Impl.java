package com.example.farooq.wallpaperapp.Database.LocalDatabase;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public class LocalDatabase_Impl extends LocalDatabase {
  private volatile RecentsDAO _recentsDAO;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `recents` (`categoryId` TEXT NOT NULL, `imageurl` TEXT NOT NULL, `saveTime` TEXT, `key` TEXT, PRIMARY KEY(`categoryId`, `imageurl`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"947bcc0e399fb86b6c6260c5e2151951\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `recents`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsRecents = new HashMap<String, TableInfo.Column>(4);
        _columnsRecents.put("categoryId", new TableInfo.Column("categoryId", "TEXT", true, 1));
        _columnsRecents.put("imageurl", new TableInfo.Column("imageurl", "TEXT", true, 2));
        _columnsRecents.put("saveTime", new TableInfo.Column("saveTime", "TEXT", false, 0));
        _columnsRecents.put("key", new TableInfo.Column("key", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRecents = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRecents = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRecents = new TableInfo("recents", _columnsRecents, _foreignKeysRecents, _indicesRecents);
        final TableInfo _existingRecents = TableInfo.read(_db, "recents");
        if (! _infoRecents.equals(_existingRecents)) {
          throw new IllegalStateException("Migration didn't properly handle recents(com.example.farooq.wallpaperapp.Database.Recents).\n"
                  + " Expected:\n" + _infoRecents + "\n"
                  + " Found:\n" + _existingRecents);
        }
      }
    }, "947bcc0e399fb86b6c6260c5e2151951", "c373724a6c2bf93b5e16a39378a3d0d8");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "recents");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `recents`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public RecentsDAO recentsDAO() {
    if (_recentsDAO != null) {
      return _recentsDAO;
    } else {
      synchronized(this) {
        if(_recentsDAO == null) {
          _recentsDAO = new RecentsDAO_Impl(this);
        }
        return _recentsDAO;
      }
    }
  }
}

package com.guyanne.dietplanner.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.guyanne.dietplanner.data.local.entities.ShoppingItemEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ShoppingItemDao_AppDatabase_Impl implements ShoppingItemDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ShoppingItemEntity> __insertionAdapterOfShoppingItemEntity;

  private final EntityDeletionOrUpdateAdapter<ShoppingItemEntity> __deletionAdapterOfShoppingItemEntity;

  private final EntityDeletionOrUpdateAdapter<ShoppingItemEntity> __updateAdapterOfShoppingItemEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllItems;

  public ShoppingItemDao_AppDatabase_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfShoppingItemEntity = new EntityInsertionAdapter<ShoppingItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `shopping_items` (`id`,`name`,`quantity`,`isChecked`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ShoppingItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getQuantity());
        final int _tmp = entity.isChecked() ? 1 : 0;
        statement.bindLong(4, _tmp);
      }
    };
    this.__deletionAdapterOfShoppingItemEntity = new EntityDeletionOrUpdateAdapter<ShoppingItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `shopping_items` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ShoppingItemEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfShoppingItemEntity = new EntityDeletionOrUpdateAdapter<ShoppingItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `shopping_items` SET `id` = ?,`name` = ?,`quantity` = ?,`isChecked` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ShoppingItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getQuantity());
        final int _tmp = entity.isChecked() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAllItems = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM shopping_items";
        return _query;
      }
    };
  }

  @Override
  public Object insertItem(final ShoppingItemEntity item,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfShoppingItemEntity.insertAndReturnId(item);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteItem(final ShoppingItemEntity item,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfShoppingItemEntity.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateItem(final ShoppingItemEntity item,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfShoppingItemEntity.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllItems(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllItems.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllItems.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ShoppingItemEntity>> getAllItems() {
    final String _sql = "SELECT * FROM shopping_items ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"shopping_items"}, new Callable<List<ShoppingItemEntity>>() {
      @Override
      @NonNull
      public List<ShoppingItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfIsChecked = CursorUtil.getColumnIndexOrThrow(_cursor, "isChecked");
          final List<ShoppingItemEntity> _result = new ArrayList<ShoppingItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ShoppingItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpQuantity;
            _tmpQuantity = _cursor.getString(_cursorIndexOfQuantity);
            final boolean _tmpIsChecked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsChecked);
            _tmpIsChecked = _tmp != 0;
            _item = new ShoppingItemEntity(_tmpId,_tmpName,_tmpQuantity,_tmpIsChecked);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

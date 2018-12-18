package com.android.a17052689.lostfound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.a17052689.lostfound.database.ItemBaseHelper;
import com.android.a17052689.lostfound.database.ItemCursorWrapper;
import com.android.a17052689.lostfound.database.LostFoundItemDBSchema.ItemTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LostFoundItemLab {
    private static LostFoundItemLab sLostFoundItemLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static LostFoundItemLab get(Context context) {
        if (sLostFoundItemLab == null) {
            sLostFoundItemLab = new LostFoundItemLab(context);
        }

        return sLostFoundItemLab;
    }

    private LostFoundItemLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void addItem(LostFoundItem i) {
        ContentValues values = getContentValues(i);
        mDatabase.insert(ItemTable.NAME, null, values);
    }

    public List<LostFoundItem> getItems() {
        List<LostFoundItem> items = new ArrayList<>();
        ItemCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                items.add(cursor.getItem());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return items;
    }

    public LostFoundItem getItem(UUID id) {
        ItemCursorWrapper cursor = queryCrimes(
                ItemTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getItem();
        } finally {
            cursor.close();
        }
    }

    public void updateItem(LostFoundItem item) {
        String uuidString = item.getmId().toString();
        ContentValues values = getContentValues(item);
        mDatabase.update(ItemTable.NAME, values,
                ItemTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void removeItem(LostFoundItem i){
        String uuidString = i.getmId().toString();

        mDatabase.delete(ItemTable.NAME,
                ItemTable.Cols.UUID + " = ? ",
                new String[]{uuidString});
    }

    private ItemCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ItemTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new ItemCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(LostFoundItem item) {
        ContentValues values = new ContentValues();
        values.put(ItemTable.Cols.UUID, item.getmId().toString());
        values.put(ItemTable.Cols.ITEM_PHOTO, item.getmItemPhoto());
        values.put(ItemTable.Cols.TITLE, item.getmTitle());
        values.put(ItemTable.Cols.DATE, item.getmDate().getTime());
        values.put(ItemTable.Cols.TIME, item.getmTime().getTime());
        values.put(ItemTable.Cols.LOCATION, item.getmLocation());
        values.put(ItemTable.Cols.FOUND, item.ismFound() ? 1 : 0);
        values.put(ItemTable.Cols.COMMENT, item.getmComment());

        return values;
    }
}

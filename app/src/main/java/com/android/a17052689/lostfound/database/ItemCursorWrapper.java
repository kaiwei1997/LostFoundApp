package com.android.a17052689.lostfound.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.android.a17052689.lostfound.LostFoundItem;
import com.android.a17052689.lostfound.database.LostFoundItemDBSchema.ItemTable.Cols;

import java.util.Date;
import java.util.UUID;

public class ItemCursorWrapper extends CursorWrapper {

    public ItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public LostFoundItem getItem() {
        String uuidString = getString(getColumnIndex(Cols.UUID));
        String title = getString(getColumnIndex(Cols.TITLE));
        byte[] photoData = getBlob(getColumnIndex(Cols.ITEM_PHOTO));
        long date = getLong(getColumnIndex(Cols.DATE));
        long time = getLong(getColumnIndex(Cols.TIME));
        String location =  getString(getColumnIndex(Cols.LOCATION));
        int isFound = getInt(getColumnIndex(Cols.FOUND));
        String comment = getString(getColumnIndex(Cols.COMMENT));

        LostFoundItem item = new LostFoundItem(UUID.fromString(uuidString));
        item.setmTitle(title);
        item.setmItemPhoto(photoData);
        item.setmDate(new Date(date));
        item.setmTime(new Date(time));
        item.setmLocation(location);
        item.setmFound(isFound != 0);
        item.setmComment(comment);

        return item;
    }
}

package com.android.a17052689.lostfound.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.a17052689.lostfound.database.LostFoundItemDBSchema.ItemTable;

public class ItemBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "itemBase.db";

    public ItemBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ItemTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ItemTable.Cols.UUID + ", " +
                ItemTable.Cols.ITEM_PHOTO + ", " +
                ItemTable.Cols.TITLE + ", " +
                ItemTable.Cols.DATE + ", " +
                ItemTable.Cols.TIME + ", " +
                ItemTable.Cols.LOCATION + ", " +
                ItemTable.Cols.FOUND + ", " +
                ItemTable.Cols.COMMENT +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

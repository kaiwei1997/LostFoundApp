package com.android.a17052689.lostfound.database;

public class LostFoundItemDBSchema {
    public static final class ItemTable{
        public static final String NAME = "items";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String ITEM_PHOTO = "item_photo";
            public static final String TITLE ="title";
            public static final String DATE = "date";
            public static final String TIME = "time";
            public static final String LOCATION = "location";
            public static final String COMMENT ="comment";
            public static final String FOUND = "found";
        }
    }
}

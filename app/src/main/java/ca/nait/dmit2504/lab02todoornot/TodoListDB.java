package ca.nait.dmit2504.lab02todoornot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

public class TodoListDB extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TodoList.db";

    public static final String TABLE_LIST_TITLE = "listTitle";
    public static final String TABLE_LIST_TITLE_COLUMN_ID = BaseColumns._ID;
    public static final String TABLE_LIST_TITLE_COLUMN_NAME = "listTitleName";

    public static final String TABLE_LIST_ITEM = "listItem";
    public static final String TABLE_LIST_ITEM_COLUMN_ID = BaseColumns._ID;
    public static final String TABLE_LIST_ITEM_COLUMN_NAME = "listItemName";
    public static final String TABLE_LIST_ITEM_COLUMN_DATE = "date";
    public static final String TABLE_LIST_ITEM_COLUMN_COMPLETE = "isComplete";
    public static final String TABLE_LIST_ITEM_COLUMN_TITLE_ID = "titleId";

    public TodoListDB(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_LIST_TITLE
        + "(" + TABLE_LIST_TITLE_COLUMN_ID + " INTEGER PRIMARY KEY, "
                + TABLE_LIST_TITLE_COLUMN_NAME + " TEXT);");
        db.execSQL("CREATE TABLE " + TABLE_LIST_ITEM
                + "(" + TABLE_LIST_ITEM_COLUMN_ID + " INTEGER PRIMARY KEY, "
                + TABLE_LIST_ITEM_COLUMN_NAME + " TEXT,"
                + TABLE_LIST_ITEM_COLUMN_DATE + " TEXT,"
                + TABLE_LIST_ITEM_COLUMN_COMPLETE + " INTEGER,"
                + TABLE_LIST_ITEM_COLUMN_TITLE_ID + " INTEGER, "
                + "FOREIGN KEY (" + TABLE_LIST_ITEM_COLUMN_TITLE_ID
                + ") REFERENCES " + TABLE_LIST_TITLE
                + "(" + TABLE_LIST_TITLE_COLUMN_ID + "));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST_TITLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST_ITEM  + ";");
        onCreate(db);
    }

    public long createListTitle(String title){
        SQLiteDatabase db = getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put(TABLE_LIST_TITLE_COLUMN_NAME, title);

         return db.insert(TABLE_LIST_TITLE, null, values);
    }

    public int updateListTitle(long id, String name){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BaseColumns._ID,id);
        values.put(TABLE_LIST_TITLE_COLUMN_NAME, name);
        return db.update(TABLE_LIST_TITLE, values, BaseColumns._ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Cursor getAllListTitle(){
        SQLiteDatabase db = getReadableDatabase();
        String queryStatement = "SELECT " + BaseColumns._ID + ", "
                + TABLE_LIST_TITLE_COLUMN_NAME
                + " FROM " + TABLE_LIST_TITLE
                + " ORDER BY " + TABLE_LIST_TITLE_COLUMN_NAME + " ASC";
        return db.rawQuery(queryStatement, null);
    }
    public ArrayList<ListTitle> getAllListTitlePOJO() {
        ArrayList<ListTitle> titles = new ArrayList<>();
        Cursor resultListCursor = getAllListTitle();
        while (resultListCursor.moveToNext()) {
            ListTitle singleTitle = new ListTitle();
            singleTitle.setId(resultListCursor.getInt(resultListCursor.getColumnIndex(TABLE_LIST_TITLE_COLUMN_ID)));
            singleTitle.setTitleName(resultListCursor.getString(resultListCursor.getColumnIndex(TABLE_LIST_TITLE_COLUMN_NAME)));

            titles.add(singleTitle);
        }
        return  titles;
    }

    public ListTitle findListTitle(long id) {
        // Get a readable database
        SQLiteDatabase db = getReadableDatabase();

        // Construct a SQL query statement
        String queryStatement = "SELECT " + BaseColumns._ID + ", "
                + TABLE_LIST_TITLE_COLUMN_NAME
                + " FROM " + TABLE_LIST_TITLE
                + " WHERE " + TABLE_LIST_ITEM_COLUMN_TITLE_ID + "= ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor singleResultCursor = db.rawQuery(queryStatement, selectionArgs);

        ListTitle foundListTitle = null;
        if (singleResultCursor.getCount() == 1) {
            singleResultCursor.moveToFirst();

            foundListTitle = new ListTitle();

            foundListTitle.setId(singleResultCursor.getInt(singleResultCursor.getColumnIndex(TABLE_LIST_TITLE_COLUMN_ID)));
            foundListTitle.setTitleName(singleResultCursor.getString(singleResultCursor.getColumnIndex(TABLE_LIST_TITLE_COLUMN_NAME)));
        }
        singleResultCursor.close();

        return foundListTitle;
    }


    public long createListItem(String name, String date, String titleId){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABLE_LIST_ITEM_COLUMN_NAME, name);
        values.put(TABLE_LIST_ITEM_COLUMN_DATE, date);
        values.put(TABLE_LIST_ITEM_COLUMN_COMPLETE, "Incomplete");
        values.put(TABLE_LIST_ITEM_COLUMN_TITLE_ID,titleId);
        // insert more from here
        return db.insert(TABLE_LIST_ITEM, null, values);
    }

    public int updateListItem(long id, String name, String date){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BaseColumns._ID,id);
        values.put(TABLE_LIST_ITEM_COLUMN_NAME, name);
        values.put(TABLE_LIST_ITEM_COLUMN_DATE, date);

        return db.update(TABLE_LIST_ITEM, values,
                BaseColumns._ID + " = ?",
                new String[]{String.valueOf(id)});
    }
    public int completeListItem(long id, String isComplete){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BaseColumns._ID,id);
        values.put(TABLE_LIST_ITEM_COLUMN_COMPLETE,isComplete);
        return db.update(TABLE_LIST_ITEM, values, BaseColumns._ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public Cursor getAllListItem(){
        SQLiteDatabase db = getReadableDatabase();
        String queryStatement = "SELECT " + BaseColumns._ID + ", "
                + TABLE_LIST_ITEM_COLUMN_NAME + ", "
                + TABLE_LIST_ITEM_COLUMN_DATE + ", "
                + TABLE_LIST_ITEM_COLUMN_COMPLETE + ", "
                + TABLE_LIST_ITEM_COLUMN_TITLE_ID
                + " FROM " + TABLE_LIST_ITEM
                + " ORDER BY " + TABLE_LIST_ITEM_COLUMN_NAME + " ASC";
        return db.rawQuery(queryStatement, null);
    }

    public Cursor getAllListItemByTitle(long titleId){
        SQLiteDatabase db = getReadableDatabase();
        String queryStatement = "SELECT " + BaseColumns._ID + ", "
                + TABLE_LIST_ITEM_COLUMN_NAME + ", "
                + TABLE_LIST_ITEM_COLUMN_DATE + ", "
                + TABLE_LIST_ITEM_COLUMN_COMPLETE + ", "
                + TABLE_LIST_ITEM_COLUMN_TITLE_ID
                + " FROM " + TABLE_LIST_ITEM
                + " WHERE " + TABLE_LIST_ITEM_COLUMN_TITLE_ID + "= ?"
                + " ORDER BY " + TABLE_LIST_ITEM_COLUMN_DATE + " DESC";
        return db.rawQuery(queryStatement, new String[]{String.valueOf(titleId)});
    }

    public ArrayList<ListItem> getAllListItemByTitlePOJO(long titleId) {
        ArrayList<ListItem> items = new ArrayList<>();
        Cursor resultListCursor = getAllListItemByTitle(titleId);
        while (resultListCursor.moveToNext()) {
            ListItem singleItem = new ListItem();
            singleItem.setId(resultListCursor.getInt(resultListCursor.getColumnIndex(TABLE_LIST_TITLE_COLUMN_ID)));
            singleItem.setListItemName(resultListCursor.getString(resultListCursor.getColumnIndex(TABLE_LIST_TITLE_COLUMN_NAME)));
            singleItem.setDate(resultListCursor.getString(resultListCursor.getColumnIndex(TABLE_LIST_ITEM_COLUMN_DATE)));
            singleItem.setIsComplete(resultListCursor.getString(resultListCursor.getColumnIndex(TABLE_LIST_ITEM_COLUMN_COMPLETE)));
            singleItem.setTitleId(resultListCursor.getString(resultListCursor.getColumnIndex(TABLE_LIST_ITEM_COLUMN_TITLE_ID)));

            items.add(singleItem);
        }
        return  items;
    }

    public ListItem findListItem(long id){
        SQLiteDatabase db = getReadableDatabase();

        String queryStatement = "SELECT " + BaseColumns._ID + ", "
                + TABLE_LIST_ITEM_COLUMN_NAME + ", "
                + TABLE_LIST_ITEM_COLUMN_DATE + ", "
                + TABLE_LIST_ITEM_COLUMN_COMPLETE + ", "
                + TABLE_LIST_ITEM_COLUMN_TITLE_ID
                + " FROM " + TABLE_LIST_ITEM
                + " WHERE " + TABLE_LIST_ITEM_COLUMN_ID + "= ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor singleResultCursor = db.rawQuery(queryStatement, selectionArgs);

        ListItem foundListItem = null;
        if (singleResultCursor.getCount() == 1) {
            singleResultCursor.moveToFirst();

            foundListItem = new ListItem();

            foundListItem.setId(singleResultCursor.getInt(singleResultCursor.getColumnIndex(TABLE_LIST_ITEM_COLUMN_ID)));
            foundListItem.setListItemName(singleResultCursor.getString(singleResultCursor.getColumnIndex(TABLE_LIST_ITEM_COLUMN_NAME)));
            foundListItem.setDate(singleResultCursor.getString(singleResultCursor.getColumnIndex(TABLE_LIST_ITEM_COLUMN_DATE)));
            foundListItem.setIsComplete(singleResultCursor.getString(singleResultCursor.getColumnIndex(TABLE_LIST_ITEM_COLUMN_COMPLETE)));
            foundListItem.setTitleId(singleResultCursor.getString(singleResultCursor.getColumnIndex(TABLE_LIST_ITEM_COLUMN_TITLE_ID)));
        }
        singleResultCursor.close();

        return foundListItem;
    }


}

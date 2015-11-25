package com.example.rabedi.todoapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rabedi on 11/24/15.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "items.db";

    public static final String COLUMN_ID = "itm_id";
    public static final String COLUMN_ITEM = "item";
    public static final String TABLE_TODO_ITEMS ="todo_items";
    private static final String DATABASE_CREATE = "create table "
            + TABLE_TODO_ITEMS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_ITEM
            + " text not null);";
    private static final int DATABASE_VERSION = 1;
    private String[] dbColumns={COLUMN_ID,COLUMN_ITEM};


    private static SQLiteHelper myInstance;

    File dbFile;
    File dbFilepath;

    public static synchronized SQLiteHelper getInstance(Context context){
        if(myInstance==null){
            myInstance=new SQLiteHelper(context.getApplicationContext());
        }
        return myInstance;
    }
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d("INFO:","Creating database");
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_ITEMS);
        onCreate(db);
    }

    public Item createItem(String item){

        Item currentItem=null;
        SQLiteDatabase database=getWritableDatabase();
        database.beginTransaction();
        try{
            ContentValues values=new ContentValues();
            values.put(COLUMN_ITEM, item);
            long insertId=database.insert(TABLE_TODO_ITEMS,null,values);
            database.setTransactionSuccessful();
            Log.d("INFO:","Item inserted with id:" + insertId);
            Cursor cursor=database.query(TABLE_TODO_ITEMS,dbColumns,COLUMN_ID + " = " + insertId,null,null,null,null);
            if(cursor.moveToFirst()){
                currentItem=cursorToItem(cursor);
                cursor.close();
            }
        }catch (Exception e){
            Log.d("Error:","Error occured while inserting item");
        }
        database.endTransaction();
        return currentItem;
    }

    public Item updateItem(Item item) {
        SQLiteDatabase database = getWritableDatabase();
        Item currentItem=null;
        database.beginTransaction();
        try{
            ContentValues values=new ContentValues();
            values.put(COLUMN_ITEM, item.getText());
            long updateId=database.update(TABLE_TODO_ITEMS,values,COLUMN_ID + " = " +item.getId(),null);
            Log.d("INFO:","Item updated");
            database.setTransactionSuccessful();
            Cursor cursor=database.query(TABLE_TODO_ITEMS, dbColumns, COLUMN_ID + " = " + updateId, null, null, null, null);
            if(cursor.moveToFirst()){
                currentItem=cursorToItem(cursor);
                cursor.close();
            }
        }catch(Exception e){
            Log.d("Error:","Error occured while updating item");
        }
        database.endTransaction();
        return currentItem;
    }
    public void deleteItem(Item item){
        SQLiteDatabase database=getWritableDatabase();
        long id=item.getId();
        database.delete(TABLE_TODO_ITEMS, SQLiteHelper.COLUMN_ID + "=" + id, null);
        Log.d("INFO:", "Item deleted with  id: " + id);

    }

    public List<Item> getAllItems(){
        SQLiteDatabase database=getReadableDatabase();
        List<Item> itemList=new ArrayList<Item>();
        Cursor cursor=database.query(TABLE_TODO_ITEMS,dbColumns,null,null,null,null,null);
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()) {
                Item item = cursorToItem(cursor);
                itemList.add(item);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return itemList;
    }

    private Item cursorToItem(Cursor cursor){

        Item item=new Item();
        item.setId(cursor.getLong(0));
        item.setText(cursor.getString(1));
        return item;
    }
}

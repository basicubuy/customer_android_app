package com.ubuyng.app.ubuyapi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.ubuyng.app.ubuyapi.Models.ItemBids;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ubuyng.db";
    public static final String TABLE_SAVED_BIDS = "savedBids";
    public static final String TABLE_SAVED_SKILLS = "savedSkills";

//    @SECTOR:: USED FOR PROJECT BIDS
    public static final String KEY_PROJECT_ID = "project_id";
    public static final String KEY_BID_ID = "bid_id";
    public static final String KEY_BIDDER_ID = "bidder_id";
    public static final String KEY_PROJECT_TITLE = "project_title";
    public static final String KEY_BID_MESSAGE = "bid_message";
    public static final String KEY_BID_AMOUNT = "bid_amount";
    public static final String KEY_BIDDER_IMAGE = "profile_image";
    public static final String KEY_BIDDER_NAME = "bidder_name";
    public static final String KEY_BID_DATE = "bid_date";
    public static final String KEY_BID_STATUS = "bid_status";


    /* @SECTOR::  USED FOR PROJECT SKILLS*/
    public static final String KEY_SKILL_ID = "skill_id";
    public static final String KEY_SKILL_NAME = "skill_name";
    public static final String KEY_SUBCATEGORY_ID = "SUB_category_id";


    /*@SECTOR STRINGS SETUP ENDS HERE*/
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_BIDS_TABLE = "CREATE TABLE " + TABLE_SAVED_BIDS + "("
                + KEY_PROJECT_ID + " INTEGER,"
                + KEY_BID_ID + " INTEGER,"
                + KEY_BIDDER_ID + " INTEGER,"
                + KEY_BID_MESSAGE + " TEXT,"
                + KEY_BID_AMOUNT + " TEXT,"
                + KEY_BIDDER_IMAGE + " TEXT,"
                + KEY_BID_STATUS + " TEXT"
                + ")";

        db.execSQL(CREATE_BIDS_TABLE);
        
        String CREATE_SKILLS_TABLE = "CREATE TABLE " + TABLE_SAVED_SKILLS + "("
                + KEY_SKILL_ID + " INTEGER,"
                + KEY_SKILL_NAME + " TEXT,"
                + KEY_SUBCATEGORY_ID + " INTEGER"
                + ")";
        db.execSQL(CREATE_SKILLS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED_SKILLS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED_BIDS);
        // Create tables again
        onCreate(db);
    }


    //    @sector:: a single bids
    public boolean getBidById(String story_id) {
        boolean count = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = new String[]{story_id};
        Cursor cursor = db.rawQuery("SELECT bid_id FROM savedBids WHERE bid_id=? ", args);
        if (cursor.moveToFirst()) {
            count = true;
        }
        cursor.close();
        db.close();
        return count;
    }

    //    @sector:: a single project list of  bids
    public boolean getBidByProjectId(String story_id) {
        boolean count = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = new String[]{story_id};
        Cursor cursor = db.rawQuery("SELECT project_id FROM savedBids WHERE project_id=? ", args);
        if (cursor.moveToFirst()) {
            count = true;
        }
        cursor.close();
        db.close();
        return count;
    }

    public void removedSavedBids(String _id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM  savedBids " + " WHERE " + KEY_BID_ID + " = " + _id);
        db.close();
    }

    public long addSavedBids(String TableName, ContentValues contentvalues, String s1) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TableName, s1, contentvalues);
    }

    public ArrayList<ItemBids> getSavedBidsById(String _id) {
        ArrayList<ItemBids> savedBidsList = new ArrayList<>();
        String selectQuery = "SELECT *  FROM "
                + TABLE_SAVED_BIDS + " WHERE " + KEY_PROJECT_ID + " = " + _id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ItemBids items = new ItemBids();
                items.setBidId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_BID_ID)));
                items.setProjectId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PROJECT_ID)));
                items.setBidderId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_BIDDER_ID)));
                items.setBidMessage(cursor.getString(cursor.getColumnIndexOrThrow(KEY_BID_MESSAGE)));
                items.setBidAmount(cursor.getString(cursor.getColumnIndexOrThrow(KEY_BID_AMOUNT)));
                items.setBidderImage(cursor.getString(cursor.getColumnIndexOrThrow(KEY_BIDDER_IMAGE)));
                items.setBidStatus(cursor.getString(cursor.getColumnIndexOrThrow(KEY_BID_STATUS)));

                savedBidsList.add(items);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return savedBidsList;
    }




}

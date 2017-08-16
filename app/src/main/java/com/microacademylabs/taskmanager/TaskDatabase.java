package com.microacademylabs.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Karen Freeman-Smith on 5/31/2017.
 */

public class TaskDatabase extends SQLiteOpenHelper {
  private static final String DATABASE_NAME = "task.db";
  private static final String TABLE_TASKS = "tasks";
  private static final String FIELD_TITLE = "title";
  private static final String FIELD_DESCRIPTION = "description";
  private static final String TABLE_LEADS = "leads";
  private static final String FIELD_ORG = "organization";
  private static final String FIELD_URL = "url";
  private static final String FIELD_NAME = "contact";
  private static final String FIELD_EMAIL = "email";
  private static final int DATABASE_VERSION = 2;

  TaskDatabase(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE " + TABLE_TASKS + "(_id integer PRIMARY KEY," + FIELD_TITLE + " TEXT, " + FIELD_DESCRIPTION + " TEXT);");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // to handle upgrading database if needed
    db.execSQL("CREATE TABLE " + TABLE_LEADS + "(_id integer PRIMARY KEY," + FIELD_NAME + " TEXT, " + FIELD_EMAIL + " TEXT, " + FIELD_ORG + " TEXT, " + FIELD_URL + " TEXT);");
  }

  public void saveRecord(String taskTitle, String taskDescription) {
    long id = findTaskId(taskTitle);
    if (id > 0) {
      updateRecord(id, taskTitle, taskDescription);
    } else {
      addRecord(taskTitle, taskDescription);
    }
  }

  public long addRecord(String taskTitle, String taskDescription) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(FIELD_TITLE, taskTitle);
    values.put(FIELD_DESCRIPTION, taskDescription);
    return db.insert(TABLE_TASKS, null, values);
  }

  public void saveContact(String orgName, String orgUrl) {
    long id = findContactId(orgName);
    if (id > 0) {
      updateContact(id, orgName, orgUrl);
    } else {
      addContact(orgName, orgUrl);
    }
  }

  public long addContact(String orgName, String orgUrl) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(FIELD_ORG, orgName);
    values.put(FIELD_URL, orgUrl);
    return db.insert(TABLE_LEADS, null, values);
  }

  public int updateRecord(long id, String taskTitle, String taskDescription) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("_id", id);
    values.put(FIELD_TITLE, taskTitle);
    values.put(FIELD_DESCRIPTION, taskDescription);
    return db.update(TABLE_TASKS, values, "_id = ?", new String[]{String.valueOf(id)});
  }

  public int updateContact(long id, String orgName, String orgUrl) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("_id", id);
    values.put(FIELD_ORG, orgName);
    values.put(FIELD_URL, orgUrl);
    return db.update(TABLE_LEADS, values, "_id = ?", new String[]{String.valueOf(id)});
  }

  public int deleteRecord(long id) {
    SQLiteDatabase db = getWritableDatabase();
    return db.delete(TABLE_TASKS, "_id = ?", new String[]{String.valueOf(id)});
  }

  public int deleteContact(long id) {
    SQLiteDatabase db = getWritableDatabase();
    return db.delete(TABLE_LEADS, "_id = ?", new String[]{String.valueOf(id)});
  }

  public long findTaskId(String taskTitle) {
    long returnVal = -1;
    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.rawQuery("SELECT _id FROM " + TABLE_TASKS + " WHERE " + FIELD_TITLE + " = ?", new String[]{taskTitle});
    Log.i("findWordId", "getCount()="+cursor.getCount());
    if(cursor.getCount()==1) {
      cursor.moveToFirst();
      returnVal = cursor.getInt(0);
    }
    return returnVal;
  }

  public long findContactId(String orgName) {
    long returnVal = -1;
    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.rawQuery("SELECT _id FROM " + TABLE_LEADS + " WHERE " + FIELD_ORG + " = ?", new String[]{orgName});
    Log.i("findWordId", "getCount()="+cursor.getCount());
    if(cursor.getCount()==1) {
      cursor.moveToFirst();
      returnVal = cursor.getInt(0);
    }
    return returnVal;
  }

  public String getDescription(long id) {
    String returnVal = "";
    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.rawQuery("SELECT description FROM " + TABLE_TASKS + " WHERE _id=?", new String[]{String.valueOf(id)});
    if(cursor.getCount()==1) {
      cursor.moveToFirst();
      returnVal = cursor.getString(0);
    }
    return returnVal;
  }

  public Cursor getTaskList() {
    SQLiteDatabase db = getReadableDatabase();
    String query = "SELECT _id, " + FIELD_TITLE + " FROM " + TABLE_TASKS + " ORDER BY " + FIELD_TITLE + " ASC";
    return db.rawQuery(query, null);
  }

  public Cursor getContactList() {
    SQLiteDatabase db = getReadableDatabase();
    String query = "SELECT _id, " + FIELD_ORG + " FROM " + TABLE_LEADS + " ORDER BY " + FIELD_ORG + " ASC";
    return db.rawQuery(query, null);
  }
}



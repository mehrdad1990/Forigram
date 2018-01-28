package org.telegram.ui.tools.AddUserToChat.UserChanges;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class SC_DBHelper extends SQLiteOpenHelper {
    private static final String _id = "_id";
    private static final String database_NAME = "SuperSContactsDB";
    private static final int database_VERSION = 3;
    private static final String table_operation = "SContactsOperationTB";
    private static final String table_scontacts = "SContactsTB";
    private String date;
    private String operation;
    private String userId;

    public SC_DBHelper(Context context) {
        super(context, database_NAME, null, database_VERSION);
        this.userId = "userId";
        this.operation = "operation";
        this.date = "date";
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE SContactsTB ( _id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER)");
        db.execSQL("CREATE TABLE SContactsOperationTB ( _id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER, operation TEXT, date integer default (strftime('%s','now') * 1000))");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS SContactsTB");
        db.execSQL("DROP TABLE IF EXISTS SContactsOperationTB");
        onCreate(db);
    }

    public void addSContact(int UID) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.userId, Integer.valueOf(UID));
        writableDatabase.insertWithOnConflict(table_scontacts, null, contentValues, 5);
        writableDatabase.close();
    }

    public ArrayList<Integer> getAllSContacts() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor cursor = writableDatabase.rawQuery("SELECT * FROM SContactsTB", null);
        ArrayList<Integer> arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(Integer.valueOf(cursor.getInt(1)));
            } while (cursor.moveToNext());
        }
        writableDatabase.close();
        return arrayList;
    }

    public void deleteAllSContacts() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete(table_scontacts, null, null);
        writableDatabase.close();
    }

    public void deleteSContact(int uid) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete(table_scontacts, this.userId + "=" + uid, null);
        writableDatabase.close();
    }

    public void addOperation(int UID, String op, int d) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.userId, Integer.valueOf(UID));
        contentValues.put(this.operation, op);
        contentValues.put(this.date, Integer.valueOf(d));
        writableDatabase.insert(table_operation, null, contentValues);
        writableDatabase.close();
    }

    public ArrayList<OperationModel> getAllOperation() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor cursor = writableDatabase.rawQuery("SELECT * FROM SContactsOperationTB", null);
        ArrayList<OperationModel> arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(new OperationModel(cursor.getInt(1), cursor.getString(2), cursor.getInt(database_VERSION)));
            } while (cursor.moveToNext());
        }
        writableDatabase.close();
        return arrayList;
    }

    public void deleteAllSOperation() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete(table_operation, null, null);
        writableDatabase.close();
    }
}

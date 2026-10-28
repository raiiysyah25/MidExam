package com.example.midexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudentDB.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_STUDENTS = "students";
    public static final String COL_ID = "student_id";
    public static final String COL_NAME = "name";
    public static final String COL_AGE = "age";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_STUDENTS + " (" +
                COL_ID + " TEXT PRIMARY KEY, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_AGE + " INTEGER NOT NULL CHECK(age >= 17 AND age <= 50))";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }

    public boolean addStudent(String id, String name, int age) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_ID, id);
        values.put(COL_NAME, name);
        values.put(COL_AGE, age);

        return db.insert(TABLE_STUDENTS, null, values) != -1;
    }

    public Cursor viewStudents() {
        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                TABLE_STUDENTS, null, null, null,
                null, null, COL_NAME + " ASC"
        );
    }

    public boolean editStudent(String id, String name, int age) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_AGE, age);

        return db.update(
                TABLE_STUDENTS,
                values,
                COL_ID + "=?",
                new String[]{id}
        ) > 0;
    }

    public boolean deleteStudent(String id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(
                TABLE_STUDENTS,
                COL_ID + "=?",
                new String[]{id}
        ) > 0;
    }
}
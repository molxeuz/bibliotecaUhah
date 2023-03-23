package com.myapp.biblioteca_uhah;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbLibrary extends SQLiteOpenHelper {

    // Creacion de las tabla en el archivo de base de datos
    String TblUser = "CREATE TABLE User (identification_user text primary key, name_user text, password_user text, status_user integer)";

    String TblBook = "CREATE TABLE Book (identification_book text primary key, name_book text, book_price text, availability_book integer)";

    String TblRent = "CREATE TABLE Rent (identification_rent integer primary key autoincrement, identification_user integer, identification_book integer, rent_date text)";

    public DbLibrary(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){

        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TblUser);
        db.execSQL(TblBook);
        db.execSQL(TblRent);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE User");
        db.execSQL(TblUser);
        db.execSQL("DROP TABLE Book");
        db.execSQL(TblBook);
        db.execSQL("DROP TABLE Rent");
        db.execSQL(TblRent);

    }

}

package com.example.freecodecampsqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyHelper(context: Context): SQLiteOpenHelper(context , "ACDB" , null , 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE ACTABLE(_id integer primary key autoincrement, NAME TEXT, MEANING TEXT)")
        db?.execSQL("INSERT INTO ACTABLE(NAME,MEANING)VALUES('WWW', 'World Wide Web')")
        db?.execSQL("INSERT INTO ACTABLE(NAME,MEANING)VALUES('GDG', 'Google Developer Group')")
        db?.execSQL("INSERT INTO  ACTABLE(NAME,MEANING)VALUES('AVD', 'Android Virtual Device')")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}
package android.example.fakeify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Fakeify.db";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase myDB) {
        myDB.execSQL("create table users(name TEXT, surname TEXT, " +
                "mail TEXT  primary key, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase myDB, int i, int i1) {
        myDB.execSQL("drop table if exists users");
    }

    public Boolean insertData(String name, String surname, String mail, String password){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("surname", surname);
        contentValues.put("mail", mail);
        contentValues.put("password", password);
        long result = myDB.insert("users",null, contentValues);
        if(result == -1) return false;
        else
            return true;
    }

    public Boolean checkMail(String mail){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("select * from users where mail = ?", new String[] {mail});
        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean validationCheck(String mail, String password){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("select * from users where mail = ? and password = ?", new String[] {mail, password});
        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }

}

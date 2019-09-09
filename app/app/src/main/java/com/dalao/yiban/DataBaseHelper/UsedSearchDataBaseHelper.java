package com.dalao.yiban.DataBaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class UsedSearchDataBaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_USEDSEARCH = "create table UsedSearch("
        +"searchid integer,"
        +"userid integer,"
        +"content text)";

    private Context mContext;

    public UsedSearchDataBaseHelper(Context context,String name,
                                    SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USEDSEARCH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

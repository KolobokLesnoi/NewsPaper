package koloboklesnoi.newspaper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "ARTICLES";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_SECTION = "SECTION";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_ABSTRACT = "ABSTRACT";
    public static final String COLUMN_PHOTO = "PHOTO";
    public static final String COLUMN_PUBLISH_DATE = "PUBLISH_DATE";
    public static final String COLUMN_SOURCE = "SOURCE";

    private static final int DATABASE_VERSION = 1;


    public DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "
                + "ARTICLES "
                + "(" + COLUMN_ID + " INTEGER, "
                + COLUMN_SECTION + " TEXT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_ABSTRACT + " TEXT, "
                + COLUMN_PHOTO + " TEXT, "
                + COLUMN_PUBLISH_DATE + " TEXT, "
                + COLUMN_SOURCE + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }



}

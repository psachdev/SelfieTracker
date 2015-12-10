package com.example.prasachd.dailyselfi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
	
	final static String TABLE_NAME = "SELFIE_DETAILS";
	final static String FILE_NAME = "filename";
	final static String FILE_PATH = "filepath";
	final static String COMMENT = "comment";
	final static String CREATION_DATE = "creation_date";
	final static String _ID = "_id";
	final static String[] columns = { _ID, FILE_NAME, FILE_PATH, COMMENT };

	final private static String CREATE_CMD =

	"CREATE TABLE SELFIE_DETAILS (" + _ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ FILE_NAME + " TEXT NOT NULL, "
			+ FILE_PATH + " TEXT NOT NULL, "
			+ COMMENT + " TEXT, "
			+ CREATION_DATE + " datetime default current_timestamp )";

	final private static String NAME = "SELFIE_DETAILS_DB";
	final private static Integer VERSION = 1;
	final private Context mContext;

	public DatabaseOpenHelper(Context context) {
		super(context, NAME, null, VERSION);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATE_CMD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// N/A
	}

	void deleteDatabase() {
		mContext.deleteDatabase(NAME);
	}
}

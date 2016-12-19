package com.wxdroid.microcodor.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.wxdroid.microcodor.db.config.DBConfig;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelperTemp extends OrmLiteSqliteOpenHelper {

	public DatabaseHelperTemp(Context context) {
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	private static DatabaseHelperTemp databaseHelper = null;
	
	// name of the database file for your application -- change to something appropriate for your app
	private static final String DATABASE_NAME = DBConfig.getDatabaseTempName();
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = DBConfig.getDatabaseVersion();

	// the DAO object we use to access the SimpleData table
	
	private static final AtomicInteger usageCounter = new AtomicInteger(0);
	/**
	 * This is called when the database is first created. Usually you should call createTable statements here to create
	 * the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		Log.i(DatabaseHelperTemp.class.getName(), "onCreate");
	}
	
	public static DatabaseHelperTemp getDBHelper(Context mContext) {
		if (databaseHelper == null) {
//			databaseHelper = OpenHelperManager.getHelper(mContext, DatabaseHelperTemp.class);
			databaseHelper = new DatabaseHelperTemp(mContext);
		}
		
//		usageCounter.incrementAndGet();
		
		return databaseHelper;
	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
	 * the various data to match the new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		Log.i(DatabaseHelperTemp.class.getName(), "onUpgrade");
		// after we drop the old databases, we create the new ones
//		try {
//			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
//
//			// after we drop the old databases, we create the new ones
//			onCreate(db, connectionSource);
//		} catch (SQLException e) {
//			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
//			throw new RuntimeException(e);
//		}
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
	}
	
	public synchronized void updateColumn(SQLiteDatabase db, String tableName,
            String columnName, String columnType, Object defaultField) throws SQLException{
		if (db != null) {
            Cursor c = db.rawQuery("SELECT * from " + tableName
                            + " limit 1 ", null);
            boolean flag = false;

            if (c != null) {
                    for (int i = 0; i < c.getColumnCount(); i++) {
                            if (columnName.equalsIgnoreCase(c.getColumnName(i))) {
                                    flag = true;
                                    break;
                            }
                    }
                    
                    if (flag == false) {
                    	if(defaultField != null){
                    		String sql = "alter table " + tableName + " add "
                                    + columnName + " " + columnType + " default "
                                    + defaultField;
                    		db.execSQL(sql);
                    	}else{
                    		String sql = "alter table " + tableName + " add "
                                    + columnName + " " + columnType;
                    		db.execSQL(sql);
                    	}
                    }
                    c.close();
            }
		}
	}
	
	public synchronized void drapTable(SQLiteDatabase db, String tableName) throws SQLException{
		if (db != null) {
			db.execSQL("DROP TABLE IF EXISTS "+tableName);
            onCreate(db);
		}
	}
}

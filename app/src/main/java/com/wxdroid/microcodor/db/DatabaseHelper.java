package com.wxdroid.microcodor.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.wxdroid.microcodor.db.config.DBConfig;
import com.wxdroid.microcodor.model.WpTermModel;

import java.sql.SQLException;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    public DatabaseHelper(Context context, String dbName) {

        super(context, dbName, null, DBConfig.getDatabaseVersion());
    }

    private static DatabaseHelper databaseHelper = null;

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        Log.i(DatabaseHelper.class.getName(), "onCreate");
        try {
            TableUtils.createTableIfNotExists(connectionSource, WpTermModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
//            TableUtils.createTableIfNotExists(connectionSource, Conversation.class);
//            TableUtils.createTableIfNotExists(connectionSource, Contact.class);
//            TableUtils.createTableIfNotExists(connectionSource, AddFriendsMsgBean.class);
//            TableUtils.createTableIfNotExists(connectionSource, PhoneContactBean.class);
//            TableUtils.createTableIfNotExists(connectionSource, UserModel.class);
//            TableUtils.createTableIfNotExists(connectionSource, PushMessageData.class);


    }

    public static DatabaseHelper getDBHelper(Context context, String append) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context, append);
        }

        return databaseHelper;
    }

    public AndroidConnectionSource getConnectionSource(Context context) {
        return connectionSource;
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, WpTermModel.class, true);

            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (Exception e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our SearchKey class. It will create it or just give the cached
     * value.
     */


    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();

    }

    public synchronized void updateColumn(SQLiteDatabase db, String tableName,
                                          String columnName, String columnType, Object defaultField) throws SQLException {
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
                    if (defaultField != null) {
                        String sql = "alter table " + tableName + " add "
                                + columnName + " " + columnType + " default "
                                + defaultField;
                        db.execSQL(sql);
                    } else {
                        String sql = "alter table " + tableName + " add "
                                + columnName + " " + columnType;
                        db.execSQL(sql);
                    }
                }
                c.close();
            }
        }
    }

    public synchronized void drapTable(SQLiteDatabase db, String tableName) throws SQLException {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS " + tableName);
            onCreate(db);
        }
    }
}

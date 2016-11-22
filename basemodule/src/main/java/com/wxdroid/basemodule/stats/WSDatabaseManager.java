package com.wxdroid.basemodule.stats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WSDatabaseManager extends SQLiteOpenHelper
{
  private static final String DATABASE = "hfstats_data_analysis.db";
  private static final int VERSION = 1;
  private static final String INIT_SQL = WSStatEvent.CREATE_SQL;
  private static final String UPGRADE_SQL = null;
  private static WSDatabaseManager self = null;
  
  private WSDatabaseManager(Context context)
  {
    super(context, DATABASE, null, VERSION);
  }
  
  public static synchronized WSDatabaseManager getInstance(Context context)
  {
    if ((self == null) && (context != null)) {
      self = new WSDatabaseManager(context);
    }
    return self;
  }
  
  public void onCreate(SQLiteDatabase database)
  {
    String[] initSqls = INIT_SQL.split(";");
    for (String initSql : initSqls) {
      database.execSQL(initSql + ";");
    }
  }
  
  public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
  {
    String[] upgradeSqls = UPGRADE_SQL.split(";");
    for (String upgradeSql : upgradeSqls) {
      database.execSQL(upgradeSql + ";");
    }
    onCreate(database);
  }
}
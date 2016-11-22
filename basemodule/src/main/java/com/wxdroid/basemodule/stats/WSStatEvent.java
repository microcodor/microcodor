package com.wxdroid.basemodule.stats;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class WSStatEvent
{
  static final String TABLE = "event";
  
  static final String FIELD_ID = "id";
  static final String FIELD_UID = "uid";
  static final String FIELD_DATE = "date";
  static final String FIELD_EVENT = "event";
  static final String FIELD_VALUE = "value";
  static final String FIELD_COUNT = "count";
  static final String FIELD_TIME = "time";
  static final String FIELD_SEND = "send";

  static final String EVENT_POST_FAILED = "failed";
  static final String CREATE_SQL = "CREATE TABLE IF NOT EXISTS event (id INTEGER PRIMARY KEY, uid TEXT, date INTEGER NOT NULL, event TEXT NOT NULL, value TEXT, count INTEGER DEFAULT 1, time NUMERIC NOT NULL, send INTEGER DEFAULT 0);";
  
  private int id;
  private String uid;
  private int date;
  private String event;
  private String value;
  private int count = 0;
  private long time;
  private boolean send = false;
  
  static WSStatEvent parse(Cursor cur)
  {
    WSStatEvent event = new WSStatEvent();
    if (cur.getColumnIndex(FIELD_ID) > -1) {
      event.setId(cur.getInt(cur.getColumnIndex(FIELD_ID)));
    }
    if (cur.getColumnIndex(FIELD_UID) > -1) {
        event.setUid(cur.getString(cur.getColumnIndex(FIELD_UID)));
    }
    if (cur.getColumnIndex(FIELD_DATE) > -1) {
      event.setDate(cur.getInt(cur.getColumnIndex(FIELD_DATE)));
    }
    if (cur.getColumnIndex(FIELD_EVENT) > -1) {
      event.setEvent(cur.getString(cur.getColumnIndex(FIELD_EVENT)));
    }
    if (cur.getColumnIndex(FIELD_VALUE) > -1) {
      event.setValue(cur.getString(cur.getColumnIndex(FIELD_VALUE)));
    }
    if (cur.getColumnIndex(FIELD_COUNT) > -1) {
      event.setCount(cur.getInt(cur.getColumnIndex(FIELD_COUNT)));
    }
    if (cur.getColumnIndex(FIELD_TIME) > -1) {
      event.setTime(cur.getLong(cur.getColumnIndex(FIELD_TIME)));
    }
    if (cur.getColumnIndex(FIELD_SEND) > -1) {
      event.setSend(cur.getInt(cur.getColumnIndex(FIELD_SEND)) == 1);
    }
    return event;
  }
  
  private void persist(WSDatabaseManager dbManager)
  {
    SQLiteDatabase database = dbManager.getWritableDatabase();
    ContentValues cv = new ContentValues();
    cv.put(FIELD_UID, getUid());
    cv.put(FIELD_DATE, Integer.valueOf(getDate()));
    cv.put(FIELD_EVENT, getEvent());
    if (getValue() != null) {
      cv.put(FIELD_VALUE, getValue());
    }
    cv.put(FIELD_COUNT, Integer.valueOf(getCount()));
    cv.put(FIELD_TIME, Long.valueOf(getTime()));
    cv.put(FIELD_SEND, Integer.valueOf(isSend() ? 1 : 0));
    database.insert(TABLE, null, cv);
  }
  
  static List<WSStatEvent> getEvents(WSDatabaseManager dbManager)
  {
	  return getEvents(dbManager, null, null, 0, false);
  }
  
  @SuppressWarnings("rawtypes")
static List<WSStatEvent> getEvents(WSDatabaseManager dbManager, String event, String value, int date, boolean send)
  {
    String where = " where send=?";
    @SuppressWarnings("unchecked")
    List<String> paramList = new ArrayList();
    if (send) {
      paramList.add("1");
    } else {
      paramList.add("0");
    }
    if ((event != null) && (event.trim().length() > 0))
    {
      where = where + " and event=?";
      paramList.add(event);
    }
    if ((value != null) && (value.trim().length() > 0))
    {
      if (where.length() > 0) {
        where = where + " and value=?";
      } else {
        where = "value=?";
      }
      paramList.add(value);
    }
    if (date > 0)
    {
      if (where.length() > 0) {
        where = where + " and date=?";
      } else {
        where = "date=?";
      }
      paramList.add(Integer.toString(date));
    }
    String[] params = null;
    params = new String[paramList.size()];
    for (int i = 0; i < paramList.size(); i++) {
      params[i] = ((String)paramList.get(i));
    }
    SQLiteDatabase db = dbManager.getReadableDatabase();
    Cursor cur = null;
    try
    {
      cur = db.rawQuery("select * from event" + where + " order by " + FIELD_TIME, params);
      if (cur.getCount() > 0)
      {
        @SuppressWarnings("unchecked")
        List<WSStatEvent> events = new ArrayList(cur.getCount());
        while (cur.moveToNext()) {
          events.add(parse(cur));
        }
        return events;
      }
    }
    finally
    {
      if (cur != null) {
        cur.close();
      }
    }
    return null;
  }
   
  static void addEvent(WSDatabaseManager dbManager, String event, String value, int count, long time, String uid)
  {
    WSStatEvent ev = new WSStatEvent();
    ev.setUid(uid);
    ev.setEvent(event);
    ev.setValue(value);
    ev.setCount(count);
    
    if (time > 0L) {
      ev.setTime(time);
      
      Date date = new Date(time);
	  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	  
      ev.setDate(Integer.parseInt(dateFormat.format(date)));
    }
    
    try
    {
      ev.persist(dbManager);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  static int clearHistoryEvents(WSDatabaseManager dbManager, int date)
  {
    SQLiteDatabase database = dbManager.getWritableDatabase();
    return database.delete(TABLE, "send=? and date<?", new String[] { "1", Integer.toString(date) });
  }
  
  static void clearHistoryEvents(WSDatabaseManager dbManager)
  {
	  Date today = new Date(System.currentTimeMillis());
	  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	  clearHistoryEvents(dbManager, Integer.parseInt(dateFormat.format(today)));
  }
  
  static void addEvent(WSDatabaseManager dbManager, String event, String uid)
  {
    addEvent(dbManager, event, null, 1, System.currentTimeMillis(), uid);
  }
  
  public static void addEvent(WSDatabaseManager dbManager, String event, String value, String uid)
  {
	  addEvent(dbManager, event, value, 1, System.currentTimeMillis(), uid);
  }
  
  public static void addEvent(WSDatabaseManager dbManager, String event, String value, int count, String uid)
  {
	  addEvent(dbManager, event, value, count, System.currentTimeMillis(), uid);
  }
  
  @SuppressWarnings("rawtypes")
  static int signSentEvent(WSDatabaseManager dbManager, List<Integer> idList)
  {
    SQLiteDatabase db = dbManager.getWritableDatabase();
    int affects = 0;
    for (Iterator i = idList.iterator(); i.hasNext();)
    {
      int id = ((Integer)i.next()).intValue();
      ContentValues cv = new ContentValues();
      cv.put(FIELD_SEND, "1");
      affects += db.update(TABLE, cv, "id=?", new String[] { Integer.toString(id) });
    }
    
    return affects;
  }
  
  public String toString()
  {
    return "{id=" + this.id + ", uid=" + this.uid + ", event=" + this.event + ", date=" + this.date + ", value=" + this.value + ", count=" + this.count + ", time=" + this.time + ", send=" + this.send + "}";
  }
  
  public int getId()
  {
    return this.id;
  }
  
  public void setId(int id)
  {
    this.id = id;
  }
  
  public String getUid()
  {
    return this.uid;
  }
  
  public void setUid(String uid)
  {
    this.uid = uid;
  }
  
  public int getDate()
  {
    return this.date;
  }
  
  public void setDate(int date)
  {
    this.date = date;
  }
    
  public String getEvent()
  {
    return this.event;
  }
  
  public void setEvent(String event)
  {
    this.event = event;
  }
  
  public String getValue()
  {
    return this.value;
  }
  
  public void setValue(String value)
  {
    this.value = value;
  }
  
  public int getCount()
  {
    return this.count;
  }
  
  public void setCount(int count)
  {
    if (count > 0) {
      this.count = count;
    }
  }
      
  public long getTime()
  {
    return this.time;
  }
   
  public void setTime(long time)
  {
    this.time = time;
  }
  
  public boolean isSend()
  {
    return this.send;
  }
  
  public void setSend(boolean send)
  {
    this.send = send;
  }
}
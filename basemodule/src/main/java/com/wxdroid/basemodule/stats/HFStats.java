package com.wxdroid.basemodule.stats;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HFStats {
	public static final boolean DEBUG = false;
	private static boolean developDebug = false;
	static final String SDK_VERSION = "1.0.0";
	public static final String TAG = "WS:";
	private static boolean init = false;
	//private static int maxLeaveTime = 300000;
	private static int commitIntervalTime = 300000;
	private static int commitIntervalTimeWifi = 60000;
	private static String appKey = null;
	private static WSDatabaseManager dbManager = null;
	private static boolean newStart = true;
	private static boolean statusSending = false;

	private static final String POST_FIELD_DATA = "log";
	private static final String POST_FIELD_TIME = "time";
	
	public static synchronized void init(Context activity) {
		if (!init) {
			if (getAppKey(activity) != null) {
				if (dbManager == null) {
					dbManager = WSDatabaseManager.getInstance(activity);
				}

				WSStatInfo.init(activity);
				WSStatInfo.updateAppAndDeviceInfo(dbManager, activity);
				init = true;
			}
		} else {
			newStart = false;
		}
		
		Log.i(TAG, TAG+"newStart: " + newStart);
	}

	public static String getAppKey(Context context) {
		if (appKey == null) {
			appKey = WSUtil.getAppKey(context);
			Log.i(TAG, TAG+"appkey: " + appKey);
		}

		return appKey;
	}

	public static void debug() {
		developDebug = true;
	}

	public static boolean isDebug() {
		return developDebug;
	}
	
	public static void sendDataDone() {
		Log.i(TAG, TAG+"send data done");
		statusSending = false;
	}
	
	public static void setUid(Context context, String id) {
		Log.i(TAG, TAG+"setUid: " + context +"," + id);
		init(context);
		
		WSStatInfo.putString(WSStatInfo.KEY_UID, id);
	}

	public static void onResume(Context context, String event) {
		Log.i(TAG, TAG+"onResume: " + context +"," + event);
		init(context);
		
		if(event != null) {
			onEvent(context, event);
		} else {
			send(context);
		}
    }

	public static void onResume(Context context) {
		onResume(context, null);
	}

	public static void commit(Context context) {
		Log.i(TAG, TAG+"commit: " + context);
		init(context);
		send(context, true);
	}
	
	public static void onExit(Context context, String event) {
		Log.i(TAG, TAG+"onExit: " + context);
		init(context);
		
		if(event != null) {
			onEvent(context, event, true);
		} else {
			send(context, true);
		}
	}
	
	public static void onExit(Context context) {
		onExit(context, null);
	}

	public static void onPause(Context context, String event) {
		Log.i(TAG, TAG+"onPause: " + context);
		
		WSStatInfo.putLong(WSStatInfo.KEY_USER_PAUSE_TIME,
				System.currentTimeMillis());
		
		if(event != null) {
			onEvent(context, event);
		}
	}

	public static void onPause(Context context) {
		onPause(context, null);
	}
	
	public static void onEvent(Context context, String eventKey, String eventValue, int count, boolean unconditional) {
		Log.i(TAG, TAG+"onEvent: " + context + ":" + eventKey + ":"+eventValue+":"+count);
		
		init(context);

		WSStatEvent.addEvent(dbManager, eventKey, eventValue, count, WSStatInfo.getString(WSStatInfo.KEY_UID, "0"));

		send(context, unconditional);
	}

	public static void onEvent(Context context, String eventKey, String eventValue, int count) {
		onEvent(context, eventKey, eventValue, count, false);
	}
	public static void onEvent(Context context, String eventKey, String eventValue) {
		onEvent(context, eventKey, eventValue, 1);
	}

	public static void onEvent(Context context, String eventKey, int count) {
		onEvent(context, eventKey, null, count);
	}
	
	public static void onEvent(Context context, String eventKey, boolean unconditional) {
		onEvent(context, eventKey, null, 1, unconditional);
	}

	public static void onEvent(Context context, String eventKey) {
		onEvent(context, eventKey, null, 1);
	}

	private static void send(final Context context) {
		send(context, false);
	}
	
	private static synchronized void send(final Context context, boolean unconditional) {
		if(statusSending) {
			Log.i(TAG, TAG+"other thread is sending data");
			return ;
		}
		
		statusSending = true;
		
		boolean doCommit = false;
		long curTime = System.currentTimeMillis();
		int curDate = WSUtil.getDate(curTime);
		
		long lastCommitTime = WSStatInfo.getLong(
				WSStatInfo.KEY_LAST_COMMIT_TIME, -1L);
		int lastCommitDate = WSUtil.getDate(lastCommitTime);
		
		Log.i(TAG, TAG+"lastCommitTime:" + WSUtil.getDateTime(lastCommitTime));
		//long activityPauseTime = WSStatInfo.getLong(WSStatInfo.KEY_USER_PAUSE_TIME, -1L);
		boolean _checkDataNum = false;
		if ((newStart) || unconditional || (lastCommitDate != curDate) || (curTime - lastCommitTime > commitIntervalTime)) {	// || (curTime - activityPauseTime > maxLeaveTime)
			doCommit = true;
		} else if ((curTime - lastCommitTime > commitIntervalTimeWifi)){
			if(WSUtil.getNetWorkType(context).equals("wifi")) {//wifi每分钟打一次，需要满10条
				doCommit = true;
				_checkDataNum = true;
			}
			
			if (WSUtil.getHourAndMin(curTime) > 2350) {//23:50到00:00每分钟打一次
				doCommit = true;
				_checkDataNum = false;
			}
		}
		
		if(!doCommit) {
			statusSending = false;
			return ;
		}
		
		final boolean checkDataNum = _checkDataNum;
		/*switch (sendWay) {
		case SEND_WAY_DAY:
			break;
		case SEND_WAY_WIFI:
			break;
		case SEND_WAY_REAL_TIME:
			break;
		default:*/
			new Thread(new Runnable() {
				public void run() {

					long time = System.currentTimeMillis();
					SendData data = HFStats.generateSendData(
							context, time);
					HFStats.sendEventsToServer(data, time, checkDataNum);
					
					HFStats.sendDataDone();
				}
			}).start();
		//}
	}

	private static void sendEventsToServer(SendData data, long time, boolean checkDataNum) {
		try {
			JSONArray json = data.getJson();
			//Log.i(TAG, TAG+"checkDataNum:"+checkDataNum);
			if (json == null || json.length() == 0) {
				Log.i(TAG, TAG+"no data to send");
				return;
			}
			
			if (checkDataNum && json.length() < 10) {
				Log.i(TAG, TAG+"data length less than 10");
				return;
			}

			JSONObject params = new JSONObject();
			params.put(POST_FIELD_DATA, json);
			params.put(POST_FIELD_TIME, WSUtil.getDateTime(time));

			WSUtil.post("upload?key=" + appKey, params.toString(), true);

			WSStatInfo.putLong(WSStatInfo.KEY_LAST_COMMIT_TIME,	time);
			
			List<Integer> idlist = data.getIdList();
			int upnum = WSStatEvent.signSentEvent(dbManager, idlist);
			Log.i(TAG, TAG+"signSentEvent: " + idlist.toString() + ", update " + upnum);
			
			int delnum = WSStatEvent.clearHistoryEvents(dbManager, WSUtil.getDate(time));
			Log.i(TAG, TAG+"clearHistoryEvents:"+delnum);
		} catch (Exception e) {
			Log.i(TAG, TAG+"send data failed:"+e.getMessage());
			
			WSStatEvent.addEvent(dbManager, WSStatEvent.EVENT_POST_FAILED, WSStatInfo.getString(WSStatInfo.KEY_UID, "0"));
		}
	} 
	
	@SuppressWarnings("unchecked")
	private static SendData generateSendData(Context context, long time) {
		@SuppressWarnings("rawtypes")
		List<Integer> eventIds = new ArrayList();

		JSONArray data = new JSONArray();

		try {
			String os_ver = WSStatInfo.getString(WSStatInfo.KEY_OS_VERSION, null);
			String app_ver = WSStatInfo.getString(WSStatInfo.KEY_APP_VERSION, null);
			String channel = WSStatInfo.getString(WSStatInfo.KEY_CHANNEL, null);
			String carrier = WSStatInfo.getString(WSStatInfo.KEY_CARRIER, null);
			String net = WSUtil.getNetWorkType(context);
			String resolution = WSStatInfo.getString(WSStatInfo.KEY_RESOLUTION, null);
			String model = WSStatInfo.getString(WSStatInfo.KEY_MODEL, null);
			String did = WSStatInfo.getString(WSStatInfo.KEY_DEVICE_ID, null);

			List<WSStatEvent> logs = WSStatEvent.getEvents(dbManager);
			if ((logs != null) && (logs.size() > 0)) {
				for (WSStatEvent row : logs) {
					JSONObject json = new JSONObject();
					json.put(WSStatInfo.KEY_OS, "Android");
					json.put(WSStatInfo.KEY_OS_VERSION, os_ver);
					json.put(WSStatInfo.KEY_APP_VERSION, app_ver);
					json.put(WSStatInfo.KEY_CHANNEL, channel);
					json.put(WSStatInfo.KEY_CARRIER, carrier);
					json.put(WSStatInfo.KEY_NET, net);
					json.put(WSStatInfo.KEY_RESOLUTION, resolution);
					json.put(WSStatInfo.KEY_MODEL, model);
					json.put(WSStatInfo.KEY_DEVICE_ID, did);

					int id = row.getId();
					json.put(WSStatInfo.KEY_ID, id);
					json.put(WSStatInfo.KEY_UID, row.getUid());
					json.put(WSStatInfo.KEY_EVENT, row.getEvent());
					json.put(WSStatInfo.KEY_VALUE, row.getValue());
					json.put(WSStatInfo.KEY_COUNT, row.getCount());
					json.put(WSStatInfo.KEY_LOG_TIME, WSUtil.getDateTime(row.getTime()));

					eventIds.add(id);
					data.put(json);
				}
			}
		} catch (JSONException e) {
			Log.e(TAG, TAG+ "[generateSendData]JSONException");
			if (isDebug()) {
				e.printStackTrace();
			}
		}

		SendData sendData = new SendData();
		sendData.setJson(data);
		sendData.setIdList(eventIds);
		return sendData;
	}

	private static class SendData {
		private JSONArray json;
		private List<Integer> idList;

		public JSONArray getJson() {
			return this.json;
		}

		public void setJson(JSONArray json) {
			this.json = json;
		}

		public List<Integer> getIdList() {
			return this.idList;
		}

		public void setIdList(List<Integer> idList) {
			this.idList = idList;
		}
	}
}

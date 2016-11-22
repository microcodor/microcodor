package com.wxdroid.basemodule.stats;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;

public class WSUtil {
	public static String mHost = "http://stats.wemi.mobi/";
	public static final String TAG = "WSUtil:WS:";
	public static TelephonyManager telephonyManager = null;
	public static ConnectivityManager connectivityManager = null;

	public static int[] getDateAndHour(long time)
	{
	    Date today = new Date(time);
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	    SimpleDateFormat hourFormat = new SimpleDateFormat("H");
	    return new int[] { Integer.parseInt(dateFormat.format(today)), Integer.parseInt(hourFormat.format(today)) };
	}
	  
	public static int[] getDateAndHour()
	{
		return getDateAndHour(System.currentTimeMillis());
	}
	
	public static int getDate(long time)
	{
	    Date today = new Date(time);
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	    return Integer.parseInt(dateFormat.format(today));
	}
	
	public static int getDate()
	{
		return getDate(System.currentTimeMillis());
	}
	
	public static String getDateTime(long time)
    {
	    Date today = new Date(time);
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return dateFormat.format(today);
	}
	  
	public static String getDateTime()
	{
		return getDateTime(System.currentTimeMillis());
	}
	
	public static int getHourAndMin(long time)
	{
		Date today = new Date(time);
	    SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");
	    return Integer.parseInt(dateFormat.format(today));
	}
	
	public static int getHourAndMin()
	{
		return getHourAndMin(System.currentTimeMillis());
	}

	public static String getVersionName(Context mContext) {
		String version = "";
		PackageManager manager = mContext.getPackageManager();
		PackageInfo info;
		try {
			info = manager.getPackageInfo(mContext.getPackageName(), 0);
			version = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return version;
	}

	public static int getVersionCode(Context mContext) {
		int verCode = -1;

		try {
			verCode = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return verCode;
	}
	
	public static String getAppKey(Context context)
	{
	   String appKey = "";
	      
	   try {
	        Object appKeyObj = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.get("WEIMISTATS_APPKEY");
	        
	        if ((appKeyObj instanceof Integer)) {
	          appKey = Integer.toString(((Integer)appKeyObj).intValue());
	        } else if ((appKeyObj instanceof Long)) {
	          appKey = Long.toString(((Long)appKeyObj).intValue());
	        } else {
	          appKey = (String)appKeyObj;
	        }
	      }
	      catch (Exception e)
	      {
	        Log.e(TAG, TAG + "[getAppKey]Can't find metadata \"WEIMISTATS_APPKEY\" in AndroidManifest.xml");
	        e.printStackTrace();
	      }
	    
	    return appKey;
	}
	
	public static String getChannel(Context context)
	{
	   String channel = "";
	      
	   try {
	        Object channelObj = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.get("WEIMISTATS_CHANNEL");
	        
	        if ((channelObj instanceof Integer)) {
	        	channel = Integer.toString(((Integer)channelObj).intValue());
	        } else if ((channelObj instanceof Long)) {
	        	channel = Long.toString(((Long)channelObj).intValue());
	        } else {
	        	channel = (String)channelObj;
	        }
	      }
	      catch (Exception e)
	      {
	        Log.e(TAG, TAG + "[getChannel]Can't find metadata \"WEIMISTATS_CHANNEL\" in AndroidManifest.xml");
	        e.printStackTrace();
	      }
	    
	    return channel;
	}

	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getDeviceId(Context mContext) {
		final TelephonyManager tm = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						mContext.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String deviceId = deviceUuid.toString();

		return MD5(deviceId).toLowerCase();
	}

    private synchronized static String getUserAgent() {
        Locale locale = Locale.getDefault();

        StringBuffer buffer = new StringBuffer();
        // Add version
        final String version = Build.VERSION.RELEASE;
        if (version.length() > 0) {
            buffer.append(version);
        } else {
            // default to "1.0"
            buffer.append("1.0");
        }  
        buffer.append("; ");
        final String language = locale.getLanguage();
        if (language != null) {
            buffer.append(language.toLowerCase());
            final String country = locale.getCountry();
            if (country != null) {
                buffer.append("-");
                buffer.append(country.toLowerCase());
            }
        } else {
            // default to "en"
            buffer.append("en");
        }
        // add the model for the release build
        if ("REL".equals(Build.VERSION.CODENAME)) {
            final String model = Build.MODEL;
            if (model.length() > 0) {
                buffer.append("; ");
                buffer.append(model);
            }
        }
        final String id = Build.ID;
        if (id.length() > 0) {
            buffer.append(" Build/");
            buffer.append(id);
        }
        
        final String base = "Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
        return String.format(base, buffer);
    }

	private static String read(InputStream inputStream, boolean isGzip)
			throws Exception {
		if (isGzip) {
			GZIPInputStream gzin = new GZIPInputStream(inputStream);

			InputStreamReader isr = new InputStreamReader(gzin, "UTF-8");
			java.io.BufferedReader br = new java.io.BufferedReader(isr);
			StringBuffer sb = new StringBuffer();
			String tempbf;
			while ((tempbf = br.readLine()) != null) {
				sb.append(tempbf);
				sb.append("\r\n");
			}
			isr.close();
			gzin.close();
			return sb.toString();
		} else {
			byte[] buffer = new byte[1024];
			int len = -1;
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			while ((len = inputStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			outSteam.close();
			inputStream.close();
			return new String(outSteam.toByteArray());
		}
	}

	public static byte[] compress(String data) {
		if (data == null || data.length() == 0) {
			return null;
		}

		try {
			byte[] input = data.getBytes("utf-8");
			Deflater deflater = new Deflater();
		      deflater.setInput(input);
		      deflater.finish();
		      @SuppressWarnings("unused")
			  int size = 0;
		      byte[] byteData = new byte[0];
		      byte[] buf = new byte[''];
		      while (!deflater.finished())
		      {
		        int byteCount = deflater.deflate(buf);
		        byte[] temp = new byte[byteData.length + byteCount];
		        for (int i = 0; i < byteData.length; i++) {
		          temp[i] = byteData[i];
		        }
		        for (int i = 0; i < byteCount; i++) {
		          temp[(byteData.length + i)] = buf[i];
		        }
		        byteData = temp;
		        size += byteCount;
		      }
		      
		      return byteData;
		} catch (IOException e) {
			return null;
		}
	  } 
	
	public static Object post(String uri, String data, boolean retry) {
		String url = mHost + uri;

		Log.v(TAG, TAG + url);

		DefaultHttpClient client = new DefaultHttpClient();

		HttpParams localHttpParams = client.getParams();
		localHttpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);
		HttpConnectionParams.setConnectionTimeout(localHttpParams, 5000);
		HttpConnectionParams.setSoTimeout(localHttpParams, 5000);
		HttpClientParams.setRedirecting(localHttpParams, true);

		HttpPost post = new HttpPost(url);
		post.setHeader("User-Agent", getUserAgent());
		post.setHeader("Accept-Encoding", "gzip");
		post.addHeader("Content-Encoding", "deflate");
		post.addHeader("Content-Type", "application/octet-stream");
		post.addHeader("Charset", "UTF-8");
	    
		if (data != null) {
			Log.v(TAG, TAG + data);
			
			ByteArrayEntity byteArray = new ByteArrayEntity(compress(data));
			post.setEntity(byteArray);
		}

		int mErrno = 0;
		JSONObject mResult = null;
		
		try {
			HttpResponse response = client.execute(post);
			int httpcode = response.getStatusLine().getStatusCode();
			if (httpcode != HttpStatus.SC_OK) {
				throw new RuntimeException();
			}

			Header encoding = response.getLastHeader("Content-Encoding");
			boolean isGzip = false;
			if (encoding != null
					&& encoding.getValue().toLowerCase().indexOf("gzip") > -1) {
				isGzip = true;
			}

			String content = read(response.getEntity().getContent(), isGzip);
			Log.v(TAG, TAG + content);
			
			mResult = new JSONObject(content);
			
			try {
				mErrno = mResult.getInt("errno");
			} catch (JSONException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}

		} catch (Exception e) {
			if(retry) {
				//return post(uri + "&retry=1", data, false);
			}
			
			throw new RuntimeException(e);
		} finally {
			if(retry) {
				//return post(uri + "&retry=1", data, false);
			}
			
			client.getConnectionManager().shutdown();
		}

		if (mErrno != 0) {
			throw new RuntimeException();
		}
		
		try {
			if(mResult.has("data")) {
				return mResult.getJSONObject("data");
			}			
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return null;
	}

	public static Object post(String uri, String data) {
		return post(uri, data, false);
	}
	
	public static Object post(String uri) {
		return post(uri, null, false);
	}
	
	public static TelephonyManager getTelephonyManager(Context context) {
		
		if (telephonyManager == null)
	    {
			telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
	    }
		
		return telephonyManager;
	}
	
	public static String getCarrier(Context context) {
		TelephonyManager telephonyManager = getTelephonyManager(context);

		String imsi = telephonyManager.getSubscriberId();
		String carrier = "";

		if ((imsi != null) && (imsi.trim().length() > 0)) {
			if ((imsi.startsWith("46000")) || (imsi.startsWith("46002"))) {
				carrier = "CMCC";//中国移动
			} else if (imsi.startsWith("46001")) {
				carrier = "CUCC";//中国联通
			} else if (imsi.startsWith("46003")) {
				carrier = "CTCC";//中国电信
			} else {
				carrier = telephonyManager.getSimOperatorName();
			}
		}

		return carrier;
	}

	public static ConnectivityManager getConnectivityManager(Context context) {
		if (connectivityManager == null)
	    {
			connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    }
		
		return connectivityManager;
	}
			
	@SuppressWarnings("deprecation")
	public static String getNetWorkType(Context context) {
		ConnectivityManager manager = getConnectivityManager(context);
		
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			String type = networkInfo.getTypeName();

			if (type.equalsIgnoreCase("WIFI")) {
				return "wifi";
			} else if (type.equalsIgnoreCase("MOBILE")) {
				String proxyHost = android.net.Proxy.getDefaultHost();
				if(proxyHost == null || proxyHost.length() == 0) { 
					TelephonyManager telephonyManager = getTelephonyManager(context);
					
					switch (telephonyManager.getNetworkType()) {
					case TelephonyManager.NETWORK_TYPE_1xRTT:// ~ 50-100 kbps
					case TelephonyManager.NETWORK_TYPE_CDMA:// ~ 14-64 kbps
					case TelephonyManager.NETWORK_TYPE_IDEN:// ~25 kbps
					case TelephonyManager.NETWORK_TYPE_EDGE:// ~ 50-100 kbps
					case TelephonyManager.NETWORK_TYPE_GPRS:// ~ 100 kbps
					case TelephonyManager.NETWORK_TYPE_UNKNOWN:
						return "2g";
					case TelephonyManager.NETWORK_TYPE_EVDO_0:// ~ 400-1000 kbps
					case TelephonyManager.NETWORK_TYPE_EVDO_A:// ~ 600-1400 kbps
					case TelephonyManager.NETWORK_TYPE_HSDPA:// ~ 2-14 Mbps
					case TelephonyManager.NETWORK_TYPE_HSPA:// ~ 700-1700 kbps
					case TelephonyManager.NETWORK_TYPE_HSUPA:// ~ 1-23 Mbps
					case TelephonyManager.NETWORK_TYPE_UMTS:// ~ 400-7000 kbps
					case TelephonyManager.NETWORK_TYPE_EHRPD:// ~ 1-2 Mbps
					case TelephonyManager.NETWORK_TYPE_EVDO_B:// ~ 5 Mbps
					case TelephonyManager.NETWORK_TYPE_HSPAP:// ~ 10-20 Mbps
					case TelephonyManager.NETWORK_TYPE_LTE:// ~ 10+ Mbps
						return "3g"; 
					default:
						return "2g";
					}
				} else {
					return "wap";
				}
			}
		}
		
		return "unkown";
	}

	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = getConnectivityManager(context);
			
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
}
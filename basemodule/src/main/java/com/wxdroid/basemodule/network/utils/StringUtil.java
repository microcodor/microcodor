package com.wxdroid.basemodule.network.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 字符串处理工具类
 * 
 * @author like1
 * 
 */
public class StringUtil {

	/**
	 * 拼装网络请求地址
	 * 
	 * @param url
	 *            网络请求地址
	 * @param paramsMap
	 *            待拼接的参数集合
	 * @return 拼接后的请求地址
	 */
	public static String organizeUrl(String url, Map paramsMap) {
		if (url != null) {
			if (paramsMap != null) {
				String args = "";
				if (!"".equals(paramsMap) && paramsMap.size() > 0) {
					Set<String> key = paramsMap.keySet();
					for (Iterator it = key.iterator(); it.hasNext();) {
						String s = (String) it.next();
						args = args + s + "=" + paramsMap.get(s) + "&";
					}
				}

				if (args != null && args.length() > 0) {
					args = args.substring(0, args.length() - 1);
					url = url + "?" + args;
				}
			}

			return url;
		}

		return null;
	}

	public static String organizeParams(Map paramsMap) {
		String args = "";
		if (paramsMap != null) {
			if (!"".equals(paramsMap) && paramsMap.size() > 0) {
				Set<String> key = paramsMap.keySet();
				for (Iterator it = key.iterator(); it.hasNext();) {
					String s = (String) it.next();
					args = args + s + "=" + paramsMap.get(s) + "&";
				}
			}

			if (args != null && args.length() > 0) {
				args = args.substring(0, args.length() - 1);
				args = "?" + args;
			}
		}

		return args;
	}
}

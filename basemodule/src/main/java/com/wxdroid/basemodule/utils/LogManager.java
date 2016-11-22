package com.wxdroid.basemodule.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;


import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;

public class LogManager {

    private String BASE_PATH = null;                        //存储路径根目录
    private String ZIP_PATH = null;//存储zip的目录
    private String LOG_PATH = null;//存储日志的目录
    private String CHAT_LOG_PATH = null;//存储谈谈日志的目录
    private String CRASH_LOG_PATH = null;//存储崩溃日志的目录

    private static final String LOG_PHONE_INFO = "phoneinfo.txt";  //设备信息
    private static final String LOG_HTTP = "http.txt";//http请求打点
    private static final String LOG_EVENT = "event.txt";//自定义事件打点


    private Date mDate = null;                            //
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Context mContext;
    private HashMap<String, String> hashMap;
    private static volatile LogManager mInstance;

    private LogManager() {

    }

    public static LogManager getInstance() {
        if (mInstance == null) {
            synchronized (LogManager.class) {
                if (mInstance == null) {
                    mInstance = new LogManager();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context, HashMap<String, String> map) {
        mContext = context;
        hashMap = map;

        initPath(mContext);

    }


    /**
     * 设备信息
     */
    private void savePhoneInfo() {
        File phoneInfoFile = new File(getCurDateFolderPath() + LOG_PHONE_INFO);
        if (phoneInfoFile.exists()) {
            return;
        }
        String phoneInfo = System.currentTimeMillis() + "\n";//打上时间戳
        phoneInfo += "MODEL:" + Build.MODEL + "\n";//手机型号
        phoneInfo += "系统版本:" + Build.VERSION.RELEASE + "\n";//系统版本
        if (hashMap != null) {
            if (hashMap.containsKey("appver")) {
                String appver = hashMap.get("appver");
                phoneInfo += "app版本: " + appver + "\n";//app版本
            }
            if (hashMap.containsKey("channel")) {
                String channel = hashMap.get("channel");
                phoneInfo += "渠道: " + channel + "\n";//app渠道
            }
        }
        writeFileSdcard(getCurDateFolderPath() + LOG_PHONE_INFO, phoneInfo);
    }


    public String appendRequestResult(String requestStr, String responseStr, int errno, String errMsg) {
        StringBuffer stringBuffer = new StringBuffer(requestStr);
        stringBuffer.append("\n");
        stringBuffer.append("result:");
        if (errno == 0) {
            stringBuffer.append("OK");
            stringBuffer.append("\n");
            stringBuffer.append(responseStr);
        } else {
            stringBuffer.append("ERROR");
            stringBuffer.append("\n");
            stringBuffer.append("errno:" + errno);
            stringBuffer.append("\n");
            stringBuffer.append("errMsg:" + errMsg);
        }
        return stringBuffer.toString();

    }


    /**
     * HTTP 打点
     */

    public void collectHttpLog(String log) {
        if (TextUtils.isEmpty(log)/*||!WRITE*/) {
            return;
        }
        initPath(mContext);
        savePhoneInfo();
        saveFile(getCurDateFolderPath() + LOG_HTTP, log);
//        Log.d("liuwei", "collectHttpLog:" + log);
    }

    /**
     * Event 打点
     *
     * @param log
     */
    public void collectEventLog(String log) {
        if (TextUtils.isEmpty(log)/*||!WRITE*/) {
            return;
        }
        initPath(mContext);
        savePhoneInfo();
        saveFile(getCurDateFolderPath() + LOG_EVENT, log);
//        Log.d("liuwei", "collectEventLog:" + log);
    }

    /**
     * 获取当前日期20160506文件夹，没有则创建
     *
     * @return
     */
    private String getCurDateFolderPath() {
        File folder = new File(LOG_PATH, getDateStr());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath() + File.separator;
    }

    /**
     * 获取chat_log文件夹，没有则创建
     *
     * @return
     */
    public String getChatLogFolderPath() {
        if (CHAT_LOG_PATH == null) {
            initPath(mContext);
        }
        File folder = new File(CHAT_LOG_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return CHAT_LOG_PATH;
    }

    /**
     * 获取crash文件夹，没有则创建
     *
     * @return
     */
    public String getCrashFolderPath() {
        if (CRASH_LOG_PATH == null) {
            initPath(mContext);
        }
        File folder = new File(CRASH_LOG_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return CRASH_LOG_PATH;
    }

    private String getDateStr() {
        if (mDate == null) {
            mDate = new Date(System.currentTimeMillis());// 获取当前时间
        } else {
            mDate.setTime(System.currentTimeMillis());
        }
        return dateFormatter.format(mDate);
    }

    /**
     * 压缩并上传
     * @param uid
     */
//    public synchronized void doZipAndUploadWholeLogFolder(final String uid) {
//        if (BASE_PATH == null) {
//            return;
//        }
//        new AsyncTask<Void, Void, Integer>() {
//            @Override
//            protected Integer doInBackground(Void... params) {
//                ArrayList<String> list = new ArrayList<String>();
//                //说明有可压缩的
//                String zipName = null;
//                String srcZipPath = BASE_PATH;
//                String time = getDateStr();
//
//                if (TextUtils.isEmpty(uid)) {
//                    zipName = "logfolder_"+"youke_" + time + ".zip";
//                } else {
//                    zipName = "logfolder_"+ uid + "_" + time + ".zip";
//                }
//                //将zipfile路径放到list中
//                File oldZipFile = new File(ZIP_PATH + zipName);
//                if(oldZipFile.exists()){//如果有之前的zip
//                    list.add(oldZipFile.getAbsolutePath());
//                }else {
//
//                    File srcZipPathFile = new File(srcZipPath);
//                    ZipUtils.zipFolder(srcZipPath, ZIP_PATH + zipName);
//
//                    //将zipfile路径放到list中
//                    File zipFile = new File(ZIP_PATH + zipName);
//                    if (zipFile.exists()) {
//                        list.add(zipFile.getAbsolutePath());
//                    }
//                    //删除压缩前的目录文件
////                    deleteFile(srcZipPathFile);
//                }
//
//                if (!list.isEmpty()) {
//                    HashMap<String,String> paramMap = new HashMap<String, String>();
//                    paramMap.put("uid",uid);
//                    paramMap.put("time",time);
//                    paramMap.put("type","logfolder");
//                    UploadFilesUtils.syncUploadLog(list,paramMap);
//
//                    return 1;
//                }
//                return 0;
//            }
//
//            @Override
//            protected void onPostExecute(Integer result) {
//                super.onPostExecute(result);
//                if(result == 1){
//                    ToastUtils.showToast(mContext,"日志上传完毕");
//                }
//            }
//        }.executeOnExecutor(Executors.newCachedThreadPool());
//    }


    /**
     * 压缩并上传
     * @param uid
     */
//    public synchronized void doZipAndUpload(final String uid,final String dateFolderName) {
//        if (LOG_PATH == null) {
//            return;
//        }
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                ArrayList<String> list = new ArrayList<String>();
//                //说明有可压缩的
//                String zipName = null;
//                String srcZipPath = null;
//                String time = getDateStr();
////                int zipFolderT20ype = 0;//0 是LOG_PATH下所有文件夹，1是LOG_PATH下指定文件夹dateFolderName
//                if(dateFolderName == null || "".endsWith(dateFolderName)){//压缩LOG_PATH目录下所有文件
//                    srcZipPath = LOG_PATH;
//                }else{
//                    time = dateFolderName;//只压缩需要的目录
//                    File dateFolder = new File(LOG_PATH + dateFolderName);
//                    if(!dateFolder.exists() || dateFolder.listFiles() == null || dateFolder.listFiles().length == 0){
//                        return null;//文件不存在，则不上传
//                    }
//                    srcZipPath = LOG_PATH + dateFolderName + File.separator;
//                }
//
//                if (TextUtils.isEmpty(uid)) {
//                    zipName = "httplog_"+"youke_" + time + ".zip";
//                } else {
//                    zipName = "httplog_"+ uid + "_" + time + ".zip";
//                }
//                //将zipfile路径放到list中
//                File oldZipFile = new File(ZIP_PATH + zipName);
//                if(oldZipFile.exists()){//如果有之前的zip
//                    list.add(oldZipFile.getAbsolutePath());
//                }else {
//
//                    File srcZipPathFile = new File(srcZipPath);
//                    ZipUtils.zipFolder(srcZipPath, ZIP_PATH + zipName);
//
//                    //将zipfile路径放到list中
//                    File zipFile = new File(ZIP_PATH + zipName);
//                    if (zipFile.exists()) {
//                        list.add(zipFile.getAbsolutePath());
//                    }
//                    //删除压缩前的目录文件
//                    deleteFile(srcZipPathFile);
//                }
//
//                if (!list.isEmpty()) {
//                    HashMap<String,String> paramMap = new HashMap<String, String>();
//                    paramMap.put("uid",uid);
//                    paramMap.put("time",time);
//                    paramMap.put("type","httplog");
//                    UploadFilesUtils.syncUploadLog(list,paramMap);
//                }
//                return null;
//            }
//        }.executeOnExecutor(Executors.newCachedThreadPool());
//    }


    /**
     * 压缩并上传
     * @param uid
     */
//    public synchronized void doZipAndUploadChatLog(final String uid) {
//        if (CHAT_LOG_PATH == null) {
//            return;
//        }
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                ArrayList<String> list = new ArrayList<String>();
//                //说明有可压缩的
//                String zipName = null;
//                String srcZipPath = CHAT_LOG_PATH;
//                String time = getDateStr();
//
//                if (TextUtils.isEmpty(uid)) {
//                    zipName = "chatlog_"+ "youke_" + time + ".zip";
//                } else {
//                    zipName = "chatlog_"+ uid + "_" + time + ".zip";
//                }
//
//                //将zipfile路径放到list中
//                File oldZipFile = new File(ZIP_PATH + zipName);
//                if(oldZipFile.exists()){//如果有之前的zip
//                    list.add(oldZipFile.getAbsolutePath());
//                }else {
//                    File srcZipPathFile = new File(srcZipPath);
//                    ZipUtils.zipFolder(srcZipPath, ZIP_PATH + zipName);
//
//                    //将zipfile路径放到list中
//                    File zipFile = new File(ZIP_PATH + zipName);
//                    if (zipFile.exists()) {
//                        list.add(zipFile.getAbsolutePath());
//                    }
//                    //删除压缩前的目录文件
////                    deleteFile(srcZipPathFile);
//                }
//                if (!list.isEmpty()) {
//                    HashMap<String,String> paramMap = new HashMap<String, String>();
//                    paramMap.put("uid",uid);
//                    paramMap.put("time",time);
//                    paramMap.put("type","chatlog");
//                    UploadFilesUtils.syncUploadLog(list,paramMap);
//                }
//                return null;
//            }
//        }.executeOnExecutor(Executors.newCachedThreadPool());
//    }

    /**
     * 崩溃压缩并上传
     * @param uid
     */
//    public synchronized void doZipAndUploadCrash(final String uid) {
//        if (CRASH_LOG_PATH == null) {
//            return;
//        }
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                ArrayList<String> list = new ArrayList<String>();
//                //说明有可压缩的
//                String zipName = null;
//                String srcZipPath = CRASH_LOG_PATH;
//                String time = getDateStr();
//
//                if (TextUtils.isEmpty(uid)) {
//                    zipName = "crash_"+ "youke_" + time + ".zip";
//                } else {
//                    zipName = "crash_"+ uid + "_" + time + ".zip";
//                }
//
//                //将zipfile路径放到list中
//                File oldZipFile = new File(ZIP_PATH + zipName);
//                if(oldZipFile.exists()){//如果有之前的zip
//                    list.add(oldZipFile.getAbsolutePath());
//                }else {
//                    File srcZipPathFile = new File(srcZipPath);
//                    ZipUtils.zipFolder(srcZipPath, ZIP_PATH + zipName);
//
//                    //将zipfile路径放到list中
//                    File zipFile = new File(ZIP_PATH + zipName);
//                    if (zipFile.exists()) {
//                        list.add(zipFile.getAbsolutePath());
//                    }
//                    //删除压缩前的目录文件
//                    deleteFile(srcZipPathFile);
//                }
//                if (!list.isEmpty()) {
//                    HashMap<String,String> paramMap = new HashMap<String, String>();
//                    paramMap.put("uid",uid);
//                    paramMap.put("time",time);
//                    paramMap.put("type","crash");
//                    UploadFilesUtils.syncUploadLog(list,paramMap);
//                }
//                return null;
//            }
//        }.executeOnExecutor(Executors.newCachedThreadPool());
//    }

    /**
     * 删除文件或者目录
     *
     * @param file 要删除的文件
     */
    public void deleteFile(File file) {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                File[] filelist = file.listFiles();
                if (filelist != null && filelist.length > 0) {
//                    File[] delFiles = file.listFiles();
                    for (File delFile : filelist) {
                        if (delFile.exists())
                            deleteFile(delFile);
                    }
                }
            }
            file.delete();
        }
    }

    /**
     * 删除超过 xx天的文件路径
     *
     * @param days
     */
    public void delExpiredLogFiles(final long days) {
        if (LOG_PATH == null || CHAT_LOG_PATH == null || ZIP_PATH == null || BASE_PATH == null) {
            initPath(mContext);
        }
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                long maxDuration = days * 24 * 60 * 60 * 1000;//毫秒
                //删除log文件夹下过期文件
                File file = new File(LOG_PATH);
                if (file.exists()) {
                    File[] files = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        File tmpFile = files[i];
                        long lastTime = tmpFile.lastModified();
                        long curTime = System.currentTimeMillis();
                        if ((curTime - lastTime) > maxDuration) {
                            deleteFile(tmpFile);
                        }
                    }
                }
                //删除chatlog文件夹下过期文件
                file = new File(CHAT_LOG_PATH);
                if (file.exists()) {
                    File[] files = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        File tmpFile = files[i];
                        long lastTime = tmpFile.lastModified();
                        long curTime = System.currentTimeMillis();
                        if ((curTime - lastTime) > maxDuration) {
                            deleteFile(tmpFile);
                        }
                    }
                }
                //删除ZIP文件夹下过期文件
                file = new File(ZIP_PATH);
                if (file.exists()) {
                    File[] files = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        File tmpFile = files[i];
                        long lastTime = tmpFile.lastModified();
                        long curTime = System.currentTimeMillis();
                        if ((curTime - lastTime) > maxDuration) {
                            deleteFile(tmpFile);
                        }
                    }
                }
                return null;
            }
        }.executeOnExecutor(Executors.newCachedThreadPool());
    }

    /**
     * 初始化log存储路径，不存在则创建
     */
    private void initPath(Context context) {
        if (BASE_PATH == null) {
            BASE_PATH = GlobalFunctions.GetAppDir(context) + "log_folder/";
        }

        File file = new File(BASE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        initChildFolder();
    }

    private void initChildFolder() {
        //initZipFolder();
        initLogFolder();
        //initChatLogFolder();
        //initCrashFolder();
    }

    private void initZipFolder() {
        File file = new File(BASE_PATH, "zip");
        if (!file.exists()) {
            file.mkdirs();
        }
        ZIP_PATH = BASE_PATH + "zip" + File.separator;
    }

    private void initLogFolder() {
        File file = new File(BASE_PATH, "log");
        if (!file.exists()) {
            file.mkdirs();
        }
        LOG_PATH = BASE_PATH + "log" + File.separator;
    }

    private void initChatLogFolder() {
        File file = new File(BASE_PATH, "chat_log");
        if (!file.exists()) {
            file.mkdirs();
        }
        CHAT_LOG_PATH = BASE_PATH + "chat_log" + File.separator;
    }

    private void initCrashFolder() {
        File file = new File(BASE_PATH, "crash");
        if (!file.exists()) {
            file.mkdirs();
        }
        CRASH_LOG_PATH = BASE_PATH + "crash" + File.separator;
    }

    /**
     * @param filePath 文件路径
     * @param msg      输出的内容
     */
    private synchronized void saveFile(String filePath, String msg) {
        if (mDate == null) {
            mDate = new Date(System.currentTimeMillis());// 获取当前时间
        } else {
            mDate.setTime(System.currentTimeMillis());
        }
        writeFileSdcard(filePath, dateTimeFormatter.format(mDate) + " : " + msg + "\n");
    }

    /**
     * 写文件
     *
     * @param filePath 文件路径
     * @param message  追加内容
     */
    private void writeFileSdcard(String filePath, String message) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath, true);
            byte[] bytes = message.getBytes();
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

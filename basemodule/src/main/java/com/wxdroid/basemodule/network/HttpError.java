package com.wxdroid.basemodule.network;

public class HttpError extends Exception
{

    public static final String ERROR_MSG_BUSINESS = "无网络或连接服务器失败";
    public static final String ERROR_MSG_PARSER = "解析失败";
    public static final String ERROR_MSG_PARSER_CLASS_TYPE = "类型不正确-解析失败";
    public static final String ERROR_MSG_PARSER_BEAN_NULL = "data封装失败，bean为空";
    public static final String ERROR_MSG_PARSER_DATA_NULL = "业务数据data为空";
    public static final String ERROR_MSG_PARSER_GET_CLASS = "类型获取失败-解析失败";
    public static final String ERROR_MSG_READ_DATA = "读取网络数据失败";

    public int mErrorType = ErrorType.ERROR_UNKNOWN;

    public interface ErrorType {
        int ERROR_UNKNOWN = 0;
        int ERROR_INVALID_VALUE = 1;    //无效的返回值
        int ERROR_PARSE = 2;            //解析失败
        int ERROR_NET_SERVER = 3;            // 无网络或连接服务器失败
        int ERROR_READ_DATA = 4;            //读取网络数据失败
        int ERROR_DATA_NULL = 5;            //业务数据为空
        int ERROR_FILE_NULL = 6;            //下载地址为空
    }

    public HttpError(String exceptionMessage) {
        super(exceptionMessage);
    }

    public HttpError(String exceptionMessage, Throwable reason) {
        super(exceptionMessage, reason);
    }

    public HttpError(Throwable cause) {
        super(cause);
    }

    public HttpError(Throwable cause, int errorType) {
        super(cause);
        mErrorType = errorType;
    }

    public HttpError(String exceptionMessage, int errorType) {
        super(exceptionMessage);
        mErrorType = errorType;
    }

    public int getErrorType(){
        return mErrorType;
    }

    public void setErrorType(int errorType){
        mErrorType = errorType;
    }
}

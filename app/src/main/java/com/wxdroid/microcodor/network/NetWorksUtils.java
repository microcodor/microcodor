package com.wxdroid.microcodor.network;

import com.wxdroid.microcodor.model.WpPostsModel;
import com.wxdroid.microcodor.model.bean.WpPostsModelBean;
import com.wxdroid.microcodor.model.bean.WpPostsModelListBean;
import com.wxdroid.microcodor.model.bean.WptermsBean;
import com.wxdroid.microcodor.network.utils.RetrofitUtils;

import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 类名称：NetWorks
 * 创建人：wangliteng
 * 创建时间：2016-5-18 14:57:11
 * 类描述：网络请求的操作类
 */
public class NetWorksUtils extends RetrofitUtils {

    protected static final NetService service = getRetrofit().create(NetService.class);

    //设缓存有效期为1天
    protected static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，使用缓存
    protected static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置。不使用缓存
    protected static final String CACHE_CONTROL_NETWORK = "max-age=0";

    private interface NetService {


//
//        //POST请求
//        @FormUrlEncoded
//        @POST("bjws/app.user/login")
//        Observable<Verification> getVerfcationCodePostMap(@FieldMap Map<String, String> map);
//
//        //GET请求
//        @GET("bjws/app.user/login")
//        Observable<Verification> getVerfcationGet(@Query("tel") String tel, @Query("password") String pass);
//
//
//        //GET请求，设置缓存
//        @Headers("Cache-Control: public," + CACHE_CONTROL_CACHE)
//        @GET("bjws/app.user/login")
//        Observable<Verification> getVerfcationGetCache(@Query("tel") String tel, @Query("password") String pass);
//
//
//        @Headers("Cache-Control: public," + CACHE_CONTROL_NETWORK)
//        @GET("bjws/app.menu/getMenu")
//        Observable<MenuBean> getMainMenu();

        //GET请求
        @GET("getwpterms")
        Observable<WptermsBean> getWpterms();

        //GET请求
        @GET("getsimpleposts/{termId}/{postId}/{num}")
        Observable<WpPostsModelListBean> getSimplePosts(@Path("termId") long termId, @Path("postId") long postId, @Path("num") int num);

        @GET("getwppost/{postId}")
        Observable<WpPostsModelBean> getWpPost(@Path("postId") long postId);

        //GET请求
        @GET("searchsimpleposts/{keyword}/{index}/{num}")
        Observable<WpPostsModelListBean> searchSimplePosts(@Path("keyword") String keyword, @Path("index") int index, @Path("num") int num);


    }

    //POST请求
//    public static void verfacationCodePost(String tel, String pass, Observer<Verification> observer){
//        setSubscribe(service.getVerfcationCodePost(tel, pass),observer);
//    }
//
//
//    //POST请求参数以map传入
//    public static void verfacationCodePostMap(Map<String, String> map, Observer<Verification> observer) {
//       setSubscribe(service.getVerfcationCodePostMap(map),observer);
//    }
//
//    //Get请求设置缓存
//    public static void verfacationCodeGetCache(String tel, String pass, Observer<Verification> observer) {
//        setSubscribe(service.getVerfcationGetCache(tel, pass),observer);
//    }
//
//    //Get请求
//    public static void verfacationCodeGet(String tel, String pass, Observer<Verification> observer) {
//        setSubscribe(service.getVerfcationGet(tel, pass),observer);
//    }
//
//    //Get请求
//    public static void verfacationCodeGetsub(String tel, String pass, Observer<Verification> observer) {
//        setSubscribe(service.getVerfcationGet(tel, pass),observer);
//    }
//
//    //Get请求
//    public static void Getcache( Observer<MenuBean> observer) {
//        setSubscribe(service.getMainMenu(),observer);
//    }
    //Get请求
    public static void GetWpterms(Observer<WptermsBean> observer) {
        setSubscribe(service.getWpterms(), observer);
    }
    public static void GetSimplePosts(long termId, long postId, int num, Observer<WpPostsModelListBean> observer) {
        setSubscribe(service.getSimplePosts(termId, postId, num), observer);
    }
    //public static void GetSimplePosts(Observer<>)
    public static void GetWpPost(long postId,Observer<WpPostsModelBean> observer) {
        setSubscribe(service.getWpPost(postId), observer);
    }
    public static void SearchSimplePosts(String keyword, int index, int num, Observer<WpPostsModelListBean> observer) {
        setSubscribe(service.searchSimplePosts(keyword, index, num), observer);
    }
    /**
     * 插入观察者
     *
     * @param observable
     * @param observer
     * @param <T>
     */
    public static <T> void setSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(observer);
    }

}

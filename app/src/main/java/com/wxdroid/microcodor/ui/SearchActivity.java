package com.wxdroid.microcodor.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.wxdroid.basemodule.utils.ToastUtils;
import com.wxdroid.microcodor.R;
import com.wxdroid.microcodor.base.BaseAdapterHelper;
import com.wxdroid.microcodor.base.BaseAppCompatActivity;
import com.wxdroid.microcodor.base.BaseQuickAdapter;
import com.wxdroid.microcodor.model.WpPostsModel;
import com.wxdroid.microcodor.model.bean.WpPostsModelListBean;
import com.wxdroid.microcodor.network.NetWorksUtils;
import com.wxdroid.microcodor.util.LogUtil;
import com.wxdroid.microcodor.util.StringUtil;
import com.wxdroid.microcodor.widget.QuickAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * Created by jinchun on 2016/11/19.
 */

public class SearchActivity extends BaseAppCompatActivity{

    private MenuItem search;
    private MaterialRefreshLayout mRefreshLayout;
    private RecyclerView recyclerView;
    private QuickAdapter<WpPostsModel> mQuickAdapter;
    private List<WpPostsModel> wpPostsModelList = new ArrayList<>();

    private SearchView searchView;
    private String mKeyword = null;
    private InputMethodManager inputmanger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void setupView() {
        inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        showBack();
        mRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.materialRefreshLayout);

        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                mRefreshLayout.finishRefresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (!TextUtils.isEmpty(mKeyword)&&wpPostsModelList.size()>0){
                    searchSimplePosts(mKeyword, wpPostsModelList.size(), 8);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * 刷新完成后调用此方法，要不然刷新效果不消失
                         */
                        mRefreshLayout.finishRefreshLoadMore();
                    }
                }, 800);
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.search_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mQuickAdapter = new QuickAdapter<WpPostsModel>(this,R.layout.item_search_posts,wpPostsModelList) {
            @Override
            protected void convert(BaseAdapterHelper helper, WpPostsModel item) {
                helper.getTextView(R.id.author_name).setText(item.getUser().getUser_nicename());

                helper.getTextView(R.id.post_title).setText(""+item.getPost_title());
                helper.getTextView(R.id.post_content).setText(StringUtil.delHTMLTag(item.getPost_content()));
            }
        };
        mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //ToastUtils.showToast(getActivity(),""+wpPostsModelList.get(position).getPost_title());
                Intent intent = new Intent();
                intent.putExtra("postId",wpPostsModelList.get(position).getId());
                intent.setClass(SearchActivity.this, ArticleActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(mQuickAdapter);
    }

    @Override
    protected void setupData() {

    }
    /**
     * 查询简版文章列表成功 getSimplePostsList(9,1,8);
     * */
    private void searchSimplePosts(String keyword, int index, int num){
        NetWorksUtils.SearchSimplePosts(keyword, index, num, new Observer<WpPostsModelListBean>() {
            @Override
            public void onCompleted() {
                Log.d("onNext-onCompleted", "onCompleted-getSimplePostsList");
                /**
                 * 刷新完成后调用此方法，要不然刷新效果不消失
                 */
            }

            @Override
            public void onError(Throwable e) {
                //异常
                Log.e("MAIN3", e.getLocalizedMessage() + "--" + e.getMessage());
                ToastUtils.showToast(SearchActivity.this,""+e.getMessage());
            }

            @Override
            public void onNext(WpPostsModelListBean wpPostsModelListBean) {
                Log.d("onNext", "getSimplePostsList:" + wpPostsModelListBean.getCommon().getCode() + ";" + wpPostsModelListBean.getCommon().getMsg());
                Log.d("onNext","wpPostsModelListBean.getData():"+ wpPostsModelListBean.getData().size());
                if (wpPostsModelList.size()>0){
                    mRefreshLayout.finishRefreshLoadMore();
                }
                if (wpPostsModelListBean!=null&&wpPostsModelListBean.getCommon().getCode()==1){
                    Log.d("onNext","lastid:"+ wpPostsModelListBean.getData().get(wpPostsModelListBean.getData().size()-1).getId());
                    wpPostsModelList.addAll(wpPostsModelListBean.getData());
                    mQuickAdapter.notifyDataSetChanged();
                }else {
                    ToastUtils.showToast(SearchActivity.this,""+wpPostsModelListBean.getCommon().getMsg());
                }

            }
        });
    }
    @Override
    protected void setChouTi() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        search = menu.findItem(R.id.action_search);
        search.collapseActionView();
        //是搜索框默认展开
        search.expandActionView();
        searchView = (SearchView)search.getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.d(TAG,"searchView:"+(searchView!=null));
                LogUtil.d(TAG,"searchView-query:"+(searchView.getQuery().toString()));
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mKeyword = null;
                LogUtil.d(TAG,"searchView:onQueryTextSubmit");
                if (searchView!=null){

                    if (TextUtils.isEmpty(searchView.getQuery())){
                        ToastUtils.showToast(SearchActivity.this,"关键词不能为空");
                        return true;
                    }else {
                        String keyword = searchView.getQuery().toString();
                        if (!sqlValidate(keyword)){
                            mKeyword = keyword;
                            wpPostsModelList.clear();
                            searchSimplePosts(keyword, wpPostsModelList.size(), 8);
                        }else {
                            ToastUtils.showToast(SearchActivity.this,"关键词不合法，不能带有特殊符号！");
                        }
                    }


                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LogUtil.d(TAG,"searchView:onQueryTextChange");
                return false;
            }
        });
        //处理返回键
        MenuItemCompat.setOnActionExpandListener(search, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //关闭软键盘
                if (searchView != null) {
                    searchView.clearFocus();
                }
                SearchActivity.this.finish();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                ToastUtils.showToast(SearchActivity.this,"search");

                break;

        }
        return true;
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        LogUtil.d(TAG,"onBackPressed#####");
//    }
    //效验
    protected static boolean sqlValidate(String str) {
        str = str.toLowerCase();//统一转为小写
//        String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|" +
//                "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" +
//                "table|from|grant|use|group_concat|column_name|" +
//                "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|" +
//                "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";//过滤掉的sql关键字，可以手动添加
        String badStr = "'|*|%|" +
                ";|-|--|+|,|like|//|/|%|#";//过滤掉的sql关键字，可以手动添加
        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
                return true;
            }
        }
        return false;
    }
    /**
     * 编写过滤html标签的代码，代码:
     */
    public static String splitHtml(String s){
        if(!s.equals("")||s!=null){
            String str=s.replaceAll("<[.[^<]]*>","");
            return str;
        }else{
            return s;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

package com.wxdroid.microcodor.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.wxdroid.basemodule.utils.ToastUtils;
import com.wxdroid.microcodor.R;
import com.wxdroid.microcodor.base.BaseAdapterHelper;
import com.wxdroid.microcodor.base.BaseFragment;
import com.wxdroid.microcodor.base.BaseQuickAdapter;
import com.wxdroid.microcodor.model.WpPostsModel;
import com.wxdroid.microcodor.model.bean.WpPostsModelListBean;
import com.wxdroid.microcodor.network.NetWorksUtils;
import com.wxdroid.microcodor.ui.ArticleActivity;
import com.wxdroid.microcodor.ui.MainActivity;
import com.wxdroid.microcodor.ui.SplashActivity;
import com.wxdroid.microcodor.util.LogUtil;
import com.wxdroid.microcodor.widget.QuickAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * Created by jinchun on 2016/11/30.
 */

public class CommonFragment extends BaseFragment{
    private static String TAG = "CommonFragment";
    private View view;

    private MaterialRefreshLayout mRefreshLayout;

    private RecyclerView recyclerView;
    private QuickAdapter<WpPostsModel> mQuickAdapter;
    private List<WpPostsModel> wpPostsModelList = new ArrayList<>();
    private long termId = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_common, container, false);
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null) {
            termId = bundle.getLong("termId",0);
            TAG = TAG+"-"+termId;
        }
        initRefresh();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d(TAG,"onResume");

    }

    private void initRefresh() {
        mRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.materialRefreshLayout);

        //mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        //ToastUtils.showToast(getActivity(), "已经没有更多数据了");

                    }
                }, 800);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(getActivity(), "已经没有更多数据了");
                        /**
                         * 刷新完成后调用此方法，要不然刷新效果不消失
                         */
                        mRefreshLayout.finishRefreshLoadMore();
                    }
                }, 3000);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.content_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mQuickAdapter = new QuickAdapter<WpPostsModel>(getActivity(),R.layout.item_simple_posts,wpPostsModelList) {
            @Override
            protected void convert(BaseAdapterHelper helper, WpPostsModel item) {
                helper.getTextView(R.id.author_name).setText(item.getUser().getUser_nicename());
                helper.getTextView(R.id.post_time).setText(item.getPost_date());
                helper.getTextView(R.id.post_title).setText(item.getPost_title());
                helper.getTextView(R.id.read_num).setText(""+(item.getViews_count()+77));
                helper.getTextView(R.id.comment_num).setText(""+item.getComment_count());
                helper.getTextView(R.id.like_num).setText("0");
            }
        };
        mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //ToastUtils.showToast(getActivity(),""+wpPostsModelList.get(position).getPost_title());
                Intent intent = new Intent();
                intent.putExtra("postId",wpPostsModelList.get(position).getId());
                intent.setClass(getActivity(), ArticleActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(mQuickAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.autoRefresh();
            }
        }, 800);
    }
    private void initData(){
        if (termId!=0){
            if (wpPostsModelList.size()>0) {
                getSimplePostsList(termId, wpPostsModelList.get(0).getId(), 8);
            }else {
                getSimplePostsList(termId, 1, 8);
            }

        }
    }


    /**
     * 查询简版文章列表成功 getSimplePostsList(9,1,8);
     * */
    private void getSimplePostsList(final long termId, long postId, int num){
        NetWorksUtils.GetSimplePosts(termId, postId, num, new Observer<WpPostsModelListBean>() {
            @Override
            public void onCompleted() {
                Log.d("onNext-onCompleted", "onCompleted-getSimplePostsList");
                /**
                 * 刷新完成后调用此方法，要不然刷新效果不消失
                 */
                mRefreshLayout.finishRefresh();
            }

            @Override
            public void onError(Throwable e) {
                //异常
                Log.e("MAIN3", e.getLocalizedMessage() + "--" + e.getMessage());
                ToastUtils.showToast(getActivity(),""+e.getMessage());
                mRefreshLayout.finishRefresh();
            }

            @Override
            public void onNext(WpPostsModelListBean wpPostsModelListBean) {
                Log.d("onNext", "termId:"+termId);
                Log.d("onNext", "getSimplePostsList:" + wpPostsModelListBean.getCommon().getCode() + ";" + wpPostsModelListBean.getCommon().getMsg());
                Log.d("onNext","wpPostsModelListBean.getData():"+ wpPostsModelListBean.getData().size());

                if (wpPostsModelListBean!=null&&wpPostsModelListBean.getCommon().getCode()==1){
                    Log.d("onNext","lastid:"+ wpPostsModelListBean.getData().get(wpPostsModelListBean.getData().size()-1).getId());
                    wpPostsModelList.addAll(0,wpPostsModelListBean.getData());
                    mQuickAdapter.notifyDataSetChanged();
                }else {
                    ToastUtils.showToast(getActivity(),""+wpPostsModelListBean.getCommon().getMsg());
                }

            }
        });
    }
}

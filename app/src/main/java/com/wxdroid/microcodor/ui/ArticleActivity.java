package com.wxdroid.microcodor.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.wxdroid.microcodor.R;
import com.wxdroid.microcodor.app.MicroCodorApplication;
import com.wxdroid.microcodor.base.BaseAppCompatActivity;
import com.wxdroid.microcodor.model.WpPostsModel;
import com.wxdroid.microcodor.model.bean.WpPostsModelBean;
import com.wxdroid.microcodor.network.NetWorksUtils;
import com.wxdroid.microcodor.util.BitmapUtil;
import com.wxdroid.microcodor.webview.utils.X5WebView;

import rx.Observer;

/**
 * Created by jinchun on 2016/12/9.
 */

public class ArticleActivity  extends BaseAppCompatActivity implements View.OnClickListener  {
    private X5WebView mWebView;

    private static String mUrl = "http://wxdroid.com";
    private WpPostsModelBean mWpPostsModelBean;

    @Override
    public void onClick(View view) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_article;
    }

    @Override
    protected void setupView() {
        setToolBarTitle("");
        mWebView = (X5WebView) findViewById(R.id.webview);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");//设置默认为utf-8
        showBack();
    }

    @Override
    protected void setupData() {
        long postId = getIntent().getLongExtra("postId",0);
        getWpPosts(postId);
        mWebView.loadUrl(mUrl);
    }

    @Override
    protected void setChouTi() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                setupDialog();
                break;
            case R.id.action_copyurl:
                
                break;

        }
        return true;
    }
    AlertDialog alertDialog;
    private void setupDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View   dialog = inflater.inflate(R.layout.dialog_share,(ViewGroup) findViewById(R.id.dialog_share));
        LinearLayout wechatLayout = (LinearLayout) dialog.findViewById(R.id.share_wechat);
        wechatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharewechat(false);
                alertDialog.dismiss();
            }
        });
        LinearLayout friendLayout = (LinearLayout) dialog.findViewById(R.id.share_friendcircle);
        friendLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharewechat(true);
                alertDialog.dismiss();
            }
        });
        LinearLayout moreLayout = (LinearLayout) dialog.findViewById(R.id.share_more);
        moreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                systemShareText();
                alertDialog.dismiss();
            }
        });
        alertDialog = new AlertDialog.Builder(this)
        //.setTitle("分享到...")
        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
        .setView(dialog)
        //.setIcon(R.mipmap.ic_launcher)
                .create();
        alertDialog.show();
    }

    /**
     * isTimelineCb : true:分享到朋友圈；false:分享到微信好友
     *
     * */
    private void sharewechat(boolean isTimelineCb){
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = ""+mWpPostsModelBean.getData().getGuid();
        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.title =  ""+mWpPostsModelBean.getData().getPost_title();
        msg.description = "软件开发技术分享第一平台";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        msg.thumbData = BitmapUtil.bmpToByteArray(thumb, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webapp");
        req.message = msg;
        req.scene = isTimelineCb ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        MicroCodorApplication.getInstance().wxapi.sendReq(req);

    }
    /**
     * 调用系统默认分享
     * */
    //分享文字
    public void systemShareText() {
        if (mWpPostsModelBean.getCommon().getCode()==1) {
            String shareText = "看这个，【" +
                    mWpPostsModelBean.getData().getPost_title() + "】:" +
                    mWpPostsModelBean.getData().getGuid() + "\n" +
                    "--来自微码农APP";


            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.setType("text/plain");

            //设置分享列表的标题，并且每次都显示分享列表
            startActivity(Intent.createChooser(shareIntent, "分享到..."));
        }
    }



    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
    private String setupHtml(WpPostsModel model){
        StringBuilder sb = new StringBuilder();
        sb.append("<html manifest=\"cache\">");
        sb.append("<head>");
        sb.append("<meta charset=\"utf-8\" />\n" +
                "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=11,IE=10,IE=9,IE=8\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0\">\n" +
                "<meta name=\"apple-mobile-web-app-title\" content=\"微码农\">\n" +
                "<meta http-equiv=\"Cache-Control\" content=\"no-siteapp\">\n");
        sb.append("<title>"+model.getPost_title()+"</title>");
        sb.append("<link rel='stylesheet' id='_bootstrap-css'  href='http://www.wxdroid.com/wp/wp-content/themes/dux/css/bootstrap.min.css?ver=1.0.2' type='text/css' media='all' />\n" +
                "<link rel='stylesheet' id='_fontawesome-css'  href='http://www.wxdroid.com/wp/wp-content/themes/dux/css/font-awesome.min.css?ver=1.0.2' type='text/css' media='all' />\n" +
                "<link rel='stylesheet' id='_main-css'  href='http://www.wxdroid.com/wp/wp-content/themes/dux/css/main.css?ver=1.0.2' type='text/css' media='all' />\n" +
                "<script type='text/javascript' src='http://www.wxdroid.com/wp/wp-content/themes/dux/js/libs/jquery.min.js?ver=1.0.2'></script>\n" +
                "<link rel='https://api.w.org/' href='http://www.wxdroid.com/wp-json/' />");
        sb.append("</head>");
        sb.append("<body class=\"single single-post postid-106 single-format-standard comment-open site-layout-2\" style=\"padding-top:0px;\">");
        sb.append("<section class=\"container\">\n" +
                "\t<div class=\"content-wrap\">\n" +
                "\t<div class=\"content\">\n");
        //标题区域
        sb.append("<header class=\"article-header\">\n" +
                "\t\t\t<h1 class=\"article-title\"><a href=\"javascript:void(0);\">"+model.getPost_title()+"</a></h1>\n" +
                "\t\t\t<div class=\"article-meta\">\n" +
                "\t\t\t\t<span class=\"item\">2016-04-06</span>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<span class=\"item\"></span>\n" +
                "\t\t\t\t<span class=\"item post-views\">阅读("+model.getViews_count()+")</span>\n" +
                "\t\t\t\t<span class=\"item\">评论("+model.getComment_count()+")</span>\n" +
                "\t\t\t\t<span class=\"item\"></span>\n" +
                "\t\t\t</div>\n" +
                "\t\t</header>\n");

        sb.append("<article class=\"article-content\">\n");
        sb.append(model.getPost_content());
        sb.append("</article>\n");
        sb.append("</div>"+
                "</div>"+
                "</section>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }
    /**
     * 查询单篇文章
     * */
    private void getWpPosts(long postId){
        NetWorksUtils.GetWpPost(postId, new Observer<WpPostsModelBean>() {
            @Override
            public void onCompleted() {
                Log.d("onNext-onCompleted", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                //异常
                Log.e("onNext-MAIN3", e.getLocalizedMessage() + "--" + e.getMessage());
            }

            @Override
            public void onNext(WpPostsModelBean wpPostsModelBean) {
                Log.d("onNext", "" + wpPostsModelBean.getCommon().getCode() + ";" + wpPostsModelBean.getCommon().getMsg());
                Log.d("onNext",""+ wpPostsModelBean.getData().getPost_title());
                mWpPostsModelBean = wpPostsModelBean;
                mWebView.loadData(setupHtml(mWpPostsModelBean.getData()), "text/html; charset=UTF-8", null);
            }
        });
    }
}
